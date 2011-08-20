package org.u_compare.gui.component.parameters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterValueChangedListener;

/**
 * 
 * Abstract base class for common functionality of all ParameterPanel types.
 * 
 * @author Luke McCrohon
 *
 */
public class ParameterPanel implements
		LockedStatusChangeListener, ParameterValueChangedListener {

	private static final int DESCRIPTION_LENGTH = 43;
	
	protected JComponent field;
	protected Parameter param;
	protected ParameterController controller;
	
	public ParameterPanel(Parameter param, ParameterController controller, Component component){
		this.param = param;
		this.controller = controller;
		
		if(!param.isMultivalued()){
			//Setup default field
			JTextField textField = new JTextField(param.getParameterString());
			textField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textFieldChanged();
				}
			});
			textField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					textFieldChanged();		
				}
			});
			field = textField;
		}else{
			//TODO
		}
		
		component.registerLockedStatusChangeListener(this);
		param.registerParameterValueChangedListener(this);
		
		updateLockedStatus(component);
	}
	
	public JLabel getLabel() {
		String description = param.getDescription();
		if(description.length() > DESCRIPTION_LENGTH){
			description = description.substring(0,DESCRIPTION_LENGTH-3) + "...";
		}
		JLabel descriptionLabel = new JLabel(description);
		descriptionLabel.setToolTipText(param.getDescription());//Unabridged description
		descriptionLabel.setHorizontalAlignment(JLabel.TRAILING);
		
		return descriptionLabel;
	}
	
	public JLabel getMandatoryLabel(){
		JLabel mandatory;
		if(param.isMandatory()){
			mandatory = new JLabel("*");
			mandatory.setToolTipText("Mandatory Parameter");
		}else{
			mandatory = new JLabel("");
		}
		return mandatory;
	}
	
	public JComponent getField() {
		return field;
	}
	
	@Override
	public void lockStatusChanged(Component component) {
		updateLockedStatus(component);
	}
	
	protected void updateLockedStatus(Component component){
		field.setEnabled(!component.getLockedStatus());
	}
	
	@Override
	public void parameterSettingsChanged(Parameter param) {
		((JTextField)field).setText(param.getParameterString());
	}
	
	protected void textFieldChanged(){
		String value = ((JTextField)field).getText();
		((JTextField)field).setText(param.getParameterString());
		controller.setValue(value);
	}
}
