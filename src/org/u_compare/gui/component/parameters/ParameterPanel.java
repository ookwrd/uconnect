package org.u_compare.gui.component.parameters;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;

/**
 * 
 * Abstract base class for common functionality of all ParameterPanel types.
 * 
 * @author Luke Mccrohon
 *
 */
@SuppressWarnings("serial")
public abstract class ParameterPanel extends JPanel implements
		LockedStatusChangeListener {

	private static final int DESCRIPTION_LENGTH = 43;
	
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
