package org.u_compare.gui.debugging;

import java.util.ArrayList;

import javax.swing.JComponent;

import org.u_compare.gui.UCompareWindow;
import org.u_compare.gui.UConnectVerticalSplitPane;

import org.u_compare.gui.control.WorkflowViewerController;
import org.u_compare.gui.library.LibraryPane;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.uima.CPE;

/**
 * Demonstration and testing class which loads a number of mock workflows into a
 * workflow panel and displays it alongside a mock library panel.
 * 
 * @author luke
 * @author pontus
 * @version 2010-12-04
 */

public class GUITestingHarness {

	public static void main(String[] args) {

		// Construct a set of workflows
		ArrayList<Workflow> workflows = new ArrayList<Workflow>();
		workflows.add(ExampleWorkflowFactory.simpleWithParameters());
		workflows.add(ExampleWorkflowFactory.aggregate());
		//workflows.add(ExampleWorkflowFactory.deepAggregate(3, 2));
		// workflows.add(ExampleWorkflowFactory.deepAggregate(5,2));
		// workflows.add(ExampleWorkflowFactory.realComponents());
		//workflows.add(ExampleWorkflowFactory.realComponents1());
		//workflows.add(ExampleWorkflowFactory.realComponents2());
		//workflows.add(ExampleWorkflowFactory.cpeWorkflow());
		//workflows.add(ExampleWorkflowFactory.cpeWorkflow());
		// workflows.add(ExampleWorkflowFactory.cpeWorkflowRecursive());
		//workflows.add(ExampleWorkflowFactory.cpeWorkflowRecursive());
		workflows.add(ExampleWorkflowFactory.cpeWorkflowParams());
		workflows.add(ExampleWorkflowFactory.cpeWorkflowParams());
		
		// Construct a WorkflowController
		WorkflowViewerController workflowViewerController = new WorkflowViewerController();
		WorkflowViewerController.defaultWorkflowFactory = CPE.emptyCPEFactory;
		WorkflowViewerController.SHOW_SAVE_PANEL = true;
		WorkflowViewerController.ALLOW_TABS = true;
		WorkflowViewerController.SHOW_LOAD_TAB = true;
		// WorkflowViewerController.ALLOW_EDITING = false;

		// Add the workflows
		JComponent tabbedPane = workflowViewerController.initialize(workflows);

		// Create mock library panel
		LibraryPane libraryPane = new LibraryPane();

		// Assemble panels
		UConnectVerticalSplitPane uConnectSplit = new UConnectVerticalSplitPane(
				tabbedPane, libraryPane);
		UCompareWindow testWindow = new UCompareWindow("GUITestingHarness",
				uConnectSplit);

		testWindow.setVisible(true);
	}
}
