package org.u_compare.gui.control;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.u_compare.gui.ConsolePane;
import org.u_compare.gui.WorkflowPane;
import org.u_compare.gui.WorkflowSplitPane;
import org.u_compare.gui.WorkflowTabbedPane;
import org.u_compare.gui.model.Workflow;

public class WorkflowPaneController {

	private static final boolean SHOW_CONSOLE = true;
	private static final boolean AlLOW_TABS = true;
	
	private WorkflowTabbedPane tabbedPane = new WorkflowTabbedPane();
	
	public JComponent initialize(){
		
		return tabbedPane;
		
	}
	
	public JComponent initialize(ArrayList<Workflow> workflows){
		
		for(Workflow workflow : workflows) {
			tabbedPane.addWorkflow(constructWorkflow(workflow));
		}
		
		return initialize();
	}
	
	private WorkflowSplitPane constructWorkflow(Workflow workflow){
		WorkflowController workflowModel = new WorkflowController(workflow);
		
		// Construct the view
		WorkflowPane workflowPane = new WorkflowPane(workflowModel.getView());
		
		ConsolePane consolePane = new ConsolePane(workflow);
		return new WorkflowSplitPane(workflowPane, consolePane);
	}
	
	
	
}
