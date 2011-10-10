package org.u_compare.gui.model.uima;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.MetaDataObject;
import org.apache.uima.util.XMLInputSource;
import org.u_compare.gui.model.AbstractAggregateComponent;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;

public class AggregateAnalysisEngine extends AbstractAggregateComponent {

	public AggregateAnalysisEngine(AnalysisEngineDescription desc){
		extractFromProcessingResouceMetaData(
				desc.getAnalysisEngineMetaData());

		setImplementationName(desc.getImplementationName());
		flowController = desc.getFlowControllerDeclaration();

		Map<String,MetaDataObject> map = desc.getDelegateAnalysisEngineSpecifiersWithImports();
		for (String key : map.keySet()) {
			Import imp = (Import) map.get(key);
			Component subComponent = AbstractComponent.constructComponentFromXML(imp);
			subComponent.setFlowControllerIdentifier(key);
			addSubComponent(subComponent);
		}

		resourceManagerConfiguration =
			desc.getResourceManagerConfiguration();
	}

}
