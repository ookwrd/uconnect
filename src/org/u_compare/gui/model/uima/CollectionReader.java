package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.u_compare.gui.model.AbstractComponent;

public class CollectionReader extends AbstractComponent {

	public CollectionReader(CollectionReaderDescription desc){
		ProcessingResourceMetaData metaData = desc.getCollectionReaderMetaData();
		extractFromProcessingResouceMetaData(metaData);
		
		setImplementationName(desc.getImplementationName());//TODO refactor these up somewhere?
		resourceManagerConfiguration =
			desc.getResourceManagerConfiguration();
	}
	
	@Override
	public ResourceCreationSpecifier getUIMADescription(){
		CollectionReaderDescription description = UIMAFramework.getResourceSpecifierFactory().createCollectionReaderDescription();
		ProcessingResourceMetaData metaData = description.getCollectionReaderMetaData();
		
		setupProcessingResourceMetaData(metaData);
		setupAnalysisEngineDescription(description);
		
		return description;
	}
	
}
