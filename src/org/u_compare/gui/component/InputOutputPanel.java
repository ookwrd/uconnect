package org.u_compare.gui.component;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.TypeListPanelController;
import org.u_compare.gui.model.Component;

/**
 * GUI Class responsible for holding both the input and output panels and drawing their borders.
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class InputOutputPanel extends JPanel {
	
	private JPanel inputPanel;
	private JPanel outputPanel;
	private JPanel languagePanel;
	
	private TypeListPanelController inputsController;
	private TypeListPanelController outputsController;
	
	public InputOutputPanel(Component component,
			ComponentController controller) {
		super();
		
		inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setBorder(new TitledBorder(new EtchedBorder(),
		"Inputs:"));

		inputsController = new TypeListPanelController(controller, component, TypeListPanel.LIST_TYPES.INPUTS);
		TypeListPanel typeListPanel = new TypeListPanel(component, TypeListPanel.LIST_TYPES.INPUTS, inputsController);
		
		inputPanel.add(typeListPanel);
		
		outputPanel = new JPanel();
		outputPanel.setOpaque(false);
		outputPanel.setBorder(new TitledBorder(new EtchedBorder(),
		"Outputs:"));
		
		outputsController = new TypeListPanelController(controller, component, TypeListPanel.LIST_TYPES.OUTPUTS);
		typeListPanel = new TypeListPanel(component, TypeListPanel.LIST_TYPES.OUTPUTS, outputsController);
		
		outputPanel.add(typeListPanel);
		
		languagePanel = new JPanel();
		languagePanel.setOpaque(false);
		languagePanel.setBorder(new TitledBorder("Languages:"));
		
		setLayout(new GridLayout(1,3));
		add(inputPanel);
		add(outputPanel);
		add(languagePanel);
		
	}
}
