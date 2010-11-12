package org.u_compare.gui.control;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.u_compare.gui.ConsolePane;
import org.u_compare.gui.WorkflowPane;
import org.u_compare.gui.WorkflowSplitPane;
import org.u_compare.gui.WorkflowTabbedPane;
import org.u_compare.gui.model.Workflow;

public class WorkflowPaneController {

	private static final boolean SHOW_CONSOLE = true;
	private static final boolean ALLOW_TABS = false;
	private static final boolean SHOW_NEW_TAB = true; //TODO
	
	private ArrayList<WorkflowSplitPane> workflowSplitPanes = new ArrayList<WorkflowSplitPane>();
	
	public JComponent initialize(){
		
		//Do we need to add a default workflow?
		if(workflowSplitPanes.size() == 0){
			workflowSplitPanes.add(constructDefaultWorkflow());
		}
		
		if(ALLOW_TABS){
	
			WorkflowTabbedPane tabbedPane = new WorkflowTabbedPane();
			
			for(WorkflowSplitPane workflowSplitPane : workflowSplitPanes){
				tabbedPane.addWorkflow(workflowSplitPane);
			}
			
			return tabbedPane;
			
		}else{
			
			assert(workflowSplitPanes.size() == 1);
			return workflowSplitPanes.get(0);
			
		}
		
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
		
		for(Workflow workflow : workflows) {
			workflowSplitPanes.add(constructWorkflow(workflow));
		}
		
		return initialize();
	}
	
	private WorkflowSplitPane constructWorkflow(Workflow workflow){
		
		WorkflowController workflowModel = new WorkflowController(workflow);
		
		// Construct the view
		WorkflowPane workflowPane = new WorkflowPane(workflowModel.getView());
		
		ConsolePane consolePane = null;
		
		if(SHOW_CONSOLE){
			consolePane = new ConsolePane(workflow);
		}
		
		return new WorkflowSplitPane(workflowPane, consolePane);
	}
	
	private WorkflowSplitPane constructDefaultWorkflow(){
		
		return constructWorkflow(WorkflowPaneController.defaultWorkflow());
		
	}
	
	public static Workflow defaultWorkflow(){

		Workflow workflow = new Workflow();
		
		workflow.setName("Untitled Workflow (Click here to edit)");
		workflow.setDescription("This is a new workflow. Click here to edit its description. Duis quis arcu id enim elementum gravida quis sit amet justo. Cras non enim nec velit aliquet luctus sed faucibus arcu. Phasellus dolor quam, dapibus a consequat eget, fringilla vitae ipsum. Donec tristique elementum turpis, in pellentesque nulla viverra vitae. Curabitur eget turpis non quam auctor ornare. Aliquam tempus quam vitae lectus consectetur fringilla. Vivamus posuere pharetra elit ac interdum. Aenean vestibulum mattis justo et malesuada. Ut ultrices, nisl sit amet tempor porttitor, nulla ipsum feugiat purus, porta tincidunt sem sapien nec leo. Phasellus rhoncus elit sit amet lectus adipiscing vulputate. ");
		
		return workflow;

	}
}
