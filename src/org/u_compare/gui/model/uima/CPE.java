package org.u_compare.gui.model.uima;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.util.XMLizable;
import org.u_compare.gui.control.WorkflowViewerController.WorkflowFactory;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.xml.sax.SAXException;

public class CPE extends Workflow implements StatusCallbackListener {

	public static WorkflowFactory emptyCPEFactory = new WorkflowFactory() {
		@Override
		public Workflow constructWorkflow() {
			return Workflow.constructWorkflowFromXML("src/org/u_compare/gui/model/uima/descriptors/emptyCPE.xml");
		}
	};
	
	private CpeCollectionReader[] collectionReaders;
	private CpeCasProcessors cpeCasProcessors;
	private CpeConfiguration cpeConfiguration;
	
	private String sourceFileName = "CPE Workflow";
	
	private CollectionProcessingEngine mCPE;
	private boolean paused;
	
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
				CPE.super.runResumeWorkflow();
				
				CpeDescription cpeDesc = (CpeDescription)getWorkflowDescription(); 
				notifyWorkflowMessageListeners("Workflow Descriptor initialzed.");
				
				setStatus(Workflow.WorkflowStatus.LOADING);
				mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);
				mCPE.addStatusCallbackListener(CPE.this);
				notifyWorkflowMessageListeners("Workflow loaded Successfully.");	
				
				mCPE.process();//Runs on a seperate thread.
			} catch (ResourceInitializationException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public void runResumeWorkflow() {
		Runnable runWorkflow  = new WorkflowInitializer();
	
		if(mCPE == null || !paused){
			Thread newTread = new Thread(runWorkflow);
			newTread.start();
			paused = false;
		} else {
			mCPE.resume();
			resumeWorkflow();
			paused = false;
		}
	}
	
	@Override
	public void stopWorkflow() {
		mCPE.removeStatusCallbackListener(this);//DOesnt work...
		mCPE.stop();
		mCPE = null;
		
		notifyWorkflowMessageListeners("Workflow processing aborted");
		
		paused = false;
		super.stopWorkflow();
	}
	
	@Override
	public void pauseWorkflow(){
		mCPE.pause();
		paused = true;
		super.pauseWorkflow();
	}
	
	@Override
	public MetaDataObject getWorkflowDescription(){
		CpeDescription retVal = UIMAFramework.getResourceSpecifierFactory().createCpeDescription();
		
		try {
			retVal.setAllCollectionCollectionReaders(collectionReaders);
			
			if(getSubComponents().get(0) instanceof CollectionReader){
				CollectionReader comp = (CollectionReader)getSubComponents().get(0);
				retVal.setAllCollectionCollectionReaders(new CpeCollectionReader[]{ constructCpeCollectionReader(comp)});
			}
			
			//Cas processors
			cpeCasProcessors.removeAllCpeCasProcessors();
			for(int i = 0; i < getSubComponents().size(); i++){
				Component comp = getSubComponents().get(i);
				if(comp instanceof CollectionReader){
					continue;
				}
				cpeCasProcessors.addCpeCasProcessor(constructCpeCasProcessor(comp));
				
				//TODO cleanup temp files
			}
			
		} catch (CpeDescriptorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		retVal.setCpeCasProcessors(cpeCasProcessors);
		retVal.setCpeConfiguration(cpeConfiguration);
		
		return retVal;
	}
	
	private CpeCasProcessor constructCpeCasProcessor(Component comp) throws CpeDescriptorException{
		CpeCasProcessor processor = CpeDescriptorFactory.produceCasProcessor(comp.getName());
		String saved = toFile(comp.getResourceCreationSpecifier());
	
		CpeComponentDescriptor desc = CpeDescriptorFactory.produceComponentDescriptor(saved);
		//Why can I only build it from the file system??? I have my specifiers in memory!
		processor.setCpeComponentDescriptor(desc);
		processor.setBatchSize(10000);
	    processor.getErrorHandling().getErrorRateThreshold().setMaxErrorCount(0);

		return processor;
	}
	
	private CpeCollectionReader constructCpeCollectionReader(CollectionReader comp) throws CpeDescriptorException{
		String saved = toFile(comp.getResourceCreationSpecifier());
		CpeCollectionReader reader = CpeDescriptorFactory.produceCollectionReader(saved);
		return reader;
	}
	
	private String toFile(XMLizable xml){
		try {
			final File file = File.createTempFile("UConnect-temp", ".xml");
			file.deleteOnExit();
			FileWriter writer = new FileWriter(file);
			xml.toXML(writer);
			writer.close();
			return file.getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * CPE Workflows don't have a name field so use the description field.
	 * 
	 */
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
		setStatus(WorkflowStatus.FINISHED);
		mCPE = null;
	}


	@Override
	public void initializationComplete() {
		setStatus(WorkflowStatus.RUNNING);
	}

	@Override
	public void entityProcessComplete(CAS arg0, EntityProcessStatus arg1) {
		//TODO don't report if mCPE is set to null as removeStatusCallbackListener doesn't work
		notifyWorkflowMessageListeners("Entity processing complete with status: " + arg1.getStatusMessage());
	}
	
}
