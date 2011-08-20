package org.u_compare.gui.component.parameters;

import javax.swing.JTextField;

import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterValueChangedListener;

public class StringParameterPanel extends ParameterPanel implements LockedStatusChangeListener, ParameterValueChangedListener {

	private ParameterController controller;
	
	public StringParameterPanel(Parameter param, ParameterController control,
			Component component){
		super(param, component);
		
		this.controller = control;
		
		updateLockedStatus(component);
	}
	
	public void textFieldChanged(){
		String value = ((JTextField)field).getText();
		((JTextField)field).setText(param.getParameterString());
		controller.setValue(value);
	}
}
