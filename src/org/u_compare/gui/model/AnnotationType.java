package org.u_compare.gui.model;

public class AnnotationType {

	private String typeName;
	
	public AnnotationType(String typeName){
		this.setTypeName(typeName);
	}

	private void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeName() {
		return typeName;
	}
	
	/**
	 * Type names function as unique identifiers. If two AnnotationTypes have the same
	 * name, then they are equal.
	 * 
	 */
	@Override
	public boolean equals(Object x){
		
		if(x instanceof AnnotationType){
			if(((AnnotationType) x).getTypeName().equals(typeName)){
				return true;
			}
		}
		return false;
	}
	
	//TODO factory
}
