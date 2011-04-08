package org.u_compare.gui;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/*
 * Good read: TODO:
 * 
 * When the user is resizing the Components the minimum size of the Components
 * is used to determine the maximum/minimum position the Components can be set
 * to. If the minimum size of the two components is greater than the size of
 * the split pane the divider will not allow you to resize it. To alter the
 * minimum size of a JComponent, see
 * JComponent.setMinimumSize(java.awt.Dimension). 
 */

/**
 * XXX: TODO:
 * 
 * @author 	luke
 * @author 	pontus
 * @version 2010-12-04
 *
 */
@SuppressWarnings("serial")
public class UConnectHorizontalSplitPane extends JSplitPane {
	
	private static final boolean DEBUG = false;
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	/* By default, distribute the new size evenly between our components */
	private static final double DEFAULT_RE_SIZE_WEIGHT = 0.5D;
	private static final double DIVIDER_START_POSITION = 0.5D;
	private static final int SPLIT_ORIENTATION = JSplitPane.HORIZONTAL_SPLIT;
	
	public UConnectHorizontalSplitPane(JComponent workflowPane,
			JComponent libraryPane) {
		
		this.setLeftComponent(workflowPane);
		this.setRightComponent(libraryPane);
		
		this.setOneTouchExpandable(UConnectHorizontalSplitPane.ONE_TOUCH_EXPANDABLE);
		
		this.setUI(new BasicSplitPaneUI() {
		    public BasicSplitPaneDivider createDefaultDivider() {
		        return new BasicSplitPaneDivider(this) {
		            public void setBorder(Border b) {
		            }
		        };
		    }
		});
		
		// Carry out all the divider configurations here
		this.setDividerSize(5);
		// Space distribution between components when we grow
		this.setResizeWeight(UConnectHorizontalSplitPane.DEFAULT_RE_SIZE_WEIGHT);
		this.setOrientation(UConnectHorizontalSplitPane.SPLIT_ORIENTATION);
		// Set the divider as centred later when everything else is set
		// XXX: This is one hell of a hack! There has to be a better way!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (DEBUG) {
					System.err.println(this.getClass().getName()
							+ "our current size" + getSize());
				}
				// Do we know our size yet? If not, wait a little more.
				if (getSize().getHeight() == 0 && getSize().getWidth() == 0) {
					SwingUtilities.invokeLater(this);
				}
				else {
					// If we do we can set the divider location
					setDividerLocation(
							UConnectHorizontalSplitPane.DIVIDER_START_POSITION);
				}
			}
		});
		
		this.setContinuousLayout(true);
	}
}
