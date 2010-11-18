package org.u_compare.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.u_compare.gui.control.ActionFocusListener;
import org.u_compare.gui.control.StringParamaterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterSettingsChangedListener;
import org.u_compare.gui.model.parameters.StringParameter;

@SuppressWarnings("serial")
public class StringParameterPanel extends ParameterPanel implements ActionListener, FocusListener, LockedStatusChangeListener, ParameterSettingsChangedListener {

	private StringParamaterController controller;
	private Component component;
	private StringParameter parameter;
	
	private JTextField textField;
	
	public StringParameterPanel(StringParameter param, StringParamaterController control,
			Component component){
		
		this.controller = control;
		this.component = component;
		this.parameter = param;
		
		this.add(new JLabel(param.getDescription()));
		
		textField = new JTextField(param.getParameterString());
		textField.addActionListener(this);
		textField.addFocusListener(this);
		
		updateLockedStatus();
		
		this.add(textField);
		
		component.registerLockedStatusChangeListener(this);
		param.registerParameterSettingsChangedListener(this);
	}

	private void updateLockedStatus(){
		
		if(component.getLockedStatus()){
			textField.setEnabled(false);
		}else{
			textField.setEnabled(true);
		}
	}
	
	@Override
	public void lockStatusChanged(Component component) {
		updateLockedStatus();
	}

	@Override
	public void parameterSettingsChanged(Parameter param) {
		
		textField.setText(param.getParameterString());
	}
	
	private void textFieldChanged(){
		String value = textField.getText();
		textField.setText(parameter.getParameter());
		controller.setValue(value);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		textFieldChanged();
	}

	@Override
	public void focusGained(FocusEvent e) {
		//No special action needed.
	}

	@Override
	public void focusLost(FocusEvent e) {
		textFieldChanged();		
	}
	
}
