package org.u_compare.gui.component;

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
		//inputPanel.setLayout(new BoxLayout(inputPanel,
			//	BoxLayout.Y_AXIS));//TODO more lightweight layout manager
		
		inputPanel.add(new TypeListPanel(component,new String[]{"first","second","thirdsafasdfasdfasfdasdfasdfasdfasdfsadfasdfsafsdfsa"}));
		
		outputPanel = new JPanel();
		outputPanel.setOpaque(false);
		outputPanel.setBorder(new TitledBorder(new EtchedBorder(),
		"Outputs:"));
		//outputPanel.setLayout(new BoxLayout(outputPanel,
			//	BoxLayout.Y_AXIS));//TODO more lightweight layout manager
		
		outputPanel.add(new TypeListPanel(component, new String[]{"output1","output2","output3","output4"}));
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(inputPanel);
		add(outputPanel);
		
	}
	
}
