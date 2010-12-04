package org.u_compare.gui.debugging;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Used as a shorthand to test GUI components in small steps.
 * 
 * @author pontus
 * @version 2009-08-27
 */
@SuppressWarnings("serial")
public class TestWindow extends JFrame {

	/* It is pretty safe to assume that HD 720 is okay in todays world */
	private static final Dimension PREFERRED_SIZE = new Dimension(1280, 720);
	/* 
	 * No modern computer won't support VGA resolution, so this is a safe
	 * minimum and saves us from absurdities.
	 */
	private static final Dimension MINIMUM_SIZE = new Dimension(640, 480);
	
	private static final boolean DEBUG = false;
	
	/**
	 * @param testTitle Title of the test window
	 * @param toDisplay Container to display in the test window 
	 */
	public TestWindow(String testTitle, Container toDisplay) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(TestWindow.PREFERRED_SIZE);
        this.setMinimumSize(TestWindow.MINIMUM_SIZE);
        
        this.setTitle(testTitle);
        toDisplay.setPreferredSize(TestWindow.PREFERRED_SIZE);
        toDisplay.setMinimumSize(TestWindow.MINIMUM_SIZE);
        this.setContentPane(toDisplay);
        this.pack();
        
        //TODO: Do copy this into our non-test window class when it comes around
        
        /* Centre the window on screen, this is hell if you have multiple
         * monitors since the standard setLocationRelativeTo(null) won't work.
         */
        
        //TODO: We could use a full-screen if we wanted to, just to show off
        
        GraphicsEnvironment graphicsEnvironment =
        	GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        // Check so that we actually have a graphical environment to render in
        if (graphicsEnvironment.isHeadlessInstance()) {
        	System.err.println("ERROR: No graphics environment found, this " +
        			"application can not run in a command-line environment");
        	System.exit(-1);
        }
        
        GraphicsDevice graphicsDevice =
        	graphicsEnvironment.getDefaultScreenDevice();
        if (TestWindow.DEBUG) {
        	System.err.println(this.getClass().getName() +
        			": Will center window on device: "
        			+ graphicsDevice.getIDstring());
        }
        
        GraphicsConfiguration graphicsConfiguration =
        	graphicsDevice.getDefaultConfiguration();
        Rectangle bounds = graphicsConfiguration.getBounds();
        if (TestWindow.DEBUG) {
        	System.err.println(this.getClass().getName() +
        			": Device default configuration bounds: " + bounds);
        }
        
        // Verify that we can fit the MINIMUM window on screen
        if (MINIMUM_SIZE.getWidth() > bounds.getWidth()
        		|| MINIMUM_SIZE.getHeight() > bounds.getHeight()) {
        	// At this stage we a graphical environment to display the error
        	JOptionPane.showMessageDialog(null,
        			"We require at least resolution of "
        			+ (int) MINIMUM_SIZE.getWidth() + "x"
        			+ (int) MINIMUM_SIZE.getHeight() + ", you are using "
        			+ (int) bounds.getWidth() + "x"
        			+ (int) bounds.getHeight(),
        			"Error", JOptionPane.ERROR_MESSAGE);
        	System.exit(-1);
        }
        
        int xPos = (int) Math.max(0,
        		(bounds.getWidth() - this.getWidth()) / 2);
        int yPos = (int) Math.max(0,
        		(bounds.getHeight() - this.getHeight()) / 2); 
        
        if (TestWindow.DEBUG) {
        	System.err.println(this.getClass().getName()
        			+ ": Will center on co-ordinates x: " + xPos
        			+ " y: " + yPos);
        }
        
        // Center us on this device
        this.setLocation(xPos, yPos);
	}
}