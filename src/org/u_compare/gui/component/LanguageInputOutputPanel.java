package org.u_compare.gui.component;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.LanguageListPanelController;
import org.u_compare.gui.control.TypeListPanelController;
import org.u_compare.gui.model.Component;

/**
 * GUI Class responsible for holding both the language, input, and output
 * panels. Also displays their borders.
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class LanguageInputOutputPanel extends JPanel {

	private JPanel inputPanel;
	private JPanel outputPanel;
	private JPanel languagePanel;

	private TypeListPanelController inputsController;
	private TypeListPanelController outputsController;
	private LanguageListPanelController languageController;

	public LanguageInputOutputPanel(Component component,
			ComponentController controller) {
		super();

		inputPanel = new JPanel();
		inputPanel.setOpaque(false);
		inputPanel.setLayout(new GridLayout(1, 1));
		inputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Inputs:"));

		inputsController = new TypeListPanelController(controller, component,
				TypeListPanel.LIST_TYPES.INPUTS);
		TypeListPanel typeListPanel = new TypeListPanel(component,
				TypeListPanel.LIST_TYPES.INPUTS, inputsController);

		inputPanel.add(typeListPanel);

		outputPanel = new JPanel();
		outputPanel.setOpaque(false);
		outputPanel.setLayout(new GridLayout(1, 1));
		outputPanel.setBorder(new TitledBorder(new EtchedBorder(), "Outputs:"));

		outputsController = new TypeListPanelController(controller, component,
				TypeListPanel.LIST_TYPES.OUTPUTS);
		typeListPanel = new TypeListPanel(component,
				TypeListPanel.LIST_TYPES.OUTPUTS, outputsController);

		outputPanel.add(typeListPanel);

		languagePanel = new JPanel();
		languagePanel.setOpaque(false);
		languagePanel.setLayout(new GridLayout(1, 1));
		languagePanel.setBorder(new TitledBorder(new EtchedBorder(),
				"Languages:"));

		languageController = new LanguageListPanelController(component);
		LanguageListPanel languageListPanel = new LanguageListPanel(component,
				languageController);

		languagePanel.add(languageListPanel);

		setLayout(new GridLayout(1, 3));

		add(languagePanel);
		add(inputPanel);
		add(outputPanel);

	}
}
