package org.u_compare.gui;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.model.LockedStatusChangeListener;



@SuppressWarnings("serial")
public abstract class ParameterPanel extends JPanel implements LockedStatusChangeListener{

	protected JLabel description;
	protected JComponent field;
	
	
	public JLabel getDescription(){
		return description;
	}
	
	public JComponent getField(){
		return field;
	}
}
