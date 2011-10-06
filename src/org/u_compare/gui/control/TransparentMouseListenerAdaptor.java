package org.u_compare.gui.control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

public class TransparentMouseListenerAdaptor implements MouseMotionListener {

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("Here in the listenrer dragging");
		for(MouseMotionListener listener : e.getComponent().getParent().getMouseMotionListeners()){
			listener.mouseDragged(SwingUtilities.convertMouseEvent(e.getComponent(), e, e.getComponent().getParent()));
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("Here in the listenrer moving");
		for(MouseMotionListener listener : e.getComponent().getParent().getMouseMotionListeners()){
			listener.mouseMoved(SwingUtilities.convertMouseEvent(e.getComponent(), e, e.getComponent().getParent()));
		}	
	}

}
