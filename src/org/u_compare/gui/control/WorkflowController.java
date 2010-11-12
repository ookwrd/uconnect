package org.u_compare.gui.control;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

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
		
		System.out.println(component.checkUnsavedChanges());
		
		if(component.checkUnsavedChanges()){
		
			JOptionPane.showConfirmDialog(componentView, "Unsaved changes exist to this workflow.\nDo you wish to save before closing?");
			//Yes no cancel
			
		}else{
			
			JOptionPane.showInternalMessageDialog(componentView, "Test Message");
			
			//JOptionPane.sho("Do you want to close this workflow?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION,null, new String[]{"Close","Cancel"});
		     

			//close tab
			
		}
		
		
	}
	
	private void closeWorkflow(){
		
		/**
		 * TODO
		 * 
		 * remove the view
		 * remove the control?
		 * 
		 * what do we do with the model?
		 */
		
		((WorkflowPanel)componentView).closeWorkflow();
		
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
