package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class TitlePanel extends JPanel {

	private static final int TITLE_SIZE_LIMIT = 30;

	private final ComponentController controller;
	private final Component component;

	protected String title;

	private JLabel titleLabel;
	private JTextField titleTextField;

	private ActionListener titleListener;
	private FocusListener titleFocusListener;

	public TitlePanel(ComponentController controller, Component component) {
		super();

		this.controller = controller;
		this.component = component;

		titleListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTitle(titleTextField.getText());
				titleTextField.setVisible(false);
				titleLabel.setVisible(true);
			}
		};

		// add a title panel
		setLayout(new CardLayout());

		titleLabel = new JLabel(title);

		titleTextField = new JTextField(15);
		titleTextField.setText(title);
		titleTextField.setDocument(new JTextFieldLimit(TITLE_SIZE_LIMIT));
		titleLabel.setFont(new Font("sansserif", Font.BOLD, 12));
		titleTextField.setFont(new Font("sansserif", Font.BOLD, 12));
		titleTextField.setVisible(false);

		add(titleLabel, BorderLayout.LINE_START);
		add(titleTextField, BorderLayout.LINE_START);
		setBackground(Color.WHITE);

		this.setTitle(component.getName());

		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// JPanel target = (JPanel) e.getSource();
					if (!TitlePanel.this.component.getLockedStatus()) {
						titleLabel.setVisible(false);
						titleTextField.setVisible(true);
					}
				}
			}
		});

		titleFocusListener = new FocusListener() {

			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				setTitle(titleLabel.getText());
				titleTextField.setVisible(false);
				titleLabel.setVisible(true);
			}
		};

		// the title JTextField has listeners
		titleTextField.addActionListener(titleListener);
		titleTextField.addFocusListener(titleFocusListener);

	}

	// TODO this might be set directly by the controller
	protected void setTitle(String title) {
		this.title = title;
		titleLabel.setText(title);
		titleTextField.setText(title);
		this.controller.setTitle(title);
	}

}
