package org.u_compare.gui.control;

import javax.swing.JOptionPane;

import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public abstract class ParameterController {

	public abstract ParameterPanel getView();
	
	/**
	 * Method for consistent handling of ConstraintFailedExceptions
	 * 
	 * @param e
	 */
	protected void processConstraintFailure(ConstraintFailedException e) {
		
		JOptionPane.showMessageDialog(null, e.getUserReadableError());
		
	}
	
}
