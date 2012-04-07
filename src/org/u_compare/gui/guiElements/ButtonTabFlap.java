package org.u_compare.gui.guiElements;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

/**
 * A JButton implementation customized to be used as a tab on the extended
 * JTabbedPane implemented by ButtonTabbedPane.
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class ButtonTabFlap extends JButton {

	/**
	 * Create a ButtonTab.
	 * 
	 * @param buttonText
	 */
	public ButtonTabFlap(String buttonText) {
		super(buttonText);

		setOpaque(false);
		setContentAreaFilled(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	/**
	 * Create ButtonTab that acts as a DropTarget.
	 * 
	 * @param string
	 * @param dropListener
	 */
	public ButtonTabFlap(String buttonText, DropTargetListener dropListener) {
		this(buttonText);
		if(dropListener != null){
			new DropTarget(this, dropListener);
		}
	}

}
