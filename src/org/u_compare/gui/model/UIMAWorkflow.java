package org.u_compare.gui.model;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

import org.u_compare.gui.model.parameters.Parameter;

/**
 * Model class representing a UIMA Workflow. The workflow is modelled as a special UIMAAggregateComponent.
 * 
 * @author lukemccrohon
 *
 */
public class UIMAWorkflow extends AbstractUIMAAggregateComponent {

	//for debugging, delete when no longer needed.
	private Timer timer = new Timer();
	
	public enum WorkflowStatus {LOADING,READY,INITIALIZING,RUNNING,ERROR,PAUSED};
	
	private WorkflowStatus status = WorkflowStatus.READY;
	
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
	public void runWorkflow() throws InvalidStatusException {
		
		if(status != WorkflowStatus.READY){
			throw new InvalidStatusException(status, WorkflowStatus.READY);
		}
		
		setStatus(WorkflowStatus.INITIALIZING);
		
		//TODO
		
		setStatus(WorkflowStatus.RUNNING);
		
		//TODO
		
		
		TimerTask task = new TimerTask() {
			
			private int x = 30;
			
			@Override
			public void run() {
				x--;
				notifyWorkflowStatusListeners();
				if(x<1){
					timer.cancel();
					afterRunning();
				}
			}
		};
		
		timer.schedule(task,0,1000);
	}
	
	private void afterRunning(){

		setStatus(WorkflowStatus.READY);
		
	}
	
	
	/**
	 * used to pause the workflow processing
	 * 
	 * @throws InvalidStatusException
	 */
	public void pauseWorkflow() throws InvalidStatusException{
		
		if(status != WorkflowStatus.RUNNING){
			throw new InvalidStatusException(status, WorkflowStatus.RUNNING);
		}
		
		//TODO make this do something
		
	}
	
	/**
	 * should be used to stop workflow processing
	 */
	public void stopWorkflow() throws InvalidStatusException {
		
		if(!(status == WorkflowStatus.RUNNING || status == WorkflowStatus.PAUSED)){
			throw new InvalidStatusException(status, WorkflowStatus.RUNNING);
		}
		
		//TODO make this do something
		
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
		
		System.out.println("notify " + getStatus());
	}

	
	public static void main(String[] args){
		UIMAWorkflow testUimaWorkflow = new UIMAWorkflow();
		
		try {
			testUimaWorkflow.runWorkflow();
		} catch (InvalidStatusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

