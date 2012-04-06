package org.u_compare.gui.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLizable;
import org.u_compare.gui.model.uima.CPE;
import org.xml.sax.SAXException;

/**
 * Model class representing a UIMA Workflow. The workflow is modelled as a
 * special UIMAAggregateComponent.
 * 
 * @author Luke McCrohon
 *
 */
public class Workflow extends AbstractAggregateComponent  {
	
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
	public void runResumeWorkflow() {
		assert(status == WorkflowStatus.READY || status == WorkflowStatus.FINISHED || status == WorkflowStatus.ERROR);

		setStatus(WorkflowStatus.INITIALIZING);
	}
	
	/**
	 * used to pause the workflow processing
	 * 
	 * @throws InvalidStatusException
	 */
	public void pauseWorkflow(){
		assert(status == WorkflowStatus.RUNNING);
		setStatus(WorkflowStatus.PAUSED);
	}
	
	public void resumeWorkflow(){
		assert(status == WorkflowStatus.PAUSED);
		setStatus(WorkflowStatus.RUNNING);
	}
	
	/**
	 * should be used to stop workflow processing
	 */
	public void stopWorkflow() {
		assert(status == WorkflowStatus.RUNNING || status == WorkflowStatus.PAUSED);
		setStatus(WorkflowStatus.FINISHED);
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
	
	public MetaDataObject getWorkflowDescription(){
		System.out.println("Abstract Workflow unable to produce description object.");
		return null;
	}
	
	protected String toFile(XMLizable xml){
		try {
			final File file = File.createTempFile("UConnect-temp", ".xml");
			file.deleteOnExit();
			FileWriter writer = new FileWriter(file);
			xml.toXML(writer);
			writer.close();
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		assert(false);
		return null;
	}
	
	public static Workflow constructWorkflowFromXML(String location){
		try {
			return constructWorkflowFromXML(new XMLInputSource(location));
		} catch (IOException e) {
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
				
				System.err.println("Workflow.constructWorkflowFromXML(): Error encountered, should be constructing an AS Workflow, but this functionality has not been implemented." + desc.getClass());
				//TODO error //TODO AS workflow
				return null;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}

