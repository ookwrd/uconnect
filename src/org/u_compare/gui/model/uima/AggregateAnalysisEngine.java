package org.u_compare.gui.model.uima;

import java.util.Map;
import java.util.Set;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.resource.metadata.Import;
import org.apache.uima.resource.metadata.MetaDataObject;
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
		
		Map<String, MetaDataObject> values = desc.getDelegateAnalysisEngineSpecifiersWithImports();
		for(MetaDataObject spec : values.values()){
			System.out.println("Class " + spec.getClass());
			
			Import imp = (Import)spec;
			System.out.println(imp.getName() + "  " + imp.getLocation());
			
			Component comp = AbstractComponent.constructComponentFromXML((ResourceSpecifier)spec);
			addSubComponent(comp);
		}
		
		resourceManagerConfiguration =
			desc.getResourceManagerConfiguration();
	}
	
}
