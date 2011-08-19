package org.u_compare.gui.model.uima.debugging;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class BasicAE extends JCasAnnotator_ImplBase {
	
	private String parameterString = "Unset";
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		
		String[] names = aContext.getConfigParameterNames();
		for(String name : names){
			if(name.equals("Param1")){
				parameterString = (String) aContext.getConfigParameterValue("Param1");
			}
		}
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		UIMAComponentTester.flags[0] = true;
		System.out.println("Processing Zero (parameter: " + parameterString + ")");
		
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
