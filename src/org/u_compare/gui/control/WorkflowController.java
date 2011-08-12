package org.u_compare.gui.control;

import org.u_compare.gui.component.WorkflowConstructionPanel;
import org.u_compare.gui.model.Workflow;

public class WorkflowController extends ComponentController {

	
	public WorkflowController(Workflow component,
			boolean showWorkflowControlPanel, boolean showWorkflowDetails,
			boolean allowEditing) {
		super(allowEditing);
		
		this.component = component;
		this.componentView = new WorkflowConstructionPanel(component, this,
				showWorkflowControlPanel, showWorkflowDetails);
		
		
	}
	
	@Override
	public WorkflowConstructionPanel getView(){
		
		return (WorkflowConstructionPanel)this.componentView;
		
	}
	
	
	public void workflowPlayRequest(){
		assert(component.isWorkflow());
	
		((Workflow)component).runWorkflow();

	}
	
	
	public void workflowPauseRequest(){
		assert(component.isWorkflow());
		
		((Workflow)component).runWorkflow();	
	}
	
	public void workflowStopRequest(){
		assert(component.isWorkflow());
		
		((Workflow)component).runWorkflow();		
	}
	
}
