package org.u_compare.gui;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

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
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = false;
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

		this.setContinuousLayout(true);
	}
	
	@Override
	public void setPreferredSize(Dimension d){
		super.setSize(d);
		
		setDividerLocation(
				UConnectHorizontalSplitPane.DIVIDER_START_POSITION);
	}
	
}
