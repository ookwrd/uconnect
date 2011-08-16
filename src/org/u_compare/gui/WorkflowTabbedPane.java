package org.u_compare.gui;

import java.awt.Graphics;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.Workflow.WorkflowStatusListener;

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
public class WorkflowTabbedPane extends ButtonTabbedPane
	implements WorkflowStatusListener, ChangeListener {
	
	// Configuration
	private static final String TOOLTIP_TEXT =
		"Your current workflow(s)";
	/* The maximum number of characters displayed for a tab name */
	private static final int MAX_TITLE_LENGTH = 15;
	
	//XXX: We will conflict with the "Change tab" key shortcuts, override!
	
	private static boolean icons_loaded = false;

	private static Icon WORKFLOW_STOPPED;
	private static Icon WORKFLOW_RUNNING;
	private static Icon WORKFLOW_FINISHED;
	private static Icon WORKFLOW_PAUSED;
	private static Icon WORKFLOW_ERROR;

	private final static String WORKFLOW_STOPPED_PATH = 
		"gfx/workflow_stopped.png";
	private final static String WORKFLOW_RUNNING_PATH =
		"gfx/workflow_running.gif";
	private final static String WORKFLOW_FINISHED_PATH =
		"gfx/workflow_finished.png";
	private final static String WORKFLOW_PAUSED_PATH =
		"gfx/workflow_paused.png";
	private final static String WORKFLOW_ERROR_PATH =
		"gfx/workflow_error.png";
	
	private final static String WORKFLOW_STOPPED_DESCRIPTION =
		"The workflow is currently stopped";
	private final static String WORKFLOW_RUNNING_DESCRIPTION =
		"The workflow is currently running";
	private final static String WORKFLOW_FINISHED_DESCRIPTION =
		"The workflow has finished executioning";
	private final static String WORKFLOW_PAUSED_DESCRIPTION =
		"The workflow is currently paused";
	private final static String WORKFLOW_ERROR_DESCRIPTION =
		"An error has occured with this workflow";
	private WorkflowPaneController controller;
	
	public WorkflowTabbedPane(WorkflowPaneController controller) {
		super();
		
		WorkflowTabbedPane.load_icons();
		assert WorkflowTabbedPane.icons_loaded == true;
		
		this.controller = controller;
		
		this.setToolTipText(WorkflowTabbedPane.TOOLTIP_TEXT);
		
		this.initializeTabButtons();
		
		this.addChangeListener(this);
	}
	
	private void initializeTabButtons(){
		
		if(WorkflowPaneController.SHOW_NEW_TAB){
			ButtonTabFlap newWorkflowButton = addButtonTab(WorkflowPaneController.NEW_TAB_NAME, controller);
			newWorkflowButton.setActionCommand(WorkflowPaneController.NEW_ACTION_COMMAND);
			newWorkflowButton.addActionListener(controller);	
		}
	
		if(WorkflowPaneController.SHOW_LOAD_TAB){
			ButtonTabFlap loadWorkflowButton = addButtonTab(WorkflowPaneController.LOAD_TAB_NAME, controller);
			loadWorkflowButton.setActionCommand(WorkflowPaneController.LOAD_ACTION_COMMAND);
			loadWorkflowButton.addActionListener(controller);
		}
	}
	
	//TODO should this really be public? and if so, should it be here? /luke
	public static String cleanTitle(String title){
		if(title.length() < MAX_TITLE_LENGTH){
			return title;
		}else{
			return title.substring(0, MAX_TITLE_LENGTH-3) + "...";
		}
	}

	public void addWorkflow(WorkflowHorizontalSplitPane splitPane) {
		
		Workflow workflow = splitPane.getWorkflowPane().getAssociatedWorkflow();
		workflow.registerWorkflowStatusListener(this);
		
		Component topComponent = splitPane.getWorkflowPane()
				.getTopWorkflowComponent().getComponent();
		
		int inserted_index = numberOfNonButtonTabs();
		
		//TODO: Different mouse-over depending on if focused or not
		// "Your current workflow" vs. "View this workflow"
		splitPane.setName(cleanTitle(topComponent.getName()));
		this.add(splitPane, inserted_index);
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
			new IconizedCloseableTabFlapComponent(this, WorkflowTabbedPane.WORKFLOW_STOPPED, dropListener, WorkflowPaneController.ALLOW_TAB_CLOSE);
		
		splitPane.linkTabbedPane(this, tabFlapComponent);
		
		this.setTabComponentAt(inserted_index,
				tabFlapComponent);
		
		this.setSelectedIndex(inserted_index);
		
	}
	
	@Override
	public void setIconAt(int index, Icon icon) {
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
				.setStatusIcon(icon, false);
	}
	
	/**
	 * Use this version if it is an icon that should be cleared when the tab is visited.
	 * 
	 * @param index
	 * @param icon
	 */
	public void setNotifactionIconAt(int index, Icon icon){
		if(index == getSelectedIndex()){
			clearIconAt(index);
			return;
		}
		
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
		.setStatusIcon(icon, true);
	}

	/**
	 *  see if we need to clear a notification Icon.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
        clearNotificationIconAt(getSelectedIndex());
	}
	
	public void clearNotificationIconAt(int index){
		if(this.getTabComponentAt(index) instanceof IconizedCloseableTabFlapComponent){
			IconizedCloseableTabFlapComponent tab = ((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index));
			if(tab.iconIsNotification()){
				clearIconAt(index);
			}
		}
	}
	
	public void clearIconAt(int index){
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index)).setStatusIcon(new Icon() {//An empty icon.
			@Override
			public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
			}
			@Override
			public int getIconWidth() {
				return 16;
			}
			@Override
			public int getIconHeight() {
				return 16;
			}
		}, false);
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
		//TODO Check with the control
		super.remove(i);
	}
	
	//TODO: We also need a safe add!

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		for (int i = 0; i < numberOfNonButtonTabs(); i++) {
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
						this.setNotifactionIconAt(i, WorkflowTabbedPane.WORKFLOW_ERROR);	
						break;
					case PAUSED:
						this.setIconAt(i, WorkflowTabbedPane.WORKFLOW_PAUSED);
						break;
					case FINISHED:
						this.setNotifactionIconAt(i, WorkflowTabbedPane.WORKFLOW_FINISHED);	
						break;
					default:
						assert false: "Unimplemented WorkflowStatus received";
				}
			}
		}
	}
	
	public void removeWorkflow(Workflow workflow) {
		for (int i = 0; i < numberOfNonButtonTabs(); i++) {
			WorkflowHorizontalSplitPane currentComponent =
				(WorkflowHorizontalSplitPane) this.getComponentAt(i); 
			if (currentComponent.getWorkflowPane()
					.getAssociatedWorkflow().equals(workflow)) {
				this.safeRemove(i);
				break;
			}
		
		}
	}
	

	private static synchronized void load_icons() {
		if (WorkflowTabbedPane.icons_loaded == false) {
			URL image_url;
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_STOPPED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_STOPPED = new ImageIcon(image_url,
					WorkflowTabbedPane.WORKFLOW_STOPPED_DESCRIPTION);
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_RUNNING_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_RUNNING = new ImageIcon(image_url,
					WorkflowTabbedPane.WORKFLOW_RUNNING_DESCRIPTION);
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_FINISHED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_FINISHED = new ImageIcon(image_url,
					WorkflowTabbedPane.WORKFLOW_FINISHED_DESCRIPTION);
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_PAUSED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_PAUSED = new ImageIcon(image_url,
					WorkflowTabbedPane.WORKFLOW_PAUSED_DESCRIPTION);
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_ERROR_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_ERROR = new ImageIcon(image_url,
					WorkflowTabbedPane.WORKFLOW_ERROR_DESCRIPTION);
			
			WorkflowTabbedPane.icons_loaded = true;
			return;
		}
		else {
			return;
		}
	}

}