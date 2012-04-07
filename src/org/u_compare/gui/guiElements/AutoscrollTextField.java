package org.u_compare.gui.guiElements;

import java.awt.Insets;
import java.awt.Point;
import java.awt.dnd.Autoscroll;

import javax.swing.JTextField;

/**
 * Extended JTextField that does not catch Autoscroll events, but instead passes
 * them up to higher level Autoscroll components.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class AutoscrollTextField extends JTextField implements Autoscroll {

	private AutoScrollSupport support = new AutoScrollSupport(this);

	public AutoscrollTextField(String text) {
		super(text);
	}

	public AutoscrollTextField(String text, int length) {
		super(text, length);
	}

	@Override
	public void autoscroll(Point cursorLocn) {
		support.autoscroll(cursorLocn);
	}

	@Override
	public Insets getAutoscrollInsets() {
		return support.getAutoscrollInsets();
	}

}
