package org.u_compare.gui.model.uima.debugging;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class BasicAE1 extends JCasAnnotator_ImplBase {

	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		
		String[] names = aContext.getConfigParameterNames();
		for(String name : names){
			if(aContext.getConfigParameterValue(name) instanceof Object[]){
				Object[] objects = (Object[])aContext.getConfigParameterValue(name);
				System.out.println("Parameter " + name + " has value: ");
				for(Object object : objects){
					System.out.println(object);
				}
			}else{
				System.out.println("Parameter " + name + " has value: " +aContext.getConfigParameterValue(name));
			}
		}
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		UIMAComponentTester.flags[1] = true;
		System.out.println("Processing One");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
