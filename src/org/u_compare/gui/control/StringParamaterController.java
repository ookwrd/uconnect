package org.u_compare.gui.control;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.StringParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.InvalidInputException;
import org.u_compare.gui.model.parameters.StringParameter;

public class StringParamaterController implements ParameterController {

	private StringParameter param;
	private ComponentController parent;
	private StringParameterPanel view;
	private Component component;
	
	public StringParamaterController(ComponentController control,
			StringParameter param, Component component) {
		this.parent = control;
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
			param.update(parameterValue);
		}catch(Exception e){
			//TODO can there be such a thing as an invalid boolean?
		}
		
	}
	
}
