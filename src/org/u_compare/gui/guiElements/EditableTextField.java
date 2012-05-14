package org.u_compare.gui.guiElements;

import java.awt.Color;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.border.EtchedBorder;

/**
 * Extended version of JTextField which changes appearance when not being
 * edited.
 * 
 * See also EditableTextPanel
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class EditableTextField extends AutoscrollTextField {

	private boolean fixed;
	private DragGestureRecognizer recognizer;

	public EditableTextField(String text) {
		super(text, 500);
		toFixedMode();

		// Double click component to enter edit mode
		addDoubleClick();

		// Selecting something else cancels edit mode
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				toFixedMode();
			}
		});

		// Finishing editing ends editable mode.
		super.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toFixedMode();
			}
		});

		// "ESC" to cancel editable mode.
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode();
				if (code == KeyEvent.VK_ESCAPE) {
					e.consume();
					toFixedMode();
				}
			}
		});
	}

	private void addDoubleClick(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (isEnabled()) {
						toEditMode();
						removeMouseListener(this);
						
						if(recognizer != null){
							recognizer.setComponent(null);
						}
					}
				}
			}
		});
	}
	
	/**
	 * Change to editable mode.
	 */
	protected void toEditMode() {
		fixed = false;
		setEditable(true);
		super.setBackground(Color.WHITE);
		setBorder(new EtchedBorder());

		// Fix java bug where Caret becomes invisible if
		// JTextArea.setEditable(false);
		setCaretPosition(getText().length());
		getCaret().setVisible(true);
	}

	/**
	 * Change to non-editable mode.
	 */
	protected void toFixedMode() {
		if (fixed) {
			return;
		}
		fixed = true;
		setEditable(false);
		super.setBackground(null);
		setBorder(null);
		if (hasFocus()) {
			transferFocus();
			// TODO Not relevant to Uconnect, but the focus should probably be
			// transfered to somewhere where it won't do anything.
		}
		addDoubleClick();
		
		if(recognizer != null){
			recognizer.setComponent(this);
		}
		
		fireActionPerformed();
	}
	
	public void setDragGestureRecognizer(DragGestureRecognizer recognizer){
		this.recognizer = recognizer;
	}
}
