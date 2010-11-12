package org.u_compare.gui.control;

import org.u_compare.gui.WorkflowPanel;
import org.u_compare.gui.component.ComponentPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.InvalidStatusException;
import org.u_compare.gui.model.Workflow;

public class WorkflowController extends ComponentController {

	public WorkflowController(Workflow component) {
		
		this.component = component;
		this.componentView = new WorkflowPanel(component, this);
		
		
	}
	
	public void workflowCloseRequested(){
		
		if(component.checkUnsavedChanges()){
		
			//Yes no cancel
			
		}else{
			
			//close tab
			
		}
		
		
	}
	
	@Override
	public WorkflowPanel getView(){
		
		return (WorkflowPanel)this.componentView;
		
	}
	
	
	public void workflowPlayRequest(){

		assert(component.isWorkflow());
	
		try {
			((Workflow)component).runWorkflow();
		} catch (InvalidStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void workflowPauseRequest(){
		
		assert(component.isWorkflow());
		
		try {
			((Workflow)component).runWorkflow();
		} catch (InvalidStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void workflowStopRequest(){
		
		assert(component.isWorkflow());
		
		try {
			((Workflow)component).runWorkflow();
		} catch (InvalidStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
