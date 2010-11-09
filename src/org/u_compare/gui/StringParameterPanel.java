package org.u_compare.gui;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.u_compare.gui.control.ActionFocusListener;
import org.u_compare.gui.control.StringParamaterController;
import org.u_compare.gui.model.parameters.Parameter;

@SuppressWarnings("serial")
public class StringParameterPanel extends ParameterPanel {

	ActionFocusListener controller;
	
	JTextField textField;
	
	public StringParameterPanel(Parameter param, ActionFocusListener control, String initialValue){
		
		this.controller = control;
		
		this.add(new JLabel(param.getDescription()));
		
		textField = new JTextField(initialValue);
		textField.addActionListener(controller);
		textField.addFocusListener(controller);
		
		this.add(textField);
	}
	
	public String getString(){
		return textField.getText();
	}
	
	public void setString(String value){
		textField.setText(value);
	}

	@Override
	public void setLockedStatus(boolean locked) {
		// TODO Auto-generated method stub
		
	}
	
}
