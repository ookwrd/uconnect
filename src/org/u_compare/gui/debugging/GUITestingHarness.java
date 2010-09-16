package org.u_compare.gui.debugging;

import javax.swing.JLabel;

import org.u_compare.gui.UConnectSplitPane;
import org.u_compare.gui.WorkflowConsolePane;
import org.u_compare.gui.WorkflowPane;
import org.u_compare.gui.WorkflowSplitPane;
import org.u_compare.gui.WorkflowTabbedPane;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.library.LibraryPane;
import org.u_compare.gui.model.UIMAWorkflow;

public class GUITestingHarness {

	
	public static void main(String[] args){
		
		//Workflow panel
		
		//Construct an example model
		//UIMAWorkflow workflow = ExampleWorkflowFactory.simpleWithParameters();
		UIMAWorkflow workflow = ExampleWorkflowFactory.aggregate();
		//UIMAWorkflow workflow = new UIMAWorkflow();
		
		//Construct the associated controller
		ComponentController workflowModel = new ComponentController(workflow);
		
		// Construct the view
		WorkflowPane workflowPane = new WorkflowPane(workflowModel.getView());
		
		WorkflowConsolePane consolePane = new WorkflowConsolePane();
		WorkflowSplitPane splitPane = new WorkflowSplitPane(workflowPane, consolePane);
		WorkflowTabbedPane tabbedPane = new WorkflowTabbedPane(splitPane);
		
		
		
		//Library panel
		
		LibraryPane libraryPane = new LibraryPane();
		
		
		
		//Combining
		
		UConnectSplitPane uConnectSplit = new UConnectSplitPane(tabbedPane,libraryPane);
		
		new TestWindow("GUITestingHarness", uConnectSplit);
	}
	
}
