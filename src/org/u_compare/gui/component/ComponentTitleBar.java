package org.u_compare.gui.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.guiElements.RoundedBorder;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class ComponentTitleBar extends JPanel {

	public ComponentTitleBar(ComponentController controller, Component component,
			ComponentPanel innerPanel, boolean isWorkflow) {

		setLayout(new BorderLayout());
		setOpaque(false);

		if (!isWorkflow) {
			setBorder(new RoundedBorder(null, ComponentPanel.BORDER_COLOR,
					ComponentPanel.HEADER_COLOR,
					ComponentPanel.BORDER_ROUNDING,
					ComponentPanel.BORDER_WIDTH, true));

			setupButtonPanel(this, innerPanel, controller, component);
			setupTitlePanel(this, false, controller, component);
		} else {//Workflow
			setBorder(new EmptyBorder(5, 5, 0, 0));
			setupTitlePanel(this, true, controller, component);
		}

	}

	protected void setupTitlePanel(JPanel target, boolean isWorkflow, ComponentController controller, Component component) {
		ComponentNamePanel titlePanel = new ComponentNamePanel(controller, component, isWorkflow, this);
		target.add(titlePanel, BorderLayout.CENTER);
	}

	protected void setupButtonPanel(JPanel target, ComponentPanel minimizable, ComponentController controller, Component component) {
		TitleButtonPanel buttonPanel = new TitleButtonPanel(controller, component, minimizable);
		target.add(buttonPanel, BorderLayout.EAST);
	}

}
