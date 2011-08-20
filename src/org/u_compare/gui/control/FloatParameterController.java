package org.u_compare.gui.control;

import org.u_compare.gui.component.parameters.FloatParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.FloatParameter;

public class FloatParameterController extends ParameterController {
	
	public FloatParameterController(ComponentController control,
			FloatParameter param, Component component) {
		this.param = param;
		this.view = new FloatParameterPanel(param, this, component);
	}
}
