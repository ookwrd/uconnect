package org.u_compare.gui.guiElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.u_compare.gui.model.Component;

@SuppressWarnings("serial")
public class EditableTextPanel extends JPanel implements KeyListener {

	private static final boolean debug = true;
	private static final int PANEL_PADDING = 5;
	public final Color defaultColor = getBackground();

	private final Component component;

	private FocusListener descriptionFocusListener;

	private JTextArea content;
	private JTextArea editableContent;
	private String contentText;
	private JButton endEditingButton;

	private ActionListener endEditingListener;

	private ArrayList<ActionListener> changeListeners = new ArrayList<ActionListener>();

	public EditableTextPanel(Component component) {

		this.component = component;

		new ActionListener() {//TODO why is this here? /luke

			public void actionPerformed(ActionEvent e) {
				setContent(editableContent.getText());
				editableContent.setVisible(false);
				content.setVisible(true);
				endEditingButton.setVisible(true);
			}
		};

		descriptionFocusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
			}
			@Override
			public void focusLost(FocusEvent e) {
				releaseFocusAction();
			}
		};

		// add a content panel under the top panel, and first set the layout
		BorderLayout descriptionLayout = new BorderLayout();
		setLayout(descriptionLayout);

		setOpaque(false);
		setBorder(new EmptyBorder(new Insets(PANEL_PADDING, PANEL_PADDING,
				PANEL_PADDING, PANEL_PADDING)));

		contentText = component.getDescription();

		content = new JTextArea(contentText);
		content.setBackground(defaultColor);
		content.setLineWrap(true);
		content.setWrapStyleWord(true);
		content.setEditable(false);

		editableContent = new JTextArea(contentText);
		editableContent.setBackground(Color.WHITE);
		editableContent.setLineWrap(true);
		editableContent.setWrapStyleWord(true);
		editableContent.setEditable(true);
		editableContent.setVisible(false);

		endEditingButton = new JButton("Save");
		endEditingButton.setActionCommand("End content editing");
		endEditingButton.addActionListener(endEditingListener);
		endEditingButton.setVisible(false);

		add(content, BorderLayout.PAGE_START);
		add(editableContent, BorderLayout.CENTER);

		JPanel saveButtonPanel = new JPanel();
		JPanel saveButtonInnerPanel = new JPanel();

		saveButtonPanel.setLayout(new BorderLayout());
		saveButtonPanel.add(saveButtonInnerPanel, BorderLayout.AFTER_LINE_ENDS);

		add(saveButtonPanel, BorderLayout.AFTER_LAST_LINE);
		saveButtonInnerPanel.add(endEditingButton);

		// TODO change the layout to cardlayout, creating the cards as on
		// http://download.oracle.com/javase/tutorial/uiswing/layout/card.html

		setBackground(Color.WHITE);

		content.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (!EditableTextPanel.this.component.getLockedStatus()) {
						
						editableContent.setVisible(true);
						//Must proceed setting content invisible to prevent focus being lost from this component
						editableContent.requestFocusInWindow();	
						content.setVisible(false);
						
						endEditingButton.setVisible(true);	
					}
				}
			}
		});

		// editableContent.addActionListener(descriptionListener); //
		// useless: not a text field anymore
		editableContent.addFocusListener(descriptionFocusListener);

	}

	protected void releaseFocusAction() {
		setContent(editableContent.getText());
		editableContent.setVisible(false);
		content.setVisible(true);
		endEditingButton.setVisible(false);
	}

	public void setContent(String text) {

		this.contentText = text;
		content.setText(text);
		editableContent.setText(text);
	}

	public void registerActionListener(ActionListener listener) {

		this.changeListeners.add(listener);
	}

	protected void notifyActionListeners() {

		for (ActionListener listener : changeListeners) {

			listener.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, "Component Contents Changed"));
		}
	}

	public String getDescription() {
		return this.contentText;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO possibly http://www.devx.com/tips/Tip/13547
		if (debug) System.out.println("Key pressed.");
		int code = e.getKeyCode(); 
		if(code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_CAPS_LOCK) 
		{ 
			if (debug) System.out.println("ESC pressed.");
			releaseFocusAction();
		} 
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}