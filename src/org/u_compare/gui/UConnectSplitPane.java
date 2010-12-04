package org.u_compare.gui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

@SuppressWarnings("serial")
public class UConnectSplitPane extends JSplitPane
		implements ComponentListener {

	private JComponent workflowPane;
	private JComponent libraryPane;
	
	private static final boolean DEBUG = false;
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	private static final double INITIIAL_DIVIDER__PREFERENCE = 0.5;
	private static final int SPLIT_ORIENTATION = JSplitPane.HORIZONTAL_SPLIT;
	
	private double dividerLocationPreference;
	
	public UConnectSplitPane(JComponent workflowPane,
			JComponent libraryPane) {
		
		this.workflowPane = workflowPane;
		this.libraryPane = libraryPane;
		
		this.setLeftComponent(workflowPane);
		this.setRightComponent(libraryPane);
		
		this.setOneTouchExpandable(UConnectSplitPane.ONE_TOUCH_EXPANDABLE);
		
		this.setUI(new BasicSplitPaneUI() {
		    public BasicSplitPaneDivider createDefaultDivider() {
		        return new BasicSplitPaneDivider(this) {
		            public void setBorder(Border b) {
		            }
		        };
		    }
		});
		
		// Do all the divider work here
		this.setDividerSize(5);
		
		// We can not determine this yet, wait for a re-size update
		this.dividerLocationPreference =
			UConnectSplitPane.INITIIAL_DIVIDER__PREFERENCE;
		this.setOrientation(UConnectSplitPane.SPLIT_ORIENTATION);
		
		/* Add a property change listener to maintain the user set ratio for
		 * the divider
		 */
		//TODO: Extract this class since it is general enough
		this.addPropertyChangeListener(
				"dividerLocation",
				new PropertyChangeListener() {
					private static final boolean DEBUG = false;
					
					public void propertyChange(PropertyChangeEvent e) {
						Number value = (Number) e.getNewValue();
						
						if (DEBUG) {
							System.err.println(this.getClass().getName()
									+ "relative divider position before update "
									+ dividerLocationPreference);
						}
						
						// Depending on our split we do this differently
						if (getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
							dividerLocationPreference =
								value.doubleValue() / getWidth();
						}
						else if (getOrientation()
								== JSplitPane.VERTICAL_SPLIT) {
							dividerLocationPreference =
								value.doubleValue() / getHeight();
						}
						else {
							assert false: "given unknown split constant";
						}
						
						if (DEBUG) {
							System.err.println(this.getClass().getName()
									+ "relative divider position after update "
									+ dividerLocationPreference);
						}
					}
				});
		
		this.setContinuousLayout(true);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// We will not act upon this
		return;
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// We will not act upon this
		return;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		Dimension currentSize = this.getSize();
		
		if (UConnectSplitPane.DEBUG) {
			System.err.println(this.getClass().getName()
					+ ": updating divider current size " + currentSize);
		}
		
		// We need to re-adjust our properties
		this.setDividerLocation(this.dividerLocationPreference);
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// We will not act upon this
		return;
	}
}
