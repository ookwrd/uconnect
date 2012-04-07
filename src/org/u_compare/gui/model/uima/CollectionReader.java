package org.u_compare.gui.model.uima;

import org.apache.uima.UIMAFramework;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.resource.ResourceCreationSpecifier;
import org.apache.uima.resource.metadata.ProcessingResourceMetaData;
import org.u_compare.gui.model.AbstractComponent;

public class CollectionReader extends AbstractComponent {

	public CollectionReader(CollectionReaderDescription desc) {
		ProcessingResourceMetaData metaData = desc
				.getCollectionReaderMetaData();
		extractFromProcessingResouceMetaData(metaData);

		extractFromSpecifier(desc);
	}

	@Override
	public ResourceCreationSpecifier getResourceCreationSpecifier() {
		CollectionReaderDescription description = UIMAFramework
				.getResourceSpecifierFactory()
				.createCollectionReaderDescription();

		setupProcessingResourceMetaData(description
				.getCollectionReaderMetaData());
		setupResourceCreationSpecifier(description);

		return description;
	}

}
