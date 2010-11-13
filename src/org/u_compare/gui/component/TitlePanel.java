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

import org.junit.internal.matchers.SubstringMatcher;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class TitlePanel extends JPanel {

	private static final int TITLE_SIZE_LIMIT = 120; // the text will be trimmed if too long
	private static int titleLabelSizeLimit = 50;

	private final ComponentController controller;
	private final Component component;

	protected String title;

	private JLabel titleLabel;
	private JTextField titleTextField;

	private ActionListener titleListener;
	private FocusListener titleFocusListener;

	public TitlePanel(ComponentController controller, Component component,
			boolean whiteBackground) {
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

		title = component.getName();

		titleLabel = new JLabel(title);
		titleLabel.setText(title);//

		titleTextField = new JTextField(title);
		titleTextField.setText(title);

		titleTextField.setDocument(new JTextFieldLimit(TITLE_SIZE_LIMIT));
		titleLabel.setFont(new Font("sansserif", Font.BOLD, 12));
		titleTextField.setFont(new Font("sansserif", Font.BOLD, 12));
		titleTextField.setVisible(false);

		add(titleLabel, BorderLayout.LINE_START);
		add(titleTextField, BorderLayout.LINE_START);
		if (whiteBackground)
			setBackground(Color.WHITE);

		// start editing
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// JPanel target = (JPanel) e.getSource();
					if (!TitlePanel.this.component.getLockedStatus()) {
						titleTextField.requestFocusInWindow();

						titleTextField.setVisible(false);
						titleTextField.setVisible(true);
						titleTextField.requestFocus(); // this is one of the
														// nastiest tricks I've
														// seen in swing. viva
														// swing.

						setTitle(title);
						titleLabel.setVisible(false);
						titleTextField.setVisible(true);
					}
				}
			}
		});

		titleFocusListener = new FocusListener() {

			public void focusGained(FocusEvent e) {
				titleTextField.grabFocus();
			}

			public void focusLost(FocusEvent e) {
				setTitle(titleTextField.getText());
				/*System.out.println("FOCUS LOST \ntitlelabel "
						+ titleLabel.getText());
				System.out.println("title " + title);
				System.out.println("titletextfield " + titleTextField.getText()
						+ "<-- nothing ?");*/
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
		if(title.length()<=titleLabelSizeLimit) {
			this.titleLabel.setText(title);
		}
		else {
			this.titleLabel.setText(title.substring(0,titleLabelSizeLimit-4)+" ...");//TODO the object still takes more place
		}
		this.titleTextField.setText(title);
		this.controller.setTitle(title);
	}

}
