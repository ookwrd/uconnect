package org.u_compare.gui.guiElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

/**
 * Behaves similar to a JTextArea except displays differently when not being edited.
 * 
 * See also EditableTextField
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class EditableTextPanel extends JPanel {
	
	private static final int PANEL_PADDING = 8;
	public final Color defaultColor = getBackground();

	private JTextArea content;
	private JButton endEditingButton;

	/**
	 * Create a new EditableTextPanel with the string "text" as the initial contents.
	 * 
	 * @param text
	 */
	public EditableTextPanel(String text) {
		
		setLayout(new BorderLayout());
		setOpaque(false);
		setBorder(new EmptyBorder(new Insets(PANEL_PADDING, PANEL_PADDING, 0,
				PANEL_PADDING)));

		content = new JTextArea(text);
		content.setBackground(defaultColor);
		content.setLineWrap(true);
		content.setWrapStyleWord(true);
		content.setEditable(false);

		content.setDragEnabled(false);
		
		endEditingButton = new JButton("Done");
		endEditingButton.setVisible(false);

		add(content, BorderLayout.PAGE_START);

		JPanel saveButtonPanel = new JPanel();
		JPanel saveButtonInnerPanel = new JPanel();
		saveButtonInnerPanel.setLayout(new BorderLayout());
		
		saveButtonPanel.setLayout(new BorderLayout());
		saveButtonPanel.add(saveButtonInnerPanel, BorderLayout.AFTER_LINE_ENDS);

		add(saveButtonPanel, BorderLayout.AFTER_LAST_LINE);
		saveButtonInnerPanel.add(endEditingButton);

		//Double click component to enter edit mode
		content.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (isEnabled()) {
						toEditMode();
					}
				}
			}
		});
		
		//"ESC" to cancel editable mode.
		content.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode(); 
				if(code == KeyEvent.VK_ESCAPE) { 
					e.consume();
					toFixedMode();
				} 
			}
		});

		//Selecting something else cancels edit mode
		content.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				toFixedMode();
			}
		});
	}

	/**
	 * Switch to editable mode.
	 */
	protected void toEditMode(){
		content.setEditable(true);
		content.setBackground(Color.WHITE);
		content.setBorder(new EtchedBorder());

		//Fix java bug where Caret becomes invisible if JTextArea.setEditable(false);
		content.setCaretPosition(content.getText().length());
		content.getCaret().setVisible(true);
		
		endEditingButton.setVisible(true);
	}
	
	/**
	 * Switch to non-editable mode.
	 */
	protected void toFixedMode(){
		content.setEditable(false);
		content.setBackground(defaultColor);
		content.setBorder(null);
		endEditingButton.setVisible(false);
		if(content.hasFocus()){
			requestFocusInWindow();
		}
		notifyActionListeners();
	}

	public void setContentText(String text) {
		content.setText(text);
	}

	public String getContentText() {
		return content.getText();
	}
	
	public JTextArea getContent(){
		return content;
	}

	private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	
	public void registerActionListener(ActionListener listener){
		actionListeners.add(listener);
	}
	
	protected void notifyActionListeners(){
		for(ActionListener listener : actionListeners){
			listener.actionPerformed(new ActionEvent(this, 1, getContentText()));
		}
	}
}