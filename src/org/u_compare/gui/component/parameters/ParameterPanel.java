package org.u_compare.gui.component.parameters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.guiElements.ControlList;
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
	
	public ParameterPanel(Parameter param, final ParameterController controller, Component component){
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
			//textField.setMaximumSize(new Dimension(1000, textField.getPreferredSize().height));
			field = textField;
		}else{
			final ControlList list = new ControlList(new JPanel().getBackground());
			
			ActionListener addListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.addValue();
				}
			};
			
			ActionListener removeListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*JList and DefaultListModel provide no way for accessing
					 * contents as anything other than an Object[]. Hence having
					 * to go through the array element by element and casting each
					 * selected name individually.
					 */
					for(Object name : list.getSelectedValues()){
						controller
							.removeValue((String)name);
					}	
				}
			};
			
			list.registerAddActionListener(addListener);
			list.registerRemoveActionListener(removeListener);
			
			field = list;

			rebuildListContents();
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
		if(field instanceof JTextField){
			((JTextField)field).setText(param.getParameterString());
		}else if(field instanceof ControlList){
			rebuildListContents();
		}else{
			System.err.println("If a class overriding parameter panel changes the field it also needs to override the parameterSettingsChanged method");
		}
	}
	
	protected void textFieldChanged(){
		String value = ((JTextField)field).getText();
		((JTextField)field).setText(param.getParameterString());
		controller.setValue(value);
	}
	
	private void rebuildListContents(){
		((ControlList)field).rebuildListContents(new ArrayList<String>(Arrays.asList(param.getParameterStrings())));
	}
}
