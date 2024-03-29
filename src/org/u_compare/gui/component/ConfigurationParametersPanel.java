package org.u_compare.gui.component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterGroup;

/**
 * Sets up the sub panel of component panel titled "Configuration Parameters:".
 * 
 * @author Luke Mccrohon
 */
@SuppressWarnings("serial")
public class ConfigurationParametersPanel extends JPanel {

	private final Component component;
	private final ComponentController controller;

	public ConfigurationParametersPanel(Component component,
			ComponentController controller) {
		super();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.component = component;
		this.controller = controller;

		// Set up the parameter panel if necessary
		if (component.getConfigurationParameters().size() > 0) {
			setBorder(new TitledBorder(new EtchedBorder(),
					"Configuration Parameters:"));
			setupPanel(component.getConfigurationParameters(), this);
		}

		if (component.getCommonParameters().size() > 0) {
			JPanel target = new JPanel();
			target.setLayout(new BoxLayout(target, BoxLayout.Y_AXIS));
			target.setBorder(new TitledBorder(new EtchedBorder(),
					"Common Parameters:"));
			setupPanel(component.getCommonParameters(), target);
			this.add(target);
		}

		ArrayList<ParameterGroup> groups = component.getParameterGroups();
		for (ParameterGroup group : groups) {
			String[] names = group.getNames();
			JPanel target = new JPanel();
			target.setLayout(new BoxLayout(target, BoxLayout.Y_AXIS));
			target.setBorder(new TitledBorder(new EtchedBorder(), names[0]));
			setupPanel(group.getConfigurationParameters(), target);
			this.add(target);
		}
	}

	private void setupPanel(ArrayList<Parameter> params, JPanel target) {

		// Collect the descendant parameter panels.
		ArrayList<ParameterPanel> paramPanels = new ArrayList<ParameterPanel>();
		for (Parameter param : params) {
			ParameterController paramController = new ParameterController(
					controller, param, component);
			paramPanels.add(paramController.getView());
			controller.addParamaterController(paramController);
		}

		if (paramPanels.size() > 0) {
			target.setOpaque(false);

			JPanel innerPanel = new JPanel();
			innerPanel.setLayout(new GridBagLayout());
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.fill = GridBagConstraints.HORIZONTAL;

			for (int i = 0; i < paramPanels.size(); i++) {
				ParameterPanel parameterPanel = paramPanels.get(i);

				// Parameter Description Label
				constraints.gridx = 0;
				constraints.gridy = i;
				constraints.weightx = 0;
				JLabel description = parameterPanel.getLabel();
				innerPanel.add(description, constraints);

				// Parameter Mandatory Status Label
				constraints.gridx = 1;
				constraints.gridy = i;
				constraints.weightx = 0;
				JLabel mandatory = parameterPanel.getMandatoryLabel();
				innerPanel.add(mandatory, constraints);

				// Parameter Setting Field
				constraints.gridx = 2;
				constraints.gridy = i;
				constraints.weightx = 1;
				JComponent selectionComponent = parameterPanel.getField();
				description.setLabelFor(selectionComponent);

				innerPanel.add(selectionComponent, constraints);
			}

			target.add(innerPanel);
		}
	}
}
