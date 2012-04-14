package org.u_compare.gui.model;

/**
 * Model class representing either a AnnotiationType or Feature. This is ugly,
 * but it is needed to match the similar schizophrenic class in the UIMA
 * framework.
 * 
 * @author Luke McCrohon
 * 
 */
public class AnnotationTypeOrFeature {

	private String typeName;
	private boolean isType = true;
	private boolean isAllAnnotatorFeatures = true;

	public AnnotationTypeOrFeature(String typeName) {
		this.setTypeName(typeName);
	}

	private void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override 
	public String toString(){
		return getDisplayName();
	}
	
	public String getDisplayName() {
		if (typeName.contains(".")) {
			return typeName.substring(typeName.lastIndexOf('.') + 1);
		} else {
			return typeName;
		}
	}

	public String getTypeName() {
		return typeName;
	}

	public boolean isType() {
		return isType;
	}

	public void setType(boolean isType) {
		this.isType = isType;
	}

	public boolean isAllAnnotatorFeatures() {
		return isAllAnnotatorFeatures;
	}

	public void setAllAnnotatorFeatures(boolean isAllAnnotatorFeatures) {
		this.isAllAnnotatorFeatures = isAllAnnotatorFeatures;
	}

	/**
	 * Type names function as unique identifiers. If two AnnotationTypes have
	 * the same name, then they are equal.
	 */
	@Override
	public boolean equals(Object x) {

		if (x instanceof AnnotationTypeOrFeature) {
			if (((AnnotationTypeOrFeature) x).getTypeName().equals(typeName)) {
				return true;
			}
		}
		return false;
	}

	// TODO factory
}
