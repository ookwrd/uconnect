package org.u_compare.gui.model.uima;

import java.io.IOException;

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
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.InvalidStatusException;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.uima.debugging.RunTester;
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
	public void runWorkflow() throws InvalidStatusException {
		super.runWorkflow();
		System.out.println("Running in CPE");
		

		//CpeDescription cpeDesc = getResourceCPEDescription();

		try {
			

			System.out.println("Before:" + UIMAComponentTester.flags[0]+ " " + UIMAComponentTester.flags[1]);
			
		//	
			CpeDescription cpeDesc;
			try {
				cpeDesc = UIMAFramework.getXMLParser().parseCpeDescription(new XMLInputSource("src/org/u_compare/gui/model/uima/debugging/basicCPE.xml"));
			
				CollectionProcessingEngine mCPE = UIMAFramework.produceCollectionProcessingEngine(cpeDesc);

				//mCPE.addStatusCallbackListener(new StatusCallbackListenerImpl());
				
				mCPE.process();
			} catch (InvalidXMLException e) {
				e.printStackTrace();
			} catch (IOException e) {

				System.out.println("here "+e.getMessage() + "\n" + e.getCause());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			System.out.println("done");
			
			System.out.println("After:" + UIMAComponentTester.flags[0]+ " " + UIMAComponentTester.flags[1]);
		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block

			System.out.println("here "+e.getMessage() + "\n" + e.getCause().getMessage());
			
			e.printStackTrace();
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
	
}
