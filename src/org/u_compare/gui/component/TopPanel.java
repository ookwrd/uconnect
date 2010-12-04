package org.u_compare.gui.component;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class TopPanel extends JPanel {

	private ComponentController controller;
	private Component component;
	private TitlePanel titlePanel;
	private ButtonPanel buttonPanel;

	public TopPanel(ComponentController controller, Component component,
			JPanel innerPanel, boolean isWorkflow) {

		this.controller = controller;
		this.component = component;
		// this.setFocusable(false);
		BorderLayout topLayout = new BorderLayout();
		setLayout(topLayout);
		setOpaque(false);

		if (!isWorkflow) {
			setBorder(new RoundedBorder(null, ComponentPanel.BORDER_COLOR,
					ComponentPanel.HEADER_COLOR,
					ComponentPanel.BORDER_ROUNDING,
					ComponentPanel.BORDER_WIDTH, true));

			setupButtonPanel(this, innerPanel);
			setupTitlePanel(this, false);
		} else {
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

		titlePanel = new TitlePanel(controller, component, isWorkflow, this);
		target.add(titlePanel, BorderLayout.LINE_START);

	}

	protected void setupButtonPanel(JPanel target, JPanel minimizable) {

		buttonPanel = new ButtonPanel(controller, component, minimizable);
		target.add(buttonPanel, BorderLayout.LINE_END);

	}

}
