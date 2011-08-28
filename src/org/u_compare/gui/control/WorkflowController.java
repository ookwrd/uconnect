package org.u_compare.gui.control;

import org.u_compare.gui.component.WorkflowPanel;
import org.u_compare.gui.model.Workflow;

public class WorkflowController extends ComponentController {

	
	public WorkflowController(Workflow component,
			boolean showWorkflowControlPanel, boolean showWorkflowDetails, 
			boolean showSavePanel, boolean allowEditing) {
		super(allowEditing);
		
		this.component = component;
		this.componentView = new WorkflowPanel(component, this,
				showWorkflowControlPanel, showWorkflowDetails, showSavePanel);
	}
	
	@Override
	public WorkflowPanel getView(){
		
		return (WorkflowPanel)this.componentView;
		
	}
	
	
	public void workflowPlayRequest(){
		assert(component.isWorkflow());
		((Workflow)component).runResumeWorkflow();
	}
	
	
	public void workflowPauseRequest(){
		assert(component.isWorkflow());
		((Workflow)component).pauseWorkflow();	
	}
	
	public void workflowStopRequest(){
		assert(component.isWorkflow());
		((Workflow)component).stopWorkflow();		
	}
	
}
