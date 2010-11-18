package org.u_compare.gui;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.u_compare.gui.control.ActionFocusListener;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;

@SuppressWarnings("serial")
public class IntegerParameterPanel extends ParameterPanel implements LockedStatusChangeListener {

	private ActionFocusListener controller;
	
	private JTextField textField;
	
	private Component component;
	
	public IntegerParameterPanel(Parameter param, ActionFocusListener control,
			String initialValue, Component component){
		
		this.controller = control;
		this.component = component;
		
		this.add(new JLabel(param.getDescription()));
		
		textField = new JTextField(initialValue);
		textField.addActionListener(controller);
		textField.addFocusListener(controller);
		
		updateLockedStatus();
		
		this.add(textField);
		
		component.registerLockedStatusChangeListener(this);
	}
	
	public String getString(){
		return textField.getText();
	}
	
	public void setString(String value){
		textField.setText(value);
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
	
}
