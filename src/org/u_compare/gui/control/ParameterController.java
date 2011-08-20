package org.u_compare.gui.control;

import javax.swing.JOptionPane;

import org.u_compare.gui.component.parameters.ParameterPanel;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public abstract class ParameterController {

	protected Parameter param;
	protected ParameterPanel view;
	
	public ParameterPanel getView(){
		return view;
	}
	
	/**
	 * Method for consistent handling of ConstraintFailedExceptions
	 * 
	 * @param e
	 */
	protected void processConstraintFailure(ConstraintFailedException e) {
		JOptionPane.showMessageDialog(null, e.getUserReadableError());	
	}
	
	public void setValue(String parameterValue){
		try{
			param.setValue(parameterValue);
		}catch(ConstraintFailedException ex) {
			processConstraintFailure(ex);
		}
	}
}
