package org.u_compare.gui;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.DescriptionChangeListener;
import org.u_compare.gui.model.SavedStatusChangeListener;

/**
 * Holds the workflow pane and the console pane.
 * 
 * @author pontus
 * @author luke
 * @version 2009-08-28
 */
@SuppressWarnings("serial")
public class WorkflowSplitPane extends JSplitPane
	implements DescriptionChangeListener, SavedStatusChangeListener {
	
	private ConsolePane consolePane;
	private WorkflowPane workflowPane;
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	private static final double SEPARATOR_START_LOCATION_FROM_BOTTOM = 0.2D;
	private static final int SPLIT_ORIENTATION = JSplitPane.VERTICAL_SPLIT;
	
	private IconizedCloseableTabFlapComponent tab;
	private JTabbedPane tabPane;
	
	public WorkflowSplitPane(WorkflowPane workflowPane,
			ConsolePane consolePane) {
		this.workflowPane = workflowPane;
		this.consolePane = consolePane;
		
		workflowPane.getTopWorkflowComponent().getComponent().registerComponentDescriptionChangeListener(this);
		workflowPane.getTopWorkflowComponent().getComponent().registerSavedStatusChangeListener(this);
		
		this.setTopComponent(this.workflowPane);
		this.setBottomComponent(this.consolePane);
		
		this.setOneTouchExpandable(WorkflowSplitPane.ONE_TOUCH_EXPANDABLE);
		this.setDividerLocation(
				WorkflowSplitPane.SEPARATOR_START_LOCATION_FROM_BOTTOM);
		this.setOrientation(WorkflowSplitPane.SPLIT_ORIENTATION);
		this.setDividerLocation(500); //XXX: Nasty, but we need the outer size
	}
	
	protected WorkflowPane getWorkflowPane() {
		return this.workflowPane;
	}

	//Should be called if we have been added to a tabbed pane.
	public void linkTabbedPane(JTabbedPane tabPane,
			IconizedCloseableTabFlapComponent tab) {
		this.tab = tab;
		this.tabPane = tabPane;
	}
	
	@Override
	public void ComponentDescriptionChanged(Component component) {
		if(tab != null) {
			tabPane.setTitleAt(tabPane.indexOfComponent(this),
					WorkflowTabbedPane.cleanTitle(component.getName())
					+ (component.checkUnsavedChanges() ? "*" : ""));
		}
	}

	@Override
	public void savedStatusChanged(Component component) {
		System.out.println("Saved change listner notified");
		if (tab != null) {
			if(component.checkUnsavedChanges()) {
				tabPane.setTitleAt(tabPane.indexOfComponent(this),
						WorkflowTabbedPane.cleanTitle(component.getName())
						+ "*");
			} else {
				tabPane.setTitleAt(tabPane.indexOfComponent(this),
						WorkflowTabbedPane.cleanTitle(component.getName()));
			}
		}
	}
}