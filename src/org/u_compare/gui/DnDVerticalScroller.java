package org.u_compare.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;


public class DnDVerticalScroller extends MouseInputAdapter implements MouseMotionListener, ActionListener{

	private static final int SPEED = 2;
	
	Timer scroller;
	
	JViewport viewport;
	
	public DnDVerticalScroller(JViewport viewport){
		this.viewport = viewport;
		
		viewport.addMouseMotionListener(this);
		
		scroller = new Timer(10,this);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		scroller.stop();
	    Point pt = e.getPoint();
	    
	    System.out.println("Mouse at: " + pt.x + "," + pt.y);
	    
	    //Set to scroll upwards
	    if(true){
	    	
	    }else if (false){//Set to scroll downwards
	    	
	    }
	    //move.setLocation(SPEED*(pt.x-startPt.x), SPEED*(pt.y-startPt.y));
	    //startPt.setLocation(pt);
	    scroller.start();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		System.out.println("Mouse moving");
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//TODO scroll the window
		
		System.out.println("Imagine I was scrolling");
		
	}

}
