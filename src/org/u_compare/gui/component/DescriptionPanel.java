package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.DescriptionChangeListener;

@SuppressWarnings("serial")
public class DescriptionPanel extends JPanel implements
		DescriptionChangeListener {

	//private static final int DESCRIPTION_PANEL_PADDING = 5;

	//public final Color defaultColor = getBackground();

	private final ComponentController controller;
	private final Component component;
	private EditableTextPanel innerPanel;
	
	private static Font font;

	public DescriptionPanel(ComponentController controller, Component component) {
		super();

		this.controller = controller;
		this.component = component;
		this.font = this.getFont();
		
		innerPanel = new EditableTextPanel(controller, component);
		
		innerPanel.registerActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setDescription(innerPanel.getDescription());
			}
		});
		
		// add a description panel under the top panel, and first set the layout
		BorderLayout descriptionLayout = new BorderLayout();
		setLayout(descriptionLayout);
		setOpaque(false);
		this.add(innerPanel);
		
		// Register Listeners
		component.registerComponentDescriptionChangeListener(this);

	}
	
	// TODO this might be set directly by the controller
	protected void setDescription(String descriptionText) {
		
		descriptionText = descriptionText.trim();
		innerPanel.setDescription(descriptionText);
		this.controller.setDescription(descriptionText);
	}

	@Override
	public void ComponentDescriptionChanged(Component component1) {

		System.out.println("Components name changed to: "
				+ component.getTitle());
		// TODO
	}

}
