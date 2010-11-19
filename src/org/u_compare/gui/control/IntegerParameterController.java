package org.u_compare.gui.control;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.security.InvalidParameterException;

import org.u_compare.gui.IntegerParameterPanel;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.StringParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.InvalidInputException;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class IntegerParameterController extends ParameterController {

	private IntegerParameter param;
	private ComponentController parent;
	private IntegerParameterPanel view;//They look the same
	private Component component;
	
	public IntegerParameterController(ComponentController control,
			IntegerParameter param, Component component) {
		this.parent = control;
		this.param = param;
		this.component = component;
		this.view = new IntegerParameterPanel(param, this, component);
	}

	public ParameterPanel getView() {
		return view;
	}
	
	public void setValue(String parameterValue){
		assert(!parent.isLocked());

		try{
			param.update(parameterValue);
		}catch(ConstraintFailedException ex) {
			processConstraintFailure(ex);
		}

	}
	
}
