package org.u_compare.gui.control;

import javax.swing.JOptionPane;

import org.u_compare.gui.component.parameters.BooleanParameterPanel;
import org.u_compare.gui.component.parameters.ParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.FloatParameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.StringParameter;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class ParameterController {

	protected Parameter param;
	protected ParameterPanel view;
	
	public ParameterController(ComponentController control,
			Parameter param, Component component) {
		
		if(param instanceof BooleanParameter){
			this.view = new BooleanParameterPanel(param, this, component);
		}else if (param instanceof StringParameter 
				|| param instanceof IntegerParameter
				|| param instanceof FloatParameter
				){
			this.view = new ParameterPanel(param, this, component);
		}else{
			assert(false);
		}
		
		this.param = param;
	}
	
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
