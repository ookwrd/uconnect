package org.u_compare.gui.control;

import org.u_compare.gui.component.BooleanParameterPanel;
import org.u_compare.gui.component.ParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;


public class BooleanParameterController extends ParameterController {

	private BooleanParameter param;
//	private ComponentController parent;
	private BooleanParameterPanel view;
	private Component component;
	
	public BooleanParameterController(ComponentController parent, BooleanParameter param, Component component){
		this.param = param;
//		this.parent = parent;
		this.component = component;
		this.view = new BooleanParameterPanel(param, this, component);
	}
	
	public ParameterPanel getView(){
		return view;
	}
	
	public void setValue(boolean value){
		assert(!component.getLockedStatus());
	
		try{
			param.setValue(value);
		}catch(ConstraintFailedException e){
			processConstraintFailure(e);
		}
	}
}
