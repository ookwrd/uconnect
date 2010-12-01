package org.evolutionarylinguistics.uima.testers;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.evolutionarylinguistics.uima.TestAnnotation;

public class TESTERBlack extends JCasAnnotator_ImplBase {

	private static String description = "Black";
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		
	System.out.println("Initializing " + description);
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		System.out.println("processing " + description);
		
		TestAnnotation ann = new TestAnnotation(aJCas, 1, 2); //TODO how do document level annotations work?
		ann.setLabel(description);
		ann.addToIndexes();
	}

}
