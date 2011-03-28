package org.u_compare.gui;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.u_compare.gui.model.LockedStatusChangeListener;

@SuppressWarnings("serial")
public abstract class ParameterPanel extends JPanel implements
		LockedStatusChangeListener {

	protected String description;
	protected boolean mandatory;
	protected JComponent field;

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
