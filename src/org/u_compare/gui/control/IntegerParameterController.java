package org.u_compare.gui.control;

import org.u_compare.gui.component.IntegerParameterPanel;
import org.u_compare.gui.component.ParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class IntegerParameterController extends ParameterController {

	private IntegerParameter param;
	private ComponentController parent;
	private IntegerParameterPanel view;//They look the same
//	private Component component;
	
	public IntegerParameterController(ComponentController control,
			IntegerParameter param, Component component) {
		this.parent = control;
		this.param = param;
//		this.component = component;
		this.view = new IntegerParameterPanel(param, this, component);
	}

	public ParameterPanel getView() {
		return view;
	}
	
	public void setValue(String parameterValue){
		assert(!parent.isLocked());

		try{
			param.setValue(parameterValue);
		}catch(ConstraintFailedException ex) {
			processConstraintFailure(ex);
		}

	}
	
}
