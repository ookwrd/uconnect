package org.u_compare.gui;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.DescriptionChangeListener;
import org.u_compare.gui.model.Component.SavedStatusChangeListener;

/**
 * Holds the WorkflowPane and the ConsolePane .
 * 
 * @author pontus
 * @author luke
 * @version 2009-08-28
 */
@SuppressWarnings("serial")
public class WorkflowHorizontalSplitPane extends JSplitPane
	implements DescriptionChangeListener, SavedStatusChangeListener {
	
	private ConsolePane consolePane;
	private WorkflowPane workflowPane;
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	/* By default, distribute the new size evenly between our components */
	private static final double DEFAULT_RE_SIZE_WEIGHT = 0.85D;
	private static final int SPLIT_ORIENTATION = JSplitPane.VERTICAL_SPLIT;
	protected static final boolean DEBUG = false;
	
	private IconizedCloseableTabFlapComponent tab;
	private JTabbedPane tabPane;
	
	public WorkflowHorizontalSplitPane(WorkflowPane workflowPane,
			ConsolePane consolePane) {
		this.workflowPane = workflowPane;
		this.consolePane = consolePane;
		
		workflowPane.getTopWorkflowComponent().getComponent()
				.registerComponentDescriptionChangeListener(this);
		workflowPane.getTopWorkflowComponent().getComponent()
				.registerSavedStatusChangeListener(this);
		
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

	//Should be called if we have been added to a tabbed pane.
	public void linkTabbedPane(JTabbedPane tabPane,
			IconizedCloseableTabFlapComponent tab) {
		this.tab = tab;
		this.tabPane = tabPane;
	}
	
	// I am not sure if this really belongs here, not in the tabbed pane /pontus
	//TODO refactor to tabbed pane /luke
	@Override
	public void ComponentDescriptionChanged(Component component) {
		if(tab != null) {
			tabPane.setTitleAt(tabPane.indexOfComponent(this),
					(component.checkUnsavedChanges() ? "*" : "")
					+ WorkflowViewer.cleanTitle(component.getName()));
		}
	}

	// I am not sure if this really belongs here, not in the tabbed pane /pontus
	//TODO refactor to tabbed pane /luke
	@Override
	public void savedStatusChanged(Component component) {
		System.out.println("Saved change listner notified");
		if (tab != null) {
			if(component.checkUnsavedChanges()) {
				tabPane.setTitleAt(tabPane.indexOfComponent(this),
						"*" +
						WorkflowViewer.cleanTitle(component.getName()));
			} else {
				tabPane.setTitleAt(tabPane.indexOfComponent(this),
						WorkflowViewer.cleanTitle(component.getName()));
			}
		}
	}

}