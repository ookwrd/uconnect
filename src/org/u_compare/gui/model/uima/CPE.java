package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.metadata.CpeCasProcessors;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeConfiguration;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.u_compare.gui.model.InvalidStatusException;
import org.u_compare.gui.model.Workflow;

public class CPE extends Workflow {

	//private ArrayList<CollectionReader> reader = new ArrayList<CollectionReader>();
	
	private CpeCollectionReader[] collectionReaders;
	private CpeCasProcessors cpeCasProcessors;
	private CpeConfiguration cpeConfiguration;
	
	public CPE(CpeDescription desc) throws CpeDescriptorException{
		
		collectionReaders = desc.getAllCollectionCollectionReaders();
		cpeCasProcessors = desc.getCpeCasProcessors(); //<- this is where the subcomponents are
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
