package org.u_compare.gui.model.uima;

import java.util.ArrayList;

import org.apache.uima.UIMAFramework;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.collection.StatusCallbackListener;
import org.apache.uima.collection.impl.metadata.cpe.CpeDescriptorFactory;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCasProcessors;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeComponentDescriptor;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.Import;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.uima.debugging.UIMAComponentTester;

public class CPE extends Workflow implements StatusCallbackListener {

	private CpeCollectionReader[] collectionReaders;
	private CpeCasProcessors cpeCasProcessors;
	private CpeConfiguration cpeConfiguration;
	
	private String sourceFileName = "CPE Workflow";
	
	public CPE(CpeDescription desc) throws CpeDescriptorException{
		
		String path = desc.getSourceUrlString();
		String pathBase = path.substring(0, path.lastIndexOf("/")+1);
		
		collectionReaders = desc.getAllCollectionCollectionReaders();
		for(CpeCollectionReader reader : collectionReaders){
			Import imp = reader.getDescriptor().getImport();
			Component comp = AbstractComponent.constructComponentFromXML(pathBase+imp.getLocation());
			super.addSubComponent(comp);
		}
		cpeCasProcessors = desc.getCpeCasProcessors(); //<- this is where the subcomponents are
		for(CpeCasProcessor processor : cpeCasProcessors.getAllCpeCasProcessors()){
			Import imp = processor.getCpeComponentDescriptor().getImport();//TODO load these into memory
			Component comp = AbstractComponent.constructComponentFromXML(pathBase+imp.getLocation());
			super.addSubComponent(comp);
		}
		cpeConfiguration = desc.getCpeConfiguration();
		
		if(desc.getSourceUrlString()!=null){
			String urlString = desc.getSourceUrlString();
			sourceFileName = urlString.substring(urlString.lastIndexOf("/")+1,urlString.length()-4);
		}
	}
	
	@Override
	public void runWorkflow() {

		Runnable runWorkflow  = new WorkflowInitializer();
	
		Thread newTread = new Thread(runWorkflow);
		newTread.start();
		
		System.out.println("afterwards");
	}
	
	/**
	 * Overridden to ensure the first component is a collection reader.
	 */
	@Override
	public boolean canAddSubComponent(Component component, int position){
		
		//Can't add a collection reader except in first place
		if(component instanceof CollectionReader && position > 0){
			return false;
		}
		
		//Can't add something in front of a collection reader
		if(position == 0){
			ArrayList<Component> subComponents = getSubComponents();
			if(subComponents.size() > 0 && subComponents.get(0) instanceof CollectionReader){
				return false;
			}
		}
		
		return super.canAddSubComponent(component, position);
	}
	
	/**
	 * Overridden to ensure the first component is a collection reader.
	 */
	@Override
	public boolean canReorderSubComponent(Component component, int position){
		
		//Can't move a collection reader except in first place
		if(component instanceof CollectionReader && position > 0){
			return false;
		}
		
		//Can't move something in front of a collection reader
		if(position == 0){
			ArrayList<Component> subComponents = getSubComponents();
			if(!(component instanceof CollectionReader) && subComponents.get(0) instanceof CollectionReader){
				return false;
			}
		}
		
		return super.canReorderSubComponent(component, position);
	}
	
	
	private class WorkflowInitializer implements Runnable {
		@Override
		public void run() {
			
			try {
				CPE.super.runWorkflow();
				
				setStatus(WorkflowStatus.INITIALIZING);
				CpeDescription cpeDesc = getResourceCPEDescription(); 
				//CpeConfiguration cpeConfiguration = cpeDesc.getCpeConfiguration();
				//cpeConfiguration.setDeployment("interactive");
				notifyWorkflowMessageListeners("Workflow Descriptor initialzed.");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				setStatus(Workflow.WorkflowStatus.LOADING);
				CollectionProcessingEngine mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);
				mCPE.addStatusCallbackListener(CPE.this);
				notifyWorkflowMessageListeners("Workflow loaded Successfully.");	
				
				mCPE.process();//Runs on a seperate thread.

				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mCPE.pause();
				
				notifyWorkflowMessageListeners("It should be paused...");
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mCPE.resume();
				

				notifyWorkflowMessageListeners("It should have resumed...");
				
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mCPE.stop();
				
				System.out.println("After:" + UIMAComponentTester.flags[0]+ " " + UIMAComponentTester.flags[1]);
			} catch (ResourceInitializationException e) {
				// TODO Auto-generated catch block

				System.out.println("here "+e.getMessage() + "\n" + e.getCause().getMessage());
				
				e.printStackTrace();
			}/* catch (CpeDescriptorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
		}
	}
	
	public CpeDescription getResourceCPEDescription(){
		CpeDescription retVal = UIMAFramework.getResourceSpecifierFactory().createCpeDescription();
		
		try {
			retVal.setAllCollectionCollectionReaders(collectionReaders);
			
			cpeCasProcessors.removeAllCpeCasProcessors();
			
			for(Component comp : getSubComponents()){
				if(comp instanceof CollectionReader){
					break; //TODO
				}
				
				CpeCasProcessor processor = CpeDescriptorFactory.produceCasProcessor(comp.getName());
				
				//TODO investifate CpmPanel
				
			//	CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(null);//TODO
				//I can only get it from the file system??? I have mine in memory!
				//processor.setCpeComponentDescriptor(desc);
				processor.setBatchSize(10000);
			    processor.getErrorHandling().getErrorRateThreshold().setMaxErrorCount(0);

				//cpeCasProcessors.addCpeCasProcessor(arg0);
				
			}
		} catch (CpeDescriptorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		retVal.setCpeCasProcessors(cpeCasProcessors);
		
		
		retVal.setCpeConfiguration(cpeConfiguration);
		
		return retVal;
	}
	
	@Override
	public String getName(){
		return sourceFileName;
	}
	
	@Override
	public String getDescription(){
		return "This is a CPE workflow and so certain functionality may be unavailable. It is highly reccomended that users make use of UIMA AS workflows when possible.";
	}
	
	/**
	 * CPE workflow's don't have editable titles or descriptions.
	 */
	/*@Override
	public boolean getLockedStatus(){//TODO a more general way of doing this.... DAMN this doesnt work
		return true;
	}*/
	
	protected void addStatusCallBackListeners() {
		//TODO incase other people want to add their own
	}
	

	@Override
	public void aborted() {
		//TODO console message
		
		notifyWorkflowMessageListeners("Workflow processing aborted");
		setStatus(WorkflowStatus.FINISHED);
		
		System.out.println("aborted");
	}
	
	/**
	 * paused and resumed are never called. I checked the UIMA CPM panel and RunAE and these don't
	 * seem to be called either. I think this may be a bug in UIMA. TODO Look this up.
	 */
	@Override
	public void paused() {
		setStatus(WorkflowStatus.PAUSED);
		System.out.println("paused");
	}
	@Override
	public void resumed() {
		setStatus(WorkflowStatus.RUNNING);
		System.out.println("resumed");	
	}
	

	@Override
	public void batchProcessComplete() {
		System.out.println("batchProcessComplete");
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
	public void entityProcessComplete(CAS arg0, EntityProcessStatus arg1) {
		// TODO Auto-generated method stub
		
		notifyWorkflowMessageListeners("Entity processing complete with status: " + arg1.getStatusMessage());
		
		System.out.println("Entity");
		System.out.println(arg1.getStatusMessage());
		
	}
	
}
