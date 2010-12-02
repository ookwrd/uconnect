package org.u_compare.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JTextField;
import org.u_compare.gui.control.IntegerParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterSettingsChangedListener;

@SuppressWarnings("serial")
public class IntegerParameterPanel extends ParameterPanel implements  ActionListener, FocusListener, LockedStatusChangeListener, ParameterSettingsChangedListener  {

	private IntegerParameterController controller;
	private Component component;
	private IntegerParameter parameter;
	
	private JTextField textField;
	
	public IntegerParameterPanel(IntegerParameter parameter, IntegerParameterController control,
			 Component component){
		
		this.controller = control;
		this.component = component;
		this.parameter = parameter;
		
		description = new JLabel(parameter.getDescription());
		this.add(description);
		
		textField = new JTextField(parameter.getParameterString());
		textField.addActionListener(this);
		textField.addFocusListener(this);
		
		updateLockedStatus();
		
		this.add(textField);
		
		component.registerLockedStatusChangeListener(this);
		parameter.registerParameterSettingsChangedListener(this);
		
		field = textField;
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
		
		textField.setText(parameter.getParameterString());
	}

	private void textFieldChanged(){
		//Change should not be reflected in view unless the underlying model changes
		String value = textField.getText();
		textField.setText(parameter.getParameterString());
		controller.setValue(value);
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		//No special action needed.
	}

	@Override
	public void focusLost(FocusEvent e) {
		textFieldChanged();	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		textFieldChanged();	
	}
	
}
