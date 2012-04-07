package org.u_compare.gui;

import javax.swing.JSplitPane;

/**
 * Panel holding everything related to a single workflow. Holds the WorkflowPane
 * (above) and the ConsolePane (Below).
 * 
 * @author pontus
 * @author luke
 * @version 2009-08-28
 */
@SuppressWarnings("serial")
public class WorkflowHorizontalSplitPane extends JSplitPane {

	private ConsolePane consolePane;
	private WorkflowPane workflowPane;

	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	/* By default, distribute the new size evenly between our components */
	private static final double DEFAULT_RE_SIZE_WEIGHT = 0.85D;
	private static final int SPLIT_ORIENTATION = JSplitPane.VERTICAL_SPLIT;
	protected static final boolean DEBUG = false;

	public WorkflowHorizontalSplitPane(WorkflowPane workflowPane,
			ConsolePane consolePane) {
		this.workflowPane = workflowPane;
		this.consolePane = consolePane;

		this.setTopComponent(this.workflowPane);
		this.setBottomComponent(this.consolePane);

		this.setOneTouchExpandable(WorkflowHorizontalSplitPane.ONE_TOUCH_EXPANDABLE);
		// Carry out all the divider configurations here
		this.setDividerSize(5);
		// Space distribution between components when we grow
		this.setResizeWeight(WorkflowHorizontalSplitPane.DEFAULT_RE_SIZE_WEIGHT);
		this.setOrientation(WorkflowHorizontalSplitPane.SPLIT_ORIENTATION);

		this.setContinuousLayout(true);
	}

	public WorkflowPane getWorkflowPane() {
		return this.workflowPane;
	}

}