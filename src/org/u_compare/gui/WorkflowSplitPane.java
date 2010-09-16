package org.u_compare.gui;

import javax.swing.JSplitPane;

/**
 * Holds the workflow pane and the console pane.
 * 
 * @author pontus
 * @version 2009-08-28
 */
@SuppressWarnings("serial")
public class WorkflowSplitPane extends JSplitPane {
	private WorkflowConsolePane consolePane;
	private WorkflowPane workflowPane;
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	private static final double SEPARATOR_START_LOCATION_FROM_BOTTOM = 0.9D;
	private static final int SPLIT_ORIENTATION = JSplitPane.VERTICAL_SPLIT;
	
	public WorkflowSplitPane(WorkflowPane workflowPane,
			WorkflowConsolePane consolePane) {
		this.workflowPane = workflowPane;
		this.consolePane = consolePane;

		this.setTopComponent(this.workflowPane);
		this.setBottomComponent(this.consolePane);
		
		this.setOneTouchExpandable(WorkflowSplitPane.ONE_TOUCH_EXPANDABLE);
		this.setDividerLocation(
				WorkflowSplitPane.SEPARATOR_START_LOCATION_FROM_BOTTOM);
		this.setOrientation(WorkflowSplitPane.SPLIT_ORIENTATION);
	}
	
	protected WorkflowPane getWorkflowPane() {
		return this.workflowPane;
	}
}
