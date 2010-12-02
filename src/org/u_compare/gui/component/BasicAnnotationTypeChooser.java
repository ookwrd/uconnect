package org.u_compare.gui.component;

import javax.swing.JOptionPane;

import org.u_compare.gui.model.AnnotationType;

/**
 * Basic AnnotationTypeChooser for testing purposes. Should be replaced shortly.
 * 
 * @author lukemccrohon
 *
 */
public class BasicAnnotationTypeChooser implements AnnotationTypeChooser {

	@Override
	public AnnotationType getNewAnnotation() {
		
		String typeName = JOptionPane.showInputDialog("Please enter the type name to add:");
		
		return new AnnotationType(typeName);
	}

}
