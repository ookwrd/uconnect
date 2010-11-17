package org.u_compare.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.u_compare.gui.control.BooleanParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterSettingsChangedListener;

/**
 * TODO
 * 
 * @author Luke McCrohon
 *
 */

@SuppressWarnings("serial")
public class BooleanParameterPanel extends ParameterPanel implements ActionListener, LockedStatusChangeListener, ParameterSettingsChangedListener{

	private JCheckBox checkBox;
	private BooleanParameterController controller;
	private Component component;
	private BooleanParameter param;
	
	public BooleanParameterPanel(
			BooleanParameter param, BooleanParameterController controller, Component component) {
		
			this.controller = controller;
			this.component = component;
			this.param = param;
			
			this.add(new JLabel(param.getDescription()));
			
			checkBox = new JCheckBox();
			checkBox.setSelected(param.getParameter());
			checkBox.addActionListener(this);
			
			this.add(checkBox);
			
			component.registerLockedStatusChangeListener(this);
			param.registerParameterSettingsChangedListener(this);
			
	}

	public boolean getValue(){
		return checkBox.isSelected();
	}

	private void updateLockedStatus(){
		if(component.getLockedStatus()){
			checkBox.setEnabled(false);
		}else{
			checkBox.setEnabled(true);
		}
	}
	

	@Override
	public void lockStatusChanged(Component component) {
		
		updateLockedStatus();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		controller.setValue(checkBox.isSelected());
		
	}

	@Override
	public void parameterSettingsChanged(Parameter param) {
		
		checkBox.setSelected(this.param.getParameter());
		
	}

}
