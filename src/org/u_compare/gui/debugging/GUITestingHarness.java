package org.u_compare.gui.debugging;

import java.util.ArrayList;

import javax.swing.JLabel;

import org.u_compare.gui.UConnectSplitPane;
import org.u_compare.gui.ConsolePane;
import org.u_compare.gui.WorkflowPane;
import org.u_compare.gui.WorkflowSplitPane;
import org.u_compare.gui.WorkflowTabbedPane;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.library.LibraryPane;
import org.u_compare.gui.model.Workflow;

public class GUITestingHarness {

	
	public static void main(String[] args){
		//Workflow panel
		ArrayList<Workflow> workflows = new ArrayList<Workflow>();
		workflows.add(ExampleWorkflowFactory.simpleWithParameters());
		workflows.add(ExampleWorkflowFactory.aggregate());
		workflows.add(ExampleWorkflowFactory.deepAggregate(3, 2));
		
		WorkflowTabbedPane tabbedPane = new WorkflowTabbedPane();
		
		for(Workflow workflow : workflows) {
			tabbedPane.addWorkflow(constructWorkflow(workflow));
		}
		
		// Library panel
		LibraryPane libraryPane = new LibraryPane();
		
		// Combining
		UConnectSplitPane uConnectSplit = new UConnectSplitPane(tabbedPane,
				libraryPane);
		
		TestWindow testWindow = new TestWindow("GUITestingHarness",
				uConnectSplit);
	}
	
	public static WorkflowSplitPane constructWorkflow(Workflow workflow){
		ComponentController workflowModel = new ComponentController(workflow);
		
		// Construct the view
		WorkflowPane workflowPane = new WorkflowPane(workflowModel.getView());
		
		ConsolePane consolePane = new ConsolePane(workflow);
		return new WorkflowSplitPane(workflowPane, consolePane);
	}
	
	public static Workflow blankWorkflow(){
		return new Workflow();
		//TODO set its parameters as appropriate
	}
}
