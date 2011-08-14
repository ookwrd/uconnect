package org.u_compare.user_tools;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.debugging.TestWindow;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.uima.CPE;

public class UIMA_Tester {

	private static final JFileChooser fc = new JFileChooser();
	
	public UIMA_Tester(String file){
		
		WorkflowPaneController.ALLOW_TABS = false;
		WorkflowPaneController.SHOW_SAVE_PANEL = true;
		
		WorkflowPaneController workflowPaneController = new WorkflowPaneController();
		Workflow workflow = Workflow.constructWorkflowFromXML(file);
		
		JComponent tabbedPane = workflowPaneController.initialize(workflow);
		
		TestWindow testWindow = new TestWindow("UIMA Workflow Tester", tabbedPane);
		testWindow.setSize(new Dimension(600,800));
		testWindow.setVisible(true);
	}
	
	public static void main(String[] args){
		if(args.length > 0){
			new UIMA_Tester(args[0]);
		}else{
			int result = fc.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				new UIMA_Tester(file.getAbsolutePath());
			}
		}
	}
	
}
