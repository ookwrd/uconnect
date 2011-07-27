package org.u_compare.gui.model.uima;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.u_compare.gui.model.AbstractAggregateComponent;

public class AggregateAnalysisEngine extends AbstractAggregateComponent {

	public AggregateAnalysisEngine(AnalysisEngineDescription desc){
		extractFromProcessingResouceMetaData(
				desc.getAnalysisEngineMetaData());
		
		setImplementationName(desc.getImplementationName());
		resourceManagerConfiguration =
			desc.getResourceManagerConfiguration();
	}
	
}
