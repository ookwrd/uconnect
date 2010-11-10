package org.u_compare.gui.component;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.control.ParameterControllerFactory;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.Parameter;

@SuppressWarnings("serial")
public class ParametersPanel extends JPanel {

	private Component component;
	private ComponentController controller;
	
	public ParametersPanel(Component component, ComponentController controller){
		super();
		
		this.component = component;
		this.controller = controller;
		
		ArrayList<ParameterPanel> paramPanels = new ArrayList<ParameterPanel>();
		for (Parameter param : component.getConfigurationParameters()){
			ParameterController paramController = ParameterControllerFactory.getController(controller, param);
			paramPanels.add(paramController.getView());
			controller.addParamaterController(paramController);
		}
		
		// Set up the parameter panel if necessary
		if (paramPanels.size() > 0) {

			setBorder(new TitledBorder(new EtchedBorder(),
					"Configuration Parameters:"));
			setLayout(new BoxLayout(this,
					BoxLayout.Y_AXIS));
			setOpaque(false);

			for (ParameterPanel paramPanel : paramPanels) {
				paramPanel.setOpaque(false);
				add(paramPanel);
			}
		
		}
	}
	
}
