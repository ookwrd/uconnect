package org.u_compare.gui;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class ButtonTabFlap extends JButton {

	/**
	 * Create a ButtonTab.
	 * 
	 * @param buttonText
	 */
	public ButtonTabFlap(String buttonText){
	
		super(buttonText);

		setOpaque(false);
		setBorder(new EmptyBorder(0,0,0,0));
		
	}
	
	/**
	 * Create ButtonTab that acts as a DropTarget.
	 * 
	 * @param string
	 * @param dropListener
	 */
	public ButtonTabFlap(String buttonText, DropTargetListener dropListener){
		
		this(buttonText);
		
		new DropTarget(this, dropListener);
		
	}

}
