package org.u_compare.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Used as a the top level application JFrame.
 * 
 * @author pontus
 * @author Luke McCrohon
 * @version 2009-08-27
 */
@SuppressWarnings("serial")
public class UCompareWindow extends JFrame {

	/* It is pretty safe to assume that HD 720 is okay in todays world */
	private static final Dimension PREFERRED_SIZE = new Dimension(1280, 720);
	private static final String PREF_WIDTH = "pref width";
	private static final String PREF_HEIGHT = "pref height";
	
	/* 
	 * No modern computer won't support VGA resolution, so this is a safe
	 * minimum and saves us from absurdities.
	 */
	private static final Dimension MINIMUM_SIZE = new Dimension(640, 480);
	
	private static final boolean DEBUG = false;
	
	/**
	 * @param windowTitle Title of the test window
	 * @param toDisplay Container to display in the test window 
	 */
	public UCompareWindow(String windowTitle, Container toDisplay) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setTitle(windowTitle);
        
        final Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				prefs.putInt(PREF_WIDTH, getSize().width);
				prefs.putInt(PREF_HEIGHT, getSize().height);
			}
		});
        
        int prefHeight = prefs.getInt(UCompareWindow.PREF_HEIGHT, UCompareWindow.PREFERRED_SIZE.height);
        int prefWidth = prefs.getInt(UCompareWindow.PREF_WIDTH, UCompareWindow.PREFERRED_SIZE.width);
        
        Dimension prefSize = new Dimension(prefWidth, prefHeight);

        this.setPreferredSize(prefSize);
        this.setMinimumSize(UCompareWindow.MINIMUM_SIZE);
        
        toDisplay.setPreferredSize(getPreferredSize());
        toDisplay.setMinimumSize(getMaximumSize());
        this.setContentPane(toDisplay);
        this.pack();
   
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
        if (UCompareWindow.DEBUG) {
        	System.err.println(this.getClass().getName() +
        			": Will center window on device: "
        			+ graphicsDevice.getIDstring());
        }
        
        GraphicsConfiguration graphicsConfiguration =
        	graphicsDevice.getDefaultConfiguration();
        Rectangle bounds = graphicsConfiguration.getBounds();
        if (UCompareWindow.DEBUG) {
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
        
        if (UCompareWindow.DEBUG) {
        	System.err.println(this.getClass().getName()
        			+ ": Will center on co-ordinates x: " + xPos
        			+ " y: " + yPos);
        }
        
        // Center us on this device
        this.setLocation(xPos, yPos);
	}
}