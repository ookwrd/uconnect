package org.u_compare.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 * GUI Component containing the WorkflowPane/WorkflowViewer on the left and the Library on the right separated by a splitPane.
 * 
 * @author 	luke
 * @author 	pontus
 * @version 2010-12-04
 *
 */
@SuppressWarnings("serial")
public class UConnectVerticalSplitPane extends JSplitPane {
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = false;
	/* By default, distribute the new size evenly between our components */
	private static final double DEFAULT_RE_SIZE_WEIGHT = 0.5D;
	private static final double DIVIDER_START_POSITION = 0.5D;
	private static final int SPLIT_ORIENTATION = JSplitPane.HORIZONTAL_SPLIT;
	
	/**
	 * 
	 * @param workflow Either a workflowPane or WorkflowViewer depending on whether the current configuration allows multiple workflow tabs.
	 * @param libraryPane
	 */
	public UConnectVerticalSplitPane(JComponent workflow,
			JComponent libraryPane) {
		
		this.setLeftComponent(workflow);
		this.setRightComponent(libraryPane);
		
		this.setOneTouchExpandable(UConnectVerticalSplitPane.ONE_TOUCH_EXPANDABLE);
		
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
		this.setResizeWeight(UConnectVerticalSplitPane.DEFAULT_RE_SIZE_WEIGHT);
		this.setOrientation(UConnectVerticalSplitPane.SPLIT_ORIENTATION);

		this.setContinuousLayout(true);
	}
	
	@Override
	public void setPreferredSize(Dimension d){
		super.setSize(d);
		
		setDividerLocation(
				UConnectVerticalSplitPane.DIVIDER_START_POSITION);
	}
	
}
