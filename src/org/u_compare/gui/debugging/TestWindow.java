package org.u_compare.gui.debugging;

import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Used as a shorthand to test GUI components in small steps.
 * 
 * @author pontus
 * @version 2009-08-27
 */
@SuppressWarnings("serial")
public class TestWindow extends JFrame {

	private static final Dimension PREFERRED_SIZE = new Dimension(960, 640);
	private static final Dimension MINIMUM_SIZE = new Dimension(48, 64);
	
	/**
	 * @param testTitle Title of the test window
	 * @param toDisplay Container to display in the test window 
	 */
	public TestWindow(String testTitle, Container toDisplay) {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(TestWindow.PREFERRED_SIZE);
        this.setMinimumSize(TestWindow.MINIMUM_SIZE);
        
        this.setTitle(testTitle);
        
        this.setContentPane(toDisplay);
        this.pack();
        this.setVisible(true);
	}
}