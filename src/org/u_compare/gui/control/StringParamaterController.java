package org.u_compare.gui.control;

import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.StringParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.StringParameter;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParamaterController extends ParameterController {

	private StringParameter param;
//	private ComponentController parent;
	private StringParameterPanel view;
	private Component component;
	
	public StringParamaterController(ComponentController control,
			StringParameter param, Component component) {
//		this.parent = control;
		this.param = param;
		this.component= component;
		this.view = new StringParameterPanel(param, this, component);
	}

	public ParameterPanel getView() {//TODO who calls this?
		return view;
	}


	public void setValue(String parameterValue){
		
		assert(!component.getLockedStatus());
		
		try{
			param.setValue(parameterValue);
		}catch(ConstraintFailedException e){
			processConstraintFailure(e);
		}
		
	}
	
}
