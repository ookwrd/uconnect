package org.u_compare.gui.model.uima.debugging;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.evolutionarylinguistics.uima.TruthAnnotation;

public class BasicAE extends JCasAnnotator_ImplBase {
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		TruthAnnotation ann = new TruthAnnotation(aJCas, 0, 1); //TODO how do document level annotations work?
		ann.setValue(true);
		ann.addToIndexes();
	}

}