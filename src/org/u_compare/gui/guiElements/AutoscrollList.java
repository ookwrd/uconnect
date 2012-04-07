package org.u_compare.gui.guiElements;

import java.awt.Insets;
import java.awt.Point;
import java.awt.dnd.Autoscroll;

import javax.swing.JList;
import javax.swing.ListModel;

/**
 * Extended JList that does not catch Autoscroll events, but instead passes them
 * up to higher level Autoscroll components.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class AutoscrollList extends JList implements Autoscroll {

	private AutoScrollSupport support = new AutoScrollSupport(this);

	public AutoscrollList(ListModel model) {
		super(model);
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
