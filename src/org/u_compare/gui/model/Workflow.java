package org.u_compare.gui.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLizable;
import org.u_compare.gui.model.uima.CPE;

/**
 * Model class representing a UIMA Workflow. The workflow is modelled as a
 * special UIMAAggregateComponent.
 * 
 * @author Luke McCrohon
 *
 */
public class Workflow extends AbstractAggregateComponent implements StatusCallbackListener {
	
	public enum WorkflowStatus {READY, LOADING, INITIALIZING, RUNNING, ERROR,
		PAUSED, FINISHED};
	
	public interface WorkflowStatusListener {
		public void workflowStatusChanged(Workflow workflow);	
	}	
	
	public interface WorkflowMessageListener{
		public void workflowMessageSent(Workflow workflow, String message);
	}
	
	private WorkflowStatus status = WorkflowStatus.READY;
	private ArrayList<WorkflowStatusListener> workflowStatusListeners = new ArrayList<WorkflowStatusListener>();
	private ArrayList<WorkflowMessageListener> workflowMessageListeners = new ArrayList<WorkflowMessageListener>();
	
	/**
	 * Creates an empty workflow.
	 */
	public Workflow(){
		super();
	}
	
	
	/**
	 * Creates a workflow from the specified list of components.
	 * 
	 * @param components
	 */
	public Workflow(ArrayList<Component> components){
		super();
		setSubComponents(components);
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
	public void runWorkflow() {
		assert(status == WorkflowStatus.READY || status == WorkflowStatus.FINISHED);//TODO

		notifyWorkflowMessageListeners("Play button hit.");
		
		setStatus(WorkflowStatus.INITIALIZING);
		
		/*//TODO
		
		setStatus(WorkflowStatus.RUNNING);
		
		//TODO
		
		
		TimerTask task = new TimerTask() {
			
			private int x = 5;
			
			@Override
			public void run() {
				x--;
				notifyWorkflowStatusListeners();
				if(x < 1) {
					timer.cancel();
					afterRunning();
				}
			}
		};
		
		timer.schedule(task, 0, 1000);
		
		*/
	}
	
	private void afterRunning(){

		setStatus(WorkflowStatus.READY);
		
	}
	
	
	/**
	 * used to pause the workflow processing
	 * 
	 * @throws InvalidStatusException
	 */
	public void pauseWorkflow(){
		assert(status == WorkflowStatus.RUNNING);
		
		//TODO make this do something
		//TODO actually call this.
		
	}
	
	/**
	 * should be used to stop workflow processing
	 */
	public void stopWorkflow() {
		assert(status == WorkflowStatus.RUNNING || status == WorkflowStatus.PAUSED);
		
		notifyWorkflowMessageListeners("Stopped??!?!?");
		
		
		//TODO make this do something
		
	}
	
	/**
	 * Status should always be changed via this method.
	 * 
	 * @param newStatus
	 */
	protected void setStatus(WorkflowStatus newStatus){
		if(status == newStatus){return;}
		
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
		this.workflowStatusListeners.add(listener);
	}
	
	/**
	 * Should be called whenever the status of workflow processing is changed.
	 */
	protected void notifyWorkflowStatusListeners(){
		for(WorkflowStatusListener listener : workflowStatusListeners){
			listener.workflowStatusChanged(this);
		}
	}
	
	public void registerWorkflowMessageListener(WorkflowMessageListener listener){
		this.workflowMessageListeners.add(listener);
	}
	
	protected void notifyWorkflowMessageListeners(String message){
		for(WorkflowMessageListener listener : workflowMessageListeners){
			listener.workflowMessageSent(this, message);
		}
	}
	
	
	public static Workflow constructWorkflowFromXML(String location){
		try {
			return constructWorkflowFromXML(new XMLInputSource(location));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static Workflow constructWorkflowFromXML(XMLInputSource inputSource){
		
		try {
			XMLizable desc = UIMAFramework.getXMLParser().parse(inputSource);
		
			if(desc instanceof CpeDescription){
				return new CPE((CpeDescription)desc);
			} else {
				//TODO error //TODO AS workflow
				return null;
			}
			
		} catch (InvalidXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (CpeDescriptorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	@Override
	public void aborted() {
		//TODO console message
		
		notifyWorkflowMessageListeners("Workflow processing aborted");
		setStatus(WorkflowStatus.FINISHED);
		
		System.out.println("aborted");
	}


	@Override
	public void batchProcessComplete() {
		System.out.println("batchProcessComplete");
		

		setStatus(WorkflowStatus.FINISHED);
		
	}


	@Override
	public void collectionProcessComplete() {
		// TODO Auto-generated method stub
		System.out.println("CollectionProcessComplete");

		setStatus(WorkflowStatus.FINISHED);
	}


	@Override
	public void initializationComplete() {

		setStatus(WorkflowStatus.RUNNING);
	}


	@Override
	public void paused() {
		// TODO Auto-generated method stub
		System.out.println("paused");
		

		setStatus(WorkflowStatus.PAUSED);
	}


	@Override
	public void resumed() {

		System.out.println("resumed");
		// TODO Auto-generated method stub
		
	}


	@Override
	public void entityProcessComplete(CAS arg0, EntityProcessStatus arg1) {
		// TODO Auto-generated method stub
		
		
		System.out.println("Entity");
		System.out.println(arg1.getStatusMessage());
		
	}
	
	protected void addStatusCallBackListeners() {
		//TODO
	}
	
}

