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
	
	//TODO equality
	//TODO factory
}
