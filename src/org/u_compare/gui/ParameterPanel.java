package org.u_compare.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;

@SuppressWarnings("serial")
public abstract class ParameterPanel extends JPanel implements
		LockedStatusChangeListener {

	protected String description;
	protected boolean mandatory;
	protected JComponent field;
	protected Component component;

	public ParameterPanel(Parameter param, Component component){
		
		this.mandatory = param.isMandatory();
		this.component = component;
		
	}
	
	public String getDescription() {
		return description;
	}

	public JComponent getField() {
		return field;
	}
	
	public boolean isMandatory(){
		return mandatory;
	}
}
