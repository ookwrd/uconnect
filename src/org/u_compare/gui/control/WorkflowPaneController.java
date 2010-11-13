package org.u_compare.gui.control;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.u_compare.gui.ConsolePane;
import org.u_compare.gui.WorkflowPane;
import org.u_compare.gui.WorkflowPanel;
import org.u_compare.gui.WorkflowSplitPane;
import org.u_compare.gui.WorkflowTabbedPane;
import org.u_compare.gui.model.Workflow;

public class WorkflowPaneController {

	private static final boolean SHOW_CONSOLE = true;
	private static final boolean ALLOW_TABS = true;
	private static final boolean SHOW_NEW_TAB = true; //TODO
	
	private WorkflowTabbedPane tabbedPane;
	
	private JComponent init(ArrayList<WorkflowSplitPane> workflowSplitPanes){
		
		if(workflowSplitPanes.size() == 0){
			workflowSplitPanes.add(constructDefaultWorkflow());
		}
		
		if(ALLOW_TABS){
			
			tabbedPane = new WorkflowTabbedPane(this);
			
			for(WorkflowSplitPane workflowSplitPane : workflowSplitPanes){
				tabbedPane.addWorkflow(workflowSplitPane);
			}
			
			return tabbedPane;
			
		}else{
			
			assert(workflowSplitPanes.size() == 1);
			return workflowSplitPanes.get(0);
			
		}
		
	}
	
	public JComponent initialize(){
		
		ArrayList<WorkflowSplitPane> workflowSplitPanes = new ArrayList<WorkflowSplitPane>();
		return init(workflowSplitPanes);
		
	}
	
	public JComponent initialize(Workflow workflow){
		ArrayList<Workflow> workflows = new ArrayList<Workflow>();
		workflows.add(workflow);
		return initialize(workflows);
	}
	
	@SuppressWarnings("unused")
	public JComponent initialize(ArrayList<Workflow> workflows){
		
		if(!ALLOW_TABS && workflows.size() > 1){
			throw new IllegalArgumentException("As Workflow Tabs are currently disabled this method can handle at most a single workflow as input.");
		}
		
		ArrayList<WorkflowSplitPane> workflowSplitPanes = new ArrayList<WorkflowSplitPane>();
		
		for(Workflow workflow : workflows) {
			workflowSplitPanes.add(constructWorkflow(workflow));
		}
		
		return init(workflowSplitPanes);
	}
	
	private WorkflowSplitPane constructWorkflow(Workflow workflow){
		workflow.setComponentSaved();//TODO should this be moved to workflow constructor?
		
		WorkflowController workflowController = new WorkflowController(workflow);
		
		// Construct the view
		WorkflowPane workflowPane = new WorkflowPane(workflowController.getView());
		
		ConsolePane consolePane = null;
		
		if(SHOW_CONSOLE){
			consolePane = new ConsolePane(workflow);
			consolePane.addConsoleMessage("Workflow Loaded.");
		}
		
		return new WorkflowSplitPane(workflowPane, consolePane);
	}
	
	private WorkflowSplitPane constructDefaultWorkflow(){
		
		return constructWorkflow(WorkflowPaneController.defaultWorkflow());
		
	}
	
	public static Workflow defaultWorkflow(){

		Workflow workflow = new Workflow();
		
		workflow.setName("Untitled Workflow (Click to edit)");
		workflow.setDescription("This is a new workflow. Click here to edit its description. Duis quis arcu id enim elementum gravida quis sit amet justo. Cras non enim nec velit aliquet luctus sed faucibus arcu. Phasellus dolor quam, dapibus a consequat eget, fringilla vitae ipsum. Donec tristique elementum turpis, in pellentesque nulla viverra vitae. Curabitur eget turpis non quam auctor ornare. Aliquam tempus quam vitae lectus consectetur fringilla. Vivamus posuere pharetra elit ac interdum. Aenean vestibulum mattis justo et malesuada. Ut ultrices, nisl sit amet tempor porttitor, nulla ipsum feugiat purus, porta tincidunt sem sapien nec leo. Phasellus rhoncus elit sit amet lectus adipiscing vulputate. ");
		
		return workflow;

	}

	public void requestNewWorkflow() {
		assert(ALLOW_TABS);
		
		tabbedPane.addWorkflow(constructDefaultWorkflow());
		
	}
	

	public void requestWorkflowClose(Workflow workflow){
		assert(ALLOW_TABS);
		
		if(workflow.checkUnsavedChanges()){
		
			int reply = JOptionPane.showOptionDialog(null, "Unsaved changes exist to this workflow.\n\nDo you wish to save them before closing?", "Unsaved Changes Exist!", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save before Closing","Close without Saving","Cancel"}, 2);
			
			if(reply == 0) {//Save and close
				saveWorkflow(workflow);
				closeWorkflow(workflow);
			}else if (reply == 1) {//Close without Saving
				closeWorkflow(workflow);
			}
			
		}else{
			
			int reply = JOptionPane.showOptionDialog(null, "Do you want to close this workflow?", "Close Workflow?", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Close","Cancel"}, 1);
		     
			if(reply == 0){//Close
				closeWorkflow(workflow);
			}
			
		}
		
	}
	
	private void closeWorkflow(Workflow workflow){
		
		tabbedPane.removeWorkflow(workflow);
		
	}
	
	private void saveWorkflow(Workflow workflow){
		
		//TODO this is going to depend on a few things... like if we have component library...
		
	}
}