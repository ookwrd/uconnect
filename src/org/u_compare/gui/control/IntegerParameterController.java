package org.u_compare.gui.control;

import org.u_compare.gui.component.parameters.IntegerParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.IntegerParameter;

public class IntegerParameterController extends ParameterController {

	private ComponentController parent;
	
	public IntegerParameterController(ComponentController control,
			IntegerParameter param, Component component) {
		this.parent = control;
		this.param = param;
		this.view = new IntegerParameterPanel(param, this, component);
	}
	
}
