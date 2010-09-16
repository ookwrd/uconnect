package org.u_compare.gui;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.u_compare.gui.control.BooleanConfigController;
import org.u_compare.gui.model.parameters.BooleanParameter;

/**
 * TODO
 * 
 * @author Luke McCrohon
 *
 */

@SuppressWarnings("serial")
public class BooleanConfigPanel extends ParameterPanel {

	private JCheckBox checkBox;
	private BooleanConfigController controller;
	private boolean locked;
	
	public BooleanConfigPanel(
			BooleanParameter param, BooleanConfigController controller, boolean lockedStatus) {
		
			this.controller = controller;
			this.locked = lockedStatus;
			
			this.add(new JLabel(param.getDescription()));
			
			checkBox = new JCheckBox();
			checkBox.setSelected(param.getParameter());
			checkBox.addActionListener(controller);
			
			this.add(checkBox);
	}

	public boolean getValue(){
		return checkBox.isSelected();
	}

	@Override
	public void setLockedStatus(boolean locked) {
		if(this.locked != locked){
			this.locked = locked;
		
			if(locked){
			//	checkBox.//TODO
			}else{
				
			}
		}
	}
}
