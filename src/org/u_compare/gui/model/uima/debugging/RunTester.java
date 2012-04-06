package org.u_compare.gui.model.uima.debugging;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.u_compare.gui.model.AbstractComponent;
import org.u_compare.gui.model.Component;

public class RunTester {

	public RunTester(String location) throws ResourceInitializationException{
		//TODO factor this into a unittest
		//TODO test the creation of an aggregate component from scratch
		
		Component component = AbstractComponent.constructComponentFromXML(location);
		
		AnalysisEngine ae = 
		    UIMAFramework.produceAnalysisEngine(component.getResourceCreationSpecifier());

		JCas jcas = ae.newJCas();
		  
		  //analyze a document
		jcas.setDocumentText("This is a very very simple document");
		
		try {
			System.out.println("Trying " + UIMAComponentTester.flags[0] + " " + UIMAComponentTester.flags[1]);
			ae.process(jcas);
			System.out.println("Done " + UIMAComponentTester.flags[0] +  " " + UIMAComponentTester.flags[1]);
			
		} catch (AnalysisEngineProcessException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args){
		try {
			new RunTester("src/org/u_compare/gui/model/uima/debugging/AggregateAEWithChildren.xml");
		} catch (ResourceInitializationException e) {
			e.printStackTrace();
		}
	}
}
