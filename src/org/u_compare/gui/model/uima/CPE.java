package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.metadata.CpeCasProcessor;
import org.apache.uima.collection.metadata.CpeCasProcessors;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.util.InvalidXMLException;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.InvalidStatusException;
import org.u_compare.gui.model.Workflow;

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
