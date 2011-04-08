package org.u_compare.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;

@SuppressWarnings("serial")
public abstract class ParameterPanel extends JPanel implements
		LockedStatusChangeListener {

	protected Parameter param;
	protected JComponent field;
	protected Component component;

	public ParameterPanel(Parameter param, Component component){
		
		this.param = param;
		this.add(new JLabel(param.getDescription()));
		this.component = component;
		
	}
	
	public String getDescription(){
		return param.getDescription();
	}
	
	public JLabel getDescriptionLabel() {
		return new JLabel(param.getDescription()); //TODO refactor
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
		updateLockedStatus();
	}
	
	protected void updateLockedStatus(){
		if(component.getLockedStatus()){
			field.setEnabled(false);
		}else{
			field.setEnabled(true);
		}
	}

}
