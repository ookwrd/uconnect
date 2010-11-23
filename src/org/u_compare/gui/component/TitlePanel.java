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

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.junit.internal.matchers.SubstringMatcher;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class TitlePanel extends JPanel {

	private static final int TITLE_SIZE_LIMIT = 120; // the text will be trimmed
														// if too long
	private static final Font font = new Font("sansserif", Font.BOLD, 12);
	private static final Font titleFont = new Font("sansserif", Font.BOLD, 16);
	private static int titleLabelSizeLimit = 60;
	private TopPanel topPanel;

	private final ComponentController controller;
	private final Component component;

	protected String title = "";

	private JLabel titleLabel;
	private JTextField titleTextField;

	private ActionListener titleListener;
	private FocusListener titleFocusListener;
	private boolean isWorkflow;

	public TitlePanel(ComponentController controller, Component component,
			boolean isWorkflow, TopPanel topPanel) {
		super();

		this.isWorkflow = isWorkflow;
		this.controller = controller;
		this.component = component;
		this.topPanel = topPanel; // there is no parent yet

		titleListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTitle(titleTextField.getText());
				titleTextField.setVisible(false);
				titleLabel.setVisible(true);
			}
		};

		// add a title panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		title = component.getTitle();

		titleLabel = new JLabel(title);
		titleLabel.setText(title);

		titleTextField = new JTextField(title);
		titleTextField.setText(title);

		titleTextField.setDocument(new JTextFieldLimit(TITLE_SIZE_LIMIT));
		if(isWorkflow){
			titleLabel.setFont(titleFont);
			titleTextField.setFont(titleFont);
		}else{
			titleLabel.setFont(font);
			titleTextField.setFont(font);
		}
		titleTextField.setVisible(false);

		titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		//titleLabel.setAlignmentY(CENTER_ALIGNMENT);
		titleTextField.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		
		add(titleLabel);
		add(titleTextField);
		
		
		if (!isWorkflow){
			setBackground(Color.WHITE);
		}
		// else
		// setBackground();

		// start editing
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// JPanel target = (JPanel) e.getSource();
					if (!TitlePanel.this.component.getLockedStatus()) {

						titleTextField.setVisible(false);
						titleTextField.setVisible(true);
						titleTextField.requestFocusInWindow(); // this is one of
																// the
																// nastiest
																// tricks I've
																// run into with
																// swing so far.
																// viva swing,
																// mazel tov.

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
				/*
				 * System.out.println("FOCUS LOST \ntitlelabel " +
				 * titleLabel.getText()); System.out.println("title " + title);
				 * System.out.println("titletextfield " +
				 * titleTextField.getText() + "<-- nothing ?");
				 */
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
		String visualTitle = title;
		if (!isWorkflow) {
			System.out.println("now getting title limit");
			int titlePanelLimit = 20;
			if (topPanel == null) {
				System.err.println("TopPanel is null, this is not normal.");
				topPanel = (TopPanel) this.getParent();
			}
			if (topPanel != null) {
				titlePanelLimit = topPanel.getTitleLimit();
				System.out.println("got titlePanel limit : " + titlePanelLimit);
			} else
				System.err
						.println("TopPanel is still null, this is really not normal."); // TODO
																						// fix
			
			// compute maximal text size
			int delta = 60; // TODO find the right value
			String shortTitle = title;
			int count = 1000;
			while (getLength(shortTitle) > titlePanelLimit - delta && count > 0) {
				System.out.println(shortTitle + " --- " + titlePanelLimit
						+ ", " + title);
				shortTitle = shortTitle.substring(0, shortTitle.length() - 2);
				visualTitle = shortTitle + "...";
				count--;
			}
		}
		this.titleLabel.setText(visualTitle);
		titleTextField.setDocument(new JTextFieldLimit(TITLE_SIZE_LIMIT));

		// if (title.length()<=titleLabelSizeLimit)
		// this.titleLabel.setText(title);
		// else this.titleLabel.setText(title.substring(0,
		// titleLabelSizeLimit)+"...");

		this.titleTextField.setText(title);
		this.controller.setTitle(title);
	}

	/*
	 * public void repaint() { this.setTitle(this.title); super.repaint(); }
	 */

}
