package org.u_compare.user_tools;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.debugging.TestWindow;
import org.u_compare.gui.model.Workflow;

public class UIMA_Tester {

	private static final JFileChooser fc = new JFileChooser();
	
	public UIMA_Tester(String file){
		
		WorkflowPaneController workflowPaneController = new WorkflowPaneController();
		workflowPaneController.setAllowTabs(false);
		JComponent tabbedPane = workflowPaneController.initialize(new Workflow());
		
		TestWindow testWindow = new TestWindow("UIMA Component Tester", tabbedPane);
		testWindow.setSize(new Dimension(600,800));
		testWindow.setVisible(true);
	}
	
	public static void main(String[] args){
	
		int result = fc.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			new UIMA_Tester(file.getAbsolutePath());
		}
	}
	
}
