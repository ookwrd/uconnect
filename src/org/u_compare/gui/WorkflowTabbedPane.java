package org.u_compare.gui;

import java.awt.Graphics;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.WorkflowStatusListener;

/**
 * Handles workflow tabs.
 * 
 * @author pontus
 * @author luke
 * @version 2009-09-27
 */

@SuppressWarnings("serial")
//TODO: Enable scrolling among tabs
//TODO: Should have mnemonics
//TODO: Should never be empty, we always have at least one tab
public class WorkflowTabbedPane extends JTabbedPane
	implements WorkflowStatusListener {
	
	private static final boolean DEBUG = false;
	
	// Configuration
	private static final String TOOLTIP_TEXT =
		"Your current workflow(s)";
	/* The maximum number of characters displayed for a tab name */
	private static final int MAX_LENGTH = 15;
	
	//XXX: We will conflict with the "Change tab" key shortcuts, override!
	
	// No, you in fact have different states... Odd...
	//TODO: We do want to animate these in the end
	private static boolean icons_loaded = false;

	 //TODO: STOPPED should also indicate forcefully stopped?
	private static Icon WORKFLOW_STOPPED;
	private static Icon WORKFLOW_RUNNING;
	private static Icon WORKFLOW_FINISHED;
	private static Icon WORKFLOW_PAUSED;
	private static Icon WORKFLOW_ERROR;

	private final static String WORKFLOW_STOPPED_PATH = 
		"gfx/workflow_stopped.png";
	private final static String WORKFLOW_RUNNING_PATH =
		"gfx/workflow_running.gif";
	// Results are ready
	private final static String WORKFLOW_FINISHED_PATH =
		"gfx/workflow_finished.png";
	private final static String WORKFLOW_PAUSED_PATH =
		"gfx/workflow_paused.png";
	private final static String WORKFLOW_ERROR_PATH =
		"gfx/workflow_error.png";
	
	private WorkflowPaneController controller;
	
	public WorkflowTabbedPane(WorkflowPaneController controller) {
		WorkflowTabbedPane.load_icons();
		assert WorkflowTabbedPane.icons_loaded == true;
		
		this.controller = controller;
		
		this.setToolTipText(WorkflowTabbedPane.TOOLTIP_TEXT);
		
		this.initializeNewWorkflowTab();
	}
	
	private void initializeNewWorkflowTab(){
		
		ActionListener workflowButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.requestNewWorkflow();
			}
		};
		
		JButton newWorkflowButton = new ButtonTabFlap("New Workflow", controller);
		newWorkflowButton.addActionListener(workflowButtonListener);
		
		addTab("New Workflow", new JLabel(){//TODO can this be moved into ButtonTab as a generalization?
			public void paint(Graphics g){//Makes the tab undisplayable
				setSelectedIndex(getTabCount()-2);//If this is drawn, show the previous tab instead.
			}
		});
		setTabComponentAt(0, newWorkflowButton);
		
		//setEnabledAt(0, false);//Unfortunately not enough to prevent access to the New Tab. Fill the tab bar in OSX and then you can scroll to it. See Fix with StateChange Listener.
	}
	
	public static String cleanTitle(String title){
		if(title.length() < MAX_LENGTH){
			return title;
		}else{
			return title.substring(0, MAX_LENGTH-3) + "...";
		}
	}

	private static synchronized void load_icons() {
		if (WorkflowTabbedPane.icons_loaded == false) {
			//TODO: Extract descriptions to static final fields
			URL image_url;
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_STOPPED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_STOPPED = new ImageIcon(image_url,
					"The workflow is currently stopped");
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_RUNNING_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_RUNNING = new ImageIcon(image_url,
					"The workflow is currently running");
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_FINISHED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_FINISHED = new ImageIcon(image_url,
					"The workflow has finished its execution");
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_PAUSED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_PAUSED = new ImageIcon(image_url,
					"The workflow is currently paused");
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_ERROR_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_ERROR = new ImageIcon(image_url,
					"The workflow has failed to execute");
			
			WorkflowTabbedPane.icons_loaded = true;
			return;
		}
		else {
			return;
		}
	}
	
	//TODO: Where does it get it's title from?
	public void addWorkflow(WorkflowHorizontalSplitPane splitPane) {
		
		Workflow workflow = splitPane.getWorkflowPane().getAssociatedWorkflow();
		workflow.registerWorkflowStatusListener(this);
		
		Component topComponent = splitPane.getWorkflowPane()
				.getTopWorkflowComponent().getComponent();
		
		int inserted_index = this.getTabCount() - 1;
		//TODO: Different mouse-over depending on if focused or not
		// "Your current workflow" vs. "View this workflow"
		this.insertTab(cleanTitle(topComponent.getTitle()), null, splitPane,
				null, inserted_index);
		// Why does the API force a fully specified tab when using insert
		// rather than add?
		this.setToolTipTextAt(inserted_index, topComponent.getDescription());
		
		
		//Listener for switching between tabs on component drag.
		DropTargetListener dropListener = new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				//Nothing
			}
			
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				//Get entered tabs index and set that tab selected.
				setSelectedIndex(indexOfTabComponent(((DropTarget)dtde.getSource()).getComponent()));	
			}
		};
		
		IconizedCloseableTabFlapComponent tabFlapComponent = 
			new IconizedCloseableTabFlapComponent(this, WorkflowTabbedPane.WORKFLOW_STOPPED, dropListener);
		
		splitPane.linkTabbedPane(this, tabFlapComponent);
		
		this.setTabComponentAt(inserted_index,
				tabFlapComponent);
		
		this.setSelectedIndex(inserted_index);
		
	}
	
	@Override
	public void setIconAt(int index, Icon icon) {
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
				.setStatusIcon(icon);
	}
	
	
	@Override
	public void remove(int i) {
		controller.requestWorkflowClose(
				((WorkflowHorizontalSplitPane) this.getComponentAt(i))
				.getWorkflowPane().getAssociatedWorkflow());
	}
	
	// Use this instead of the internal one to conform with our selection
	// policies.
	private void safeRemove(int i) {
		//TODO: Implement this by referring to the super class
		System.err.println("WorkflowTabbedPane: safeRemove(int i) "
				+ "called but not implemented");
	}
	
	public void insertTab(String title, Icon icon,
			java.awt.Component component, String tip, int index) {
		//TODO: Override the default behaviour here to handle focus etc.
		if (WorkflowTabbedPane.DEBUG) {
			System.err.println(this.getClass().getName() +
					": insertTab called, " +
					"defaulting to super class implementation for now " +
					"until we implement our own");
		}
		super.insertTab(title, icon, component, tip, index);
	}
	//TODO: We also need a safe add!

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		for (int i = 0; i < this.getTabCount() - 1; i++) {
			WorkflowHorizontalSplitPane tab =
				(WorkflowHorizontalSplitPane) this.getComponentAt(i);
			
			if (tab.getWorkflowPane()
					.getAssociatedWorkflow().equals(workflow)) {
				switch (workflow.getStatus()) {
					case RUNNING:
					case LOADING:
					case INITIALIZING:
						this.setIconAt(i, WorkflowTabbedPane.WORKFLOW_RUNNING);
						break;
					case READY:
						this.setIconAt(i, WorkflowTabbedPane.WORKFLOW_STOPPED);
						break;
					case ERROR:
						this.setIconAt(i, WorkflowTabbedPane.WORKFLOW_ERROR);
						break;
					case PAUSED:
						this.setIconAt(i, WorkflowTabbedPane.WORKFLOW_PAUSED);
						break;
					case FINISHED:
						this.setIconAt(i, WorkflowTabbedPane.WORKFLOW_FINISHED);
						break;
					default:
						assert false: "Unimplemented WorkflowStatus received";
				}
			}
		}
	}
	
	public void removeWorkflow(Workflow workflow) {
		for (int i = 0; i < this.getTabCount() - 1; i++) {
			WorkflowHorizontalSplitPane currentComponent =
				(WorkflowHorizontalSplitPane) this.getComponentAt(i); 
			if (currentComponent.getWorkflowPane()
					.getAssociatedWorkflow().equals(workflow)) {
				this.safeRemove(i);
				break;
			}
		
		}
	}
}