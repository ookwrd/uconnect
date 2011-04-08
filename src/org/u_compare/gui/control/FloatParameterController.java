package org.u_compare.gui.control;

import org.u_compare.gui.component.FloatParameterPanel;
import org.u_compare.gui.component.ParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.FloatParameter;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;


//TODO integrate this with the IntegerParameterController
public class FloatParameterController extends ParameterController {

	private FloatParameter param;
	private ComponentController parent;
	private FloatParameterPanel view;//They look the same
//	private Component component;
	
	public FloatParameterController(ComponentController control,
			FloatParameter param, Component component) {
		this.parent = control;
		this.param = param;
//		this.component = component;
		this.view = new FloatParameterPanel(param, this, component);
	}

	//TODO push into the abstract base class
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
