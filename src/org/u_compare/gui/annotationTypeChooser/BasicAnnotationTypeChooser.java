package org.u_compare.gui.annotationTypeChooser;

import javax.swing.JOptionPane;

import org.u_compare.gui.model.AnnotationTypeOrFeature;

/**
 * Basic AnnotationTypeChooser for testing purposes. Should be replaced shortly.
 * 
 * @author Luke McCrohon
 *
 */
public class BasicAnnotationTypeChooser implements AnnotationTypeChooser {

	@Override
	public AnnotationTypeOrFeature getNewAnnotation() {
		
		String typeName = JOptionPane.showInputDialog("Please enter the type name to add:");
		
		return new AnnotationTypeOrFeature(typeName);
	}

}
