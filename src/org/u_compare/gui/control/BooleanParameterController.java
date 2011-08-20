package org.u_compare.gui.control;

import org.u_compare.gui.component.parameters.BooleanParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.BooleanParameter;

public class BooleanParameterController extends ParameterController {
	
	public BooleanParameterController(ComponentController parent, BooleanParameter param, Component component){//TODO remove component
		this.param = param;
		this.view = new BooleanParameterPanel(param, this, component);
	}
	
	public void setValue(boolean value){//TODO switch to string version.
		((BooleanParameter)param).setValue(value);
	}
}
