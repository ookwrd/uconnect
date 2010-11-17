package org.u_compare.gui.component;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.TypeListPanelController;
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
		//inputPanel.setLayout(new BoxLayout(inputPanel,
			//	BoxLayout.Y_AXIS));//TODO more lightweight layout manager
		
		TypeListPanelController typeListPanelController = new TypeListPanelController(controller, component, TypeListPanel.INPUTS_LIST);
		TypeListPanel typeListPanel = new TypeListPanel(component, TypeListPanel.INPUTS_LIST, typeListPanelController);
		
		inputPanel.add(typeListPanel);
		
		outputPanel = new JPanel();
		outputPanel.setOpaque(false);
		outputPanel.setBorder(new TitledBorder(new EtchedBorder(),
		"Outputs:"));
		//outputPanel.setLayout(new BoxLayout(outputPanel,
			//	BoxLayout.Y_AXIS));//TODO more lightweight layout manager
		
		typeListPanelController = new TypeListPanelController(controller, component, TypeListPanel.OUTPUTS_LIST);
		typeListPanel = new TypeListPanel(component, TypeListPanel.OUTPUTS_LIST, typeListPanelController);
		
		outputPanel.add(typeListPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(inputPanel);
		add(outputPanel);
		
	}
	
}
