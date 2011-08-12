package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionProcessingEngine;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCasProcessors;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.Import;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.Workflow.WorkflowStatus;
import org.u_compare.gui.model.uima.debugging.UIMAComponentTester;

public class CPE extends Workflow {

	//private ArrayList<CollectionReader> reader = new ArrayList<CollectionReader>();
	
	private CpeCollectionReader[] collectionReaders;
	private CpeCasProcessors cpeCasProcessors;
	private CpeConfiguration cpeConfiguration;
	
	public CPE(CpeDescription desc) throws CpeDescriptorException{
		
		String path = desc.getSourceUrlString();
		String pathBase = path.substring(0, path.lastIndexOf("/")+1);
		
		collectionReaders = desc.getAllCollectionCollectionReaders();
		for(CpeCollectionReader reader : collectionReaders){
			Import imp = reader.getDescriptor().getImport();
			Component comp = AbstractComponent.constructComponentFromXML(pathBase+imp.getLocation());
			super.addSubComponent(comp);//TODO make this special
		}
		cpeCasProcessors = desc.getCpeCasProcessors(); //<- this is where the subcomponents are
		for(CpeCasProcessor processor : cpeCasProcessors.getAllCpeCasProcessors()){
			Import imp = processor.getCpeComponentDescriptor().getImport();//TODO load these into memory
			
			//System.out.println(path.substring(0, path.lastIndexOf("/")+1));
			Component comp = AbstractComponent.constructComponentFromXML(pathBase+imp.getLocation());
			super.addSubComponent(comp);
		}
		cpeConfiguration = desc.getCpeConfiguration();
		
	}
	
	@Override
	public void runWorkflow() {

		Runnable runWorkflow  = new WorkflowInitializer();
	
		Thread newTread = new Thread(runWorkflow);
		newTread.start();
		
		System.out.println("afterwards");
	}
	
	private class WorkflowInitializer implements Runnable {
		@Override
		public void run() {
			
			try {
				CPE.super.runWorkflow();
				
				setStatus(WorkflowStatus.INITIALIZING);
				CpeDescription cpeDesc = getResourceCPEDescription(); 
				notifyWorkflowMessageListeners("Workflow Descriptor initialzed.");

				setStatus(Workflow.WorkflowStatus.LOADING);
				CollectionProcessingEngine mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);
				mCPE.addStatusCallbackListener(CPE.this);
				notifyWorkflowMessageListeners("Workflow loaded Successfully.");	
				
				mCPE.process();//Runs on a seperate thread.

				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mCPE.pause();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mCPE.resume();
				
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
			}
			
		}
	}
	
	public CpeDescription getResourceCPEDescription(){
		CpeDescription retVal = UIMAFramework.getResourceSpecifierFactory().createCpeDescription();
		
		try {
			retVal.setAllCollectionCollectionReaders(collectionReaders);
		} catch (CpeDescriptorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		retVal.setCpeCasProcessors(cpeCasProcessors);
		retVal.setCpeConfiguration(cpeConfiguration);
		
		return retVal;
	}
	
	public static void main(String[] args){
		System.out.println("test");
	}
	
}
