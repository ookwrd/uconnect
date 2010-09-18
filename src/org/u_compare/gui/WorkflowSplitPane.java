package org.u_compare.gui;

import java.awt.Dimension;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.DescriptionChangeListener;

/**
 * Holds the workflow pane and the console pane.
 * 
 * @author pontus
 * @version 2009-08-28
 */
@SuppressWarnings("serial")
public class WorkflowSplitPane extends JSplitPane implements DescriptionChangeListener{
	private ConsolePane consolePane;
	private WorkflowPane workflowPane;
	
	// Configuration
	private static final boolean ONE_TOUCH_EXPANDABLE = true;
	private static final double SEPARATOR_START_LOCATION_FROM_BOTTOM = 0.2;
	private static final int SPLIT_ORIENTATION = JSplitPane.VERTICAL_SPLIT;
	
	JTabbedPane tabs;
	
	public WorkflowSplitPane(WorkflowPane workflowPane,
			ConsolePane consolePane) {
		this.workflowPane = workflowPane;
		this.consolePane = consolePane;

		workflowPane.getTopWorkflowComponent().getComponent().registerComponentDescriptionChangeListener(this);
		
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

	//Should be called if we have been added to a tabbed pane.
	public void linkTabbedPane(JTabbedPane pane){
		
		this.tabs = pane;
	}
	
	@Override
	public void ComponentDescriptionChanged(Component component) {
		if(tabs!=null){
			System.out.println("Attempting to change workflow tab name");
			
			int index = tabs.indexOfTabComponent(this);
			System.out.println("Index recorded as: " + index);
			index = 0;
			System.out.println(tabs.getTabComponentAt(0).equals(this));
			System.out.println(tabs.getTabComponentAt(0) == this);
			//WTF is going on here?
			this.setPreferredSize(new Dimension(1,1));
			System.out.println(tabs.getTabComponentAt(0));
			System.out.println(this);
			

			int index1 = indexOfTabComponent(this);
			System.out.println("Index recorded as: " + index1);
			
			tabs.setTitleAt(index, component.getName());
		}
	}
	
	//getComponentAt
	//getTabComponentAt
	
	//straight from java source:
    public int indexOfTabComponent(java.awt.Component tabComponent) {
        for(int i = 0; i < tabs.getTabCount(); i++) {
        	java.awt.Component c = tabs.getTabComponentAt(i);
        	System.out.println(c);
        	System.out.println(c == this);
            if (c == tabComponent) {
                return i;
            }
        }
        return -1; 
    }
}
