package org.u_compare.gui.debugging;

import java.util.ArrayList;

import javax.swing.JComponent;

import org.u_compare.gui.UConnectVerticalSplitPane;

import org.u_compare.gui.control.WorkflowViewerController;
import org.u_compare.gui.library.LibraryPane;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.uima.CPE;

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
		//workflows.add(ExampleWorkflowFactory.simpleWithParameters());
		//workflows.add(ExampleWorkflowFactory.aggregate());
		workflows.add(ExampleWorkflowFactory.deepAggregate(3, 2));
		//workflows.add(ExampleWorkflowFactory.deepAggregate(5,2));
		workflows.add(ExampleWorkflowFactory.realComponents());
		//workflows.add(ExampleWorkflowFactory.realComponents1());
		//workflows.add(ExampleWorkflowFactory.realComponents2());
		//workflows.add(ExampleWorkflowFactory.cpeWorkflow());
		
		//TODO check path
		workflows.add(ExampleWorkflowFactory.cpeWorkflow());
		workflows.add(ExampleWorkflowFactory.cpeWorkflowRecursive());
		workflows.add(ExampleWorkflowFactory.cpeWorkflowRecursive());
		
		WorkflowViewerController workflowPaneController = new WorkflowViewerController();
		WorkflowViewerController.defaultWorkflowFactory = CPE.emptyCPEFactory;
		//WorkflowPaneController.SHOW_SAVE_PANEL = true;
		WorkflowViewerController.SHOW_LOAD_TAB = true;
		JComponent tabbedPane = workflowPaneController.initialize(workflows);
		
		// Library panel
		LibraryPane libraryPane = new LibraryPane();
		
		// Combining
		UConnectVerticalSplitPane uConnectSplit = new UConnectVerticalSplitPane(tabbedPane,
				libraryPane);
		TestWindow testWindow = new TestWindow("GUITestingHarness", uConnectSplit);
		
		testWindow.setVisible(true);
	}
}
