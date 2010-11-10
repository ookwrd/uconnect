package org.u_compare.gui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class InputOutputPanel extends JPanel {

	private Component component;
	private ComponentController controller;
	
	private JPanel inputPanel;
	private JPanel outputPanel;
	
	public InputOutputPanel(Component component, ComponentController controller){
		super();
		
		this.component = component;
		this.controller = controller;
		
		inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setBorder(new TitledBorder(new EtchedBorder(),
		"Inputs:"));
		
		inputPanel.add(new JLabel("This is an input"));
		
		outputPanel = new JPanel();
		outputPanel.setOpaque(false);
		outputPanel.setBorder(new TitledBorder(new EtchedBorder(),
		"Outputs:"));
		
		outputPanel.add(new JLabel("This is an output"));
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(inputPanel);
		add(outputPanel);
		
	}
	
}
