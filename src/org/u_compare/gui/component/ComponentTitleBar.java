package org.u_compare.gui.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.guiElements.RoundedBorder;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class ComponentTitleBar extends JPanel {

	private ComponentController controller;
	private Component component;
	private ComponentNamePanel titlePanel;
	private TitleButtonPanel buttonPanel;

	public ComponentTitleBar(ComponentController controller, Component component,
			Minimizable innerPanel, boolean isWorkflow) {

		this.controller = controller;
		this.component = component;
		setLayout(new BorderLayout());
		setOpaque(false);

		if (!isWorkflow) {
			setBorder(new RoundedBorder(null, ComponentPanel.BORDER_COLOR,
					ComponentPanel.HEADER_COLOR,
					ComponentPanel.BORDER_ROUNDING,
					ComponentPanel.BORDER_WIDTH, true));

			setupButtonPanel(this, innerPanel);
			setupTitlePanel(this, false);
		} else {//Workflow
			setBorder(new EmptyBorder(5, 5, 0, 0));
			setupTitlePanel(this, true);
		}

	}

	/**
	 * Find the limit for the size of the title, using : topPanel.width minus
	 * buttonPanel.width
	 */
	public int getTitleLimit() {

		int topPanelWidth = this.getWidth();
		int buttonPanelWidth = 0;
		buttonPanelWidth = buttonPanel.getWidth();
		System.out.println("limit = " + (topPanelWidth - buttonPanelWidth));
		return topPanelWidth - buttonPanelWidth;
	}

	protected void setupTitlePanel(JPanel target, boolean isWorkflow) {

		titlePanel = new ComponentNamePanel(controller, component, isWorkflow, this);
		target.add(titlePanel, BorderLayout.CENTER);

	}

	protected void setupButtonPanel(JPanel target, Minimizable minimizable) {

		buttonPanel = new TitleButtonPanel(controller, component, minimizable);
		target.add(buttonPanel, BorderLayout.EAST);

	}

}
