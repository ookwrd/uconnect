package org.u_compare.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;

/**
 * XXX: TODO:
 * 
 * @author 	olaf
 * @version	2010-12-04
 */
public class DnDVerticalScrollerPanel extends MouseInputAdapter
		implements MouseMotionListener, ActionListener{

	private static final int SPEED = 10;
	
	Timer scroller;
	
	public DnDVerticalScrollerPanel(){
		scroller = new Timer(DnDVerticalScrollerPanel.SPEED, this);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		scroller.stop();
	    Point pt = e.getPoint();
	    System.err.println("Mouse at: " + pt.x + "," + pt.y);
	    scroller.start();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.err.println("Mouse moving");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO: Scroll the window
		System.err.println("Imagine I was scrolling");
	}

}
