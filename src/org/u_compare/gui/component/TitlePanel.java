package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
	private static final Font font = new Font("sansserif", Font.BOLD, 12);
	private static int titleLabelSizeLimit = 60;
	private TopPanel topPanel;

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
		this.topPanel = (TopPanel) getParent();

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
		titleLabel.setFont(font);
		titleTextField.setFont(font);
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
						
						
						titleTextField.setVisible(false);
						titleTextField.setVisible(true);
						titleTextField.requestFocusInWindow(); 	// this is one of the
																// nastiest tricks I've
																// run into with swing so far.
																// viva swing, mazel tov.

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

	private int getLength(String str) {
		return getFontMetrics(font).stringWidth(str);
	}
	
	// TODO this might be set directly by the controller
	protected void setTitle(String title) {
		this.title = title;
		/*System.out.println("TITLE_PANEL LENGTH = " + topPanel.getWidth() );
		int titlePanelLimit = topPanel.getTitleLimit();
		//getLength(title)>
		String shortTitle = title;
		while(this.getWidth() > titlePanelLimit-50) {
			shortTitle = shortTitle.substring(0,shortTitle.length()-3);
			this.titleLabel.setText(shortTitle+"...");
		}
		
		*/
		// apres c'est poubelle
		/*
		if(title.length()<=titleLabelSizeLimit) {
			this.titleLabel.setText(title);
		}
		else {
			int titleWidth = 30;
			this.titleLabel.setText(title.substring(0, titleWidth)+"...");//TODO the object still takes more place
		}
		*/
		if (title.length()<=titleLabelSizeLimit)  			this.titleLabel.setText(title);

		else this.titleLabel.setText(title.substring(0, titleLabelSizeLimit)+"...");
		this.titleTextField.setText(title);
		this.controller.setTitle(title);
	}

}
