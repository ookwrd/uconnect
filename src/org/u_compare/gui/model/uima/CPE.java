package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.metadata.CpeCollectionReader;
import org.apache.uima.collection.metadata.CpeDescription;
import org.apache.uima.collection.metadata.CpeDescriptorException;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.u_compare.gui.model.Workflow;

public class CPE extends Workflow {

	//private ArrayList<CollectionReader> reader = new ArrayList<CollectionReader>();
	
	private CpeCollectionReader[] collectionReaders;
	
	public CPE(CpeDescription desc) throws CpeDescriptorException{
		
		collectionReaders = desc.getAllCollectionCollectionReaders();
		
		
	}
	
	public CpeDescription getResourceCPEDescription(){
		CpeDescription retVal = UIMAFramework.getResourceSpecifierFactory().createCpeDescription();
		
		return retVal;
	}
	
}
