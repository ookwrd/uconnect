package org.u_compare.gui.annotationTypeChooser;

import org.u_compare.gui.model.AnnotationTypeOrFeature;

/**
 * Common interface for functionality required of pluggable AnnotationTypeChooser Components.
 * 
 * @author Luke McCrohon
 *
 */
public interface AnnotationTypeChooser {

	public AnnotationTypeOrFeature getNewAnnotation();
	
	//TODO provide information on why a type is being choosen which can be shown to the user..
}
