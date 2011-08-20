package org.u_compare.gui.control;

import org.u_compare.gui.component.parameters.StringParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.StringParameter;

public class StringParamaterController extends ParameterController {
	
	public StringParamaterController(ComponentController control,
			StringParameter param, Component component) {
		this.param = param;
		this.view = new StringParameterPanel(param, this, component);
	}
	
}
