package org.u_compare.gui.model.uima;

import java.util.Map.Entry;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.InvalidXMLException;
import org.u_compare.gui.model.AbstractAggregateComponent;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;

public class AggregateAnalysisEngine extends AbstractAggregateComponent {

	public AggregateAnalysisEngine(AnalysisEngineDescription desc){
		extractFromProcessingResouceMetaData(
				desc.getAnalysisEngineMetaData());
		
		setImplementationName(desc.getImplementationName());
		flowController = desc.getFlowControllerDeclaration();
		
		try {
			for(Entry<String, ResourceSpecifier> pair : desc.getDelegateAnalysisEngineSpecifiers().entrySet()){
				Component subComponent = AbstractComponent.constructComponentFromXML(pair.getValue());
				subComponent.setFlowControllerIdentifier(pair.getKey());
				addSubComponent(subComponent);
			}
		} catch (InvalidXMLException e) {
			e.printStackTrace();
		}
		
		resourceManagerConfiguration =
			desc.getResourceManagerConfiguration();
	}
	
}
