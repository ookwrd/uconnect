package org.u_compare.gui.component.parameters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterValueChangedListener;

public class IntegerParameterPanel extends ParameterPanel implements  ActionListener, FocusListener, LockedStatusChangeListener, ParameterValueChangedListener  {

	private ParameterController controller;
	private JTextField textField;
	
	public IntegerParameterPanel(Parameter parameter, ParameterController control,
			 Component component){
		super(parameter, component);
		
		this.controller = control;
		
		textField = new JTextField(parameter.getParameterString());
		textField.addActionListener(this);
		textField.addFocusListener(this);
		
		parameter.registerParameterValueChangedListener(this);
		
		field = textField;
		
		updateLockedStatus(component);
	}

	@Override
	public void parameterSettingsChanged(Parameter param) {
		textField.setText(param.getParameterString());
	}

	public void textFieldChanged(){
		//Change should not be reflected in view unless the underlying model changes
		String value = textField.getText();
		textField.setText(param.getParameterString());
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
