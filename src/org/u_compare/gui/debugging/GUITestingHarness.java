package org.u_compare.gui.debugging;

import java.util.ArrayList;

import javax.swing.JComponent;

import org.u_compare.gui.UConnectSplitPane;

import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.library.LibraryPane;
import org.u_compare.gui.model.Workflow;

/**
 * TODO: XXX:
 * 
 * @author 	luke
 * @author 	pontus
 * @version 2010-12-04
 */

public class GUITestingHarness {

	
	public static void main(String[] args){
		//Workflow panel
		ArrayList<Workflow> workflows = new ArrayList<Workflow>();
		workflows.add(ExampleWorkflowFactory.simpleWithParameters());
		workflows.add(ExampleWorkflowFactory.aggregate());
		//workflows.add(ExampleWorkflowFactory.deepAggregate(3, 2));
		//workflows.add(WorkflowPaneController.defaultWorkflow());
		workflows.add(ExampleWorkflowFactory.realComponents());
		
		WorkflowPaneController workflowPaneController = new WorkflowPaneController();
		
		JComponent tabbedPane = workflowPaneController.initialize(workflows);
		
		// Library panel
		LibraryPane libraryPane = new LibraryPane();
		
		// Combining
		UConnectSplitPane uConnectSplit = new UConnectSplitPane(tabbedPane,
				libraryPane);
		TestWindow testWindow = new TestWindow("GUITestingHarness",
				uConnectSplit);
		
		testWindow.setVisible(true);
	}
}
