package org.evolutionarylinguistics.uima.logic;

import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.evolutionarylinguistics.uima.TruthAnnotation;
/**
 * 
 * Use an aggregate fixed flow component to handle scoping.
 * 
 * @author lukemccrohon1
 *
 */
public class Not extends JCasAnnotator_ImplBase {
	
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
	}
	
	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		
		AnnotationIndex annotations = aJCas.getAnnotationIndex(TruthAnnotation.type);
		
		Iterator iter = annotations.iterator();
		TruthAnnotation ann = (TruthAnnotation)iter.next();
		
		//Update existing value version
		Feature feature = aJCas.getTypeSystem().getFeatureByFullName("org.evolutionarylinguistics.uima.TruthAnnotation:value");
		boolean value = ann.getBooleanValue(feature);
		
		ann.setBooleanValue(feature, !value);
		
		//Remove and Add new version
		/*Feature feature = aJCas.getTypeSystem().getFeatureByFullName("org.evolutionarylinguistics.uima.TruthAnnotation:value");
		boolean value = ann.getBooleanValue(feature);
		
		ann.removeFromIndexes();
		
		TruthAnnotation an = new TruthAnnotation(aJCas, 0, 1); //TODO how do document level annotations work?
		an.setValue(!value);
		an.addToIndexes();*/
		
	}

}
