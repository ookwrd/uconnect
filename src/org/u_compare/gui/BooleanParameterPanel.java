package org.u_compare.gui;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.u_compare.gui.control.BooleanParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.BooleanParameter;

/**
 * TODO
 * 
 * @author Luke McCrohon
 *
 */

@SuppressWarnings("serial")
public class BooleanParameterPanel extends ParameterPanel implements LockedStatusChangeListener {

	private JCheckBox checkBox;
	private BooleanParameterController controller;
	private Component component;
	
	public BooleanParameterPanel(
			BooleanParameter param, BooleanParameterController controller, Component component) {
		
			this.controller = controller;
			this.component = component;
			
			this.add(new JLabel(param.getDescription()));
			
			checkBox = new JCheckBox();
			checkBox.setSelected(param.getParameter());
			checkBox.addActionListener(controller);
			
			this.add(checkBox);
			
			component.registerLockedStatusChangeListener(this);
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
}
