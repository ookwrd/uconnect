package org.u_compare.gui;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.u_compare.gui.library.LibraryPane;

@SuppressWarnings("serial")
public class UConnectSplitPane extends JSplitPane {

	private WorkflowTabbedPane workflowPane;
	private LibraryPane libraryPane;
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	private static final int SEPARATOR_START_LOCATION_FROM_LEFT = 480;
	private static final int SPLIT_ORIENTATION = JSplitPane.HORIZONTAL_SPLIT;
	
	public UConnectSplitPane(WorkflowTabbedPane workflowPane,
			LibraryPane libraryPane) {
		
		this.workflowPane = workflowPane;
		this.libraryPane = libraryPane;
		
		this.setLeftComponent(workflowPane);
		this.setRightComponent(libraryPane);
		
		this.setOneTouchExpandable(UConnectSplitPane.ONE_TOUCH_EXPANDABLE);
		this.setDividerLocation(
				UConnectSplitPane.SEPARATOR_START_LOCATION_FROM_LEFT);
		this.setOrientation(UConnectSplitPane.SPLIT_ORIENTATION);
		
		
		this.setUI(new BasicSplitPaneUI() {
		    public BasicSplitPaneDivider createDefaultDivider() {
		        return new BasicSplitPaneDivider(this) {
		            public void setBorder(Border b) {
		            }
		        };
		    }
		});
		
		this.setDividerSize(5);

	}
}
