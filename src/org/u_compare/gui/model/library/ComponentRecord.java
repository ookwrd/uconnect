package org.u_compare.gui.model.library;

/**
 * This will be the class representing an instance of a component in the library, based on an underlying UIMA xml descriptor
 * 
 * 
 * eventually merge this with UIMA component
 * 
 * @author luke mccrohon
 *
 */

public class ComponentRecord {

	String descriptor;
	
	
	
	public ComponentRecord(String descriptor){
		
		this.descriptor = descriptor;
		
	}
	
	public String getComponentDescriptor(){
		return descriptor;
	}
	
}
