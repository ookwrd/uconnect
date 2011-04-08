package org.u_compare.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import org.u_compare.gui.control.StringParamaterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterSettingsChangedListener;
import org.u_compare.gui.model.parameters.StringParameter;

@SuppressWarnings("serial")
public class StringParameterPanel extends ParameterPanel implements ActionListener, FocusListener, LockedStatusChangeListener, ParameterSettingsChangedListener {

	private StringParamaterController controller;
	private StringParameter parameter;
	private JTextField textField;
	
	public StringParameterPanel(StringParameter param, StringParamaterController control,
			Component component){
		super(param, component);
		
		this.controller = control;
		this.parameter = param;
		
		textField = new JTextField(param.getParameterString());
		textField.addActionListener(this);
		textField.addFocusListener(this);
		
		this.add(textField);
		
		component.registerLockedStatusChangeListener(this);
		param.registerParameterSettingsChangedListener(this);
		
		field = textField;
		
		updateLockedStatus();
	}

	@Override
	public void parameterSettingsChanged(Parameter param) {
		textField.setText(parameter.getParameterString());
	}
	
	private void textFieldChanged(){
		String value = textField.getText();
		textField.setText(parameter.getParameter());
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
