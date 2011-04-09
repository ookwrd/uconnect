package org.u_compare.gui.annotationTypeChooser;

import org.u_compare.gui.model.AnnotationType;

/**
 * Common interface for functionality required of pluggable AnnotationTypeChooser Components.
 * 
 * @author Luke McCrohon
 *
 */
public interface AnnotationTypeChooser {

	public AnnotationType getNewAnnotation();
	
	//TODO provide information on why a type is being choosen which can be shown to the user..
}
