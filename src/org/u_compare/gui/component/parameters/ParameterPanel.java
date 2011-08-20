package org.u_compare.gui.component.parameters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterValueChangedListener;

/**
 * 
 * Abstract base class for common functionality of all ParameterPanel types.
 * 
 * @author Luke McCrohon
 *
 */
public abstract class ParameterPanel implements
		LockedStatusChangeListener, ParameterValueChangedListener {

	private static final int DESCRIPTION_LENGTH = 43;
	
	protected JComponent field;
	protected Parameter param;
	
	public ParameterPanel(Parameter param, Component component){
		this.param = param;
		
		if(param instanceof BooleanParameter){
			System.out.println("In super");
		}
		
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
		
		component.registerLockedStatusChangeListener(this);
		param.registerParameterValueChangedListener(this);
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
		System.out.println(field.getClass());
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
	
	protected abstract void textFieldChanged();//TODO move it down here

}
