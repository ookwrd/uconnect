package org.u_compare.gui.guiElements;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class EditableTextField extends JTextField {

	private ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
	
	private boolean fixed;
	
	public EditableTextField(String text){
		this(text, 30);
	}
	
	public EditableTextField(String text, int length){
		super(text, length);
		toFixedMode();
		
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					if (isEnabled()) {
						toEditMode();
					}
				}
			}
		});
		
		addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				toFixedMode();
			}
		});
		
		super.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toFixedMode();
			}
		});
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int code = e.getKeyCode(); 
				if(code == KeyEvent.VK_ESCAPE) { 
					e.consume();
					toFixedMode();
				} 
			}
		});
	}
	
	protected void toEditMode(){
		fixed = false;
		setEditable(true);
		super.setBackground(Color.WHITE);
		setBorder(new EtchedBorder());
		
		//Fix java bug where Caret becomes invisible if JTextArea.setEditable(false);
		setCaretPosition(getText().length());
		getCaret().setVisible(true);
	}
	
	protected void toFixedMode(){
		if(fixed){
			return;
		}
		fixed = true;
		setEditable(false);
		super.setBackground(null);
		setBorder(null);
		if(hasFocus()){
			transferFocus();//TODO transfer the focus somewhere where it won't do anything.
		}
		notifyActionListeners();
	}

	@Override
	public void addActionListener(ActionListener listener){
		actionListeners.add(listener);
	}
	
	protected void notifyActionListeners(){
		for(ActionListener listener : actionListeners){
			listener.actionPerformed(new ActionEvent(this, 1, getText()));//TODO what sprt of action event should I really be returning?
		}
	}
	
}
