package org.u_compare.gui.model;

import java.util.ArrayList;

import org.u_compare.gui.model.parameters.Parameter;

/**
 * Model class representing a UIMA Workflow. The workflow is modelled as a special UIMAAggregateComponent.
 * 
 * @author lukemccrohon
 *
 */
public class UIMAWorkflow extends AbstractUIMAAggregateComponent {

	public enum WorkflowStatus {LOADING,READY,INITIALIZING,RUNNING,ERROR,STOPPED};
	
	private WorkflowStatus status = WorkflowStatus.LOADING;
	
	private ArrayList<WorkflowStatusListener> workflowStatusListeners;
	
	
	/**
	 * Creates an empty workflow.
	 */
	public UIMAWorkflow(){
		super();		
		workflowStatusListeners = new ArrayList<WorkflowStatusListener>();
		
	}
	
	
	/**
	 * Creates a workflow from the specified list of components.
	 * 
	 * @param components
	 */
	public UIMAWorkflow(ArrayList<UIMAComponent> components){
		super();
		
		setSubComponents(components);
		workflowStatusListeners = new ArrayList<WorkflowStatusListener>();
	}
	
	/**
	 * This class is by definition a workflow.
	 */
	public boolean isWorkflow(){
		return true;
	}
	
	/**
	 * Should be used to start workflow processing.
	 * 
	 */
	private void runWorkflow() throws InvalidStatusException {
		
		if(status != WorkflowStatus.READY){
			throw new InvalidStatusException(status, WorkflowStatus.READY);
		}
		
		setStatus(WorkflowStatus.INITIALIZING);
		
		
		
		setStatus(WorkflowStatus.RUNNING);
		
		//TODO
		
	}
	
	/**
	 * should be used to stop workflow processing
	 */
	private void stopWorkflow() throws InvalidStatusException {
		
		if(status != WorkflowStatus.RUNNING){
			throw new InvalidStatusException(status, WorkflowStatus.RUNNING);
		}
		
	}
	
	/**
	 * Status should always be changed via this method.
	 * 
	 * @param newStatus
	 */
	private void setStatus(WorkflowStatus newStatus){
		
		if(status == newStatus){
			return;
		}
		
		status = newStatus;
		
		notifyWorkflowStatusListeners();
		
	}
	
	/**
	 * Returns the current status of this workflow.
	 * 
	 * @return
	 */
	public WorkflowStatus getStatus(){
		
		return status;
	}
	
	
	/**
	 * Registers a new class to listen to workflowStatusListener changes.
	 * 
	 * @param listener
	 */
	public void registerWorkflowStatusListener(WorkflowStatusListener listener){
		
		workflowStatusListeners.add(listener);
		
	}
	
	/**
	 * Should be called whenever the status of workflow processing is changed.
	 */
	private void notifyWorkflowStatusListeners(){
		
		for(WorkflowStatusListener listener : workflowStatusListeners){
			listener.workflowStatusChanged(this);
		}
		
	}

}

