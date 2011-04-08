package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.SpringUtilities;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.control.ParameterControllerFactory;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.Parameter;

@SuppressWarnings("serial")
public class ParametersPanel extends JPanel {
	
	public ParametersPanel(Component component,
			ComponentController controller) {
		super();
		
		ArrayList<ParameterPanel> paramPanels = new ArrayList<ParameterPanel>();
		for (Parameter param : component.getConfigurationParameters()){
			ParameterController paramController =
				ParameterControllerFactory.getController(controller,
						param, component);
			paramPanels.add(paramController.getView());
			controller.addParamaterController(paramController);
		}
		
		// Set up the parameter panel if necessary
		if (paramPanels.size() > 0) {

			setBorder(new TitledBorder(new EtchedBorder(),
					"Configuration Parameters:"));
			setLayout(new BorderLayout());
			setOpaque(false);
			
			/*
			 * Needed so that the Parameter Panel can expand horizontally even if 
			 * none of the subcomponents can, which is not permitted by the spring
			 * layout.
			 */
			JPanel innerPanel = new JPanel();
			
			innerPanel.setLayout(new SpringLayout());
			for (ParameterPanel parameterPanel : paramPanels) {
				
				//Parameter Description Label
				JLabel description = parameterPanel.getDescriptionLabel();
				innerPanel.add(description);
				
				//Parameter Mandatory Status Label
				JLabel mandatory = parameterPanel.getMandatoryLabel();
				innerPanel.add(mandatory);
				
				//Parameter Setting Field
				JComponent selectionComponent = parameterPanel.getField();
				description.setLabelFor(selectionComponent);
				
				innerPanel.add(selectionComponent);
			}
			
			SpringUtilities.makeCompactGrid(innerPanel,
                    paramPanels.size(), 3, 	//rows, cols
                    6, 6,        			//initX, initY
                    6, 0);      			 //xPad, yPad

			add(innerPanel);
		
		}
	}
	
}
