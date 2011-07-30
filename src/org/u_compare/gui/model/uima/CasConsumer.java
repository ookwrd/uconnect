package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CasConsumerDescription;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.u_compare.gui.model.AbstractComponent;

public class CasConsumer extends AbstractComponent {

	public CasConsumer(CasConsumerDescription desc){
		ProcessingResourceMetaData metaData = desc.getCasConsumerMetaData();
		extractFromProcessingResouceMetaData(metaData);
		
		extractFromSpecifier(desc);
	}
	
	@Override
	public ResourceCreationSpecifier getResourceCreationSpecifier(){
		CasConsumerDescription description = UIMAFramework.getResourceSpecifierFactory().createCasConsumerDescription();
	
		setupProcessingResourceMetaData(description.getCasConsumerMetaData());
		setupResourceCreationSpecifier(description);
		
		return description;
	}
	
}
