package org.u_compare.gui;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import org.u_compare.gui.model.Component;

/**
 * Handles workflow tabs.
 * 
 * @author pontus
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
public class WorkflowTabbedPane extends JTabbedPane {

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
	
	
	public WorkflowTabbedPane(WorkflowSplitPane splitPane) {
		WorkflowTabbedPane.load_icons();
		assert WorkflowTabbedPane.icons_loaded == true;
		
		this.setToolTipText(WorkflowTabbedPane.TOOLTIP_TEXT);
		this.addWorkflow(splitPane);
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
		
		splitPane.linkTabbedPane(this);
		
		//XXX: -1 if we have a "New tab"-tab
		int inserted_index = this.getTabCount();
		//TODO: Different mouse-over depending on if focused or not
		// "Your current workflow" vs. "View this workflow"
		this.addTab(topComponent.getName(), splitPane);
		this.setToolTipTextAt(inserted_index, topComponent.getDescription());
		this.setTabComponentAt(inserted_index,
				new IconizedCloseableTabFlapComponent(this,
						WorkflowTabbedPane.WORKFLOW_STOPPED));
		
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
}