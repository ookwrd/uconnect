package org.u_compare.gui.guiElements;

import java.awt.Insets;
import java.awt.Point;
import java.awt.dnd.Autoscroll;

import javax.swing.JTextArea;

/**
 * Extended JTextArea that does not catch Autoscroll events, but instead passes
 * them up to higher level Autoscroll components.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class AutoscrollTextArea extends JTextArea implements Autoscroll {

	private AutoScrollSupport support = new AutoScrollSupport(this);
	
	public AutoscrollTextArea(String text){
		super(text);
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
