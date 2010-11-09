package org.u_compare.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.u_compare.gui.debugging.GUITestingHarness;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

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
//TODO: Need to listen to changes to workflows, for naming the tab
//TODO: Also needs listeners for the state of the workflow
//TODO: Should never be empty
//TODO: "Create new workflow" tab
//TODO: Animated icons by calling "set icon" with a time interval?
public class WorkflowTabbedPane extends JTabbedPane implements ChangeListener{

	private static final int MAX_LENGTH = 7;
	
	// Configuration
	private static final String TOOLTIP_TEXT =
		"Your current workflow(s)";
	
	//XXX: We will conflict with the "Change tab" key shortcuts, override!
	
	//TODO: Argue with Luke regarding the states a workflow can have.
	// No, you in fact have different states... Odd...
	//TODO: We do want to animate these in the end
	private static boolean icons_loaded = false; 

	private static Icon WORKFLOW_STOPPED; //TODO: Indicates forcefully stopped?
	private static Icon WORKFLOW_BUILDING;
	private static Icon WORKFLOW_RUNNING;
	private static Icon WORKFLOW_FINISHED;
	private static Icon WORKFLOW_PAUSED;
	private static Icon WORKFLOW_FAILED;

	private final static String WORKFLOW_STOPPED_PATH = 
		"gfx/workflow_stopped.png";
	private final static String WORKFLOW_BUILDING_PATH = 
		"gfx/workflow_building.png";
	private final static String WORKFLOW_RUNNING_PATH =
		"gfx/workflow_running.png";
	private final static String WORKFLOW_FINISHED_PATH =
		"gfx/workflow_finished.png";
	private final static String WORKFLOW_PAUSED_PATH =
		"gfx/workflow_paused.png";
	private final static String WORKFLOW_FAILED_PATH =
		"gfx/workflow_failed.png";
	
	
	public WorkflowTabbedPane() {
		WorkflowTabbedPane.load_icons();
		assert WorkflowTabbedPane.icons_loaded == true;
		
		this.setToolTipText(WorkflowTabbedPane.TOOLTIP_TEXT);
		//this.addWorkflow(splitPane);
		initializeNewWorkflowTab();
	}
	
	private void initializeNewWorkflowTab(){
		
		ActionListener workflowButtonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBlankTab();
			}
		};
		
		JButton newWorkflowButton = new JButton("New");
		newWorkflowButton.setOpaque(false);
		newWorkflowButton.addActionListener(workflowButtonListener);
		
		addTab("New Workflow", new JLabel("test"));
		setTabComponentAt(0, newWorkflowButton);
		
		this.addChangeListener(this);
	}
	
	private void addBlankTab(){
		addWorkflow(GUITestingHarness.constructWorkflow(GUITestingHarness.blankWorkflow()));
		
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
					"The workflow has finished it's execution");
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_PAUSED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_PAUSED = new ImageIcon(image_url,
					"The workflow is currently paused");
			
			image_url = WorkflowTabbedPane.class.getResource(
					WorkflowTabbedPane.WORKFLOW_FAILED_PATH);
			assert image_url != null;
			WorkflowTabbedPane.WORKFLOW_FAILED = new ImageIcon(image_url,
					"The workflow has failed to execute");
			
			WorkflowTabbedPane.icons_loaded = true;
			return;
		}
		else {
			return;
		}
	}
	
	//TODO: Where does it get it's title from?
	public void addWorkflow(WorkflowSplitPane splitPane) {
		
		Component topComponent = splitPane.getWorkflowPane()
				.getTopWorkflowComponent().getComponent();
		
		
		int inserted_index = this.getTabCount() - 1;
		//TODO: Different mouse-over depending on if focused or not
		// "Your current workflow" vs. "View this workflow"
		this.insertTab(cleanTitle(topComponent.getName()), null, splitPane, null, inserted_index);//Why does the API force a fully specified tab when using insert rather than add?
		this.setToolTipTextAt(inserted_index, topComponent.getDescription());
		
		IconizedCloseableTabFlapComponent tabFlapComponent = new IconizedCloseableTabFlapComponent(this,
				WorkflowTabbedPane.WORKFLOW_STOPPED);
		
		splitPane.linkTabbedPane(this, tabFlapComponent);
		
		this.setTabComponentAt(inserted_index,
				tabFlapComponent);
		
		setSelectedIndex(inserted_index);
		
	}
	
	@Override
	public void setIconAt(int index, Icon icon) {
		((IconizedCloseableTabFlapComponent) this.getComponentAt(index))
				.setStatusIcon(icon);
	}
	
	@Override
	// Asks the controller for permission before calling remove
	public void remove(int i) {
		//TODO: Ask controller
		System.err.println("WARNING: WorkflowTabbedPane - Did not ask controller for permission to close");
		super.remove(i);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		
		if(getSelectedIndex() == getTabCount()-1){//damn this gets loaded repeatedly at startup
		//	addWorkflow(GUITestingHarness.constructWorkflow(GUITestingHarness.blankWorkflow()));
		}
		
	}
}