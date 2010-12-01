package org.evolutionarylinguistics.uima.logic;

import java.util.Iterator;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.cas.CAS;
import org.apache.uima.flow.CasFlowController_ImplBase;
import org.apache.uima.flow.CasFlow_ImplBase;
import org.apache.uima.flow.FinalStep;
import org.apache.uima.flow.Flow;
import org.apache.uima.flow.FlowControllerContext;
import org.apache.uima.flow.SimpleStep;
import org.apache.uima.flow.Step;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.evolutionarylinguistics.uima.TruthAnnotation;

public class If extends CasFlowController_ImplBase{

	public void initialize(FlowControllerContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
	}
	  
	@Override
	public Flow computeFlow(CAS aCAS) throws AnalysisEngineProcessException {
	    IfFlow flow = new IfFlow();
	    flow.setCas(aCAS);
	    return flow;
	}

	
	
	class IfFlow extends CasFlow_ImplBase{

		private boolean checked = false;
		private boolean done = false;
		
		public Step next() throws AnalysisEngineProcessException {
			
			if(done){
				return new FinalStep();
			}
			
			String[] orderStrings = ((FixedFlow) (getContext().getAggregateMetadata().getFlowConstraints())).getFixedFlow();
			//TODO make this safe, I dont know if I can assume a FixedFlow is added
			
			
			if(!checked){
				
				checked = true;
				return new SimpleStep(orderStrings[0]);
				
			}else{
				
				
				
				
				Iterator iter = getCas().getAnnotationIndex(getCas().getTypeSystem().getType("org.evolutionarylinguistics.uima.TruthAnnotation")).iterator();
				
				if(!iter.hasNext()){
					//ummm //TODO
				}
				
				TruthAnnotation truth = (TruthAnnotation)iter.next();
				
				boolean value = truth.getValue();
				
				if(value){
					done = true;
					return new SimpleStep(orderStrings[1]);
				}else{
					done = true;
					return new SimpleStep(orderStrings[2]);
				}
				
			}
		}
		
	}
}
