package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.border.EmptyBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;

public class EditableTextPanel extends JPanel {

	private static final int DESCRIPTION_PANEL_PADDING = 5;
	public final Color defaultColor = getBackground();

	private final ComponentController controller;
	private final Component component;

	private ActionListener descriptionListener;
	private FocusListener descriptionFocusListener;

	private JTextArea description;
	private JTextArea editableDescription;
	private String descriptionText;
	private JButton endEditingButton;

	private ActionListener endEditingListener;

	public EditableTextPanel(ComponentController controller, Component component) {

		this.controller = controller;
		this.component = component;

		descriptionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDescription(editableDescription.getText());
				editableDescription.setVisible(false);
				description.setVisible(true);
				endEditingButton.setVisible(true);
			}
		};

		descriptionFocusListener = new FocusListener() {

			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				setDescription(editableDescription.getText());
				editableDescription.setVisible(false);
				description.setVisible(true);
				endEditingButton.setVisible(false);
			}
		};

		endEditingListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO remove the useless lines
				System.out.println("PUSHED STOP EDITING");
				setDescription(editableDescription.getText());
				editableDescription.setVisible(false);
				description.setVisible(true);
				endEditingButton.setVisible(false);
			}
		};

		// add a description panel under the top panel, and first set the layout
		BorderLayout descriptionLayout = new BorderLayout();
		setLayout(descriptionLayout);

		setOpaque(false);
		setBorder(new EmptyBorder(new Insets(DESCRIPTION_PANEL_PADDING,
				DESCRIPTION_PANEL_PADDING, DESCRIPTION_PANEL_PADDING,
				DESCRIPTION_PANEL_PADDING)));

		descriptionText = component.getDescription();

		description = new JTextArea(descriptionText);
		description.setBackground(defaultColor);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);

		editableDescription = new JTextArea(descriptionText);
		editableDescription.setBackground(Color.WHITE);
		editableDescription.setLineWrap(true);
		editableDescription.setWrapStyleWord(true);
		editableDescription.setEditable(true);
		editableDescription.setVisible(false);

		endEditingButton = new JButton("Save");
		endEditingButton.setActionCommand("End description editing");
		endEditingButton.addActionListener(endEditingListener);
		endEditingButton.setVisible(false);

		add(description, BorderLayout.PAGE_START);
		add(editableDescription, BorderLayout.CENTER);
		add(endEditingButton, BorderLayout.AFTER_LAST_LINE);
		// TODO change the layout to cardlayout, creating the cards as on
		// http://download.oracle.com/javase/tutorial/uiswing/layout/card.html

		setBackground(Color.WHITE);

		description.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// JPanel target = (JPanel) e.getSource();
					if (!EditableTextPanel.this.component.getLockedStatus()) {
						// setDescription(description.getText());
						description.setVisible(false);
						editableDescription.setVisible(true);

						// editableDescription.requestFocusInWindow();
						editableDescription.setVisible(false);
						editableDescription.setVisible(true);
						editableDescription.requestFocus();

						endEditingButton.setVisible(true);
					}
				}
			}
		});

		// editableDescription.addActionListener(descriptionListener); //
		// useless: not a text field anymore
		editableDescription.addFocusListener(descriptionFocusListener);

	}

	protected void setDescription(String descriptionText) {

		this.descriptionText = descriptionText;
		description.setText(descriptionText);
		editableDescription.setText(descriptionText);
	}

}