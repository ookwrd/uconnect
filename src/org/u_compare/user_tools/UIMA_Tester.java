package org.u_compare.user_tools;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;

import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.control.WorkflowPaneController.WorkflowFactory;
import org.u_compare.gui.debugging.TestWindow;
import org.u_compare.gui.model.Workflow;

public class UIMA_Tester {

	private static final JFileChooser fc = new JFileChooser();
	
	public UIMA_Tester(String filename){
		//TODO use input filename
		
		WorkflowFactory factory = new LoadFactory();
		
		WorkflowPaneController.ALLOW_TABS = true;
		WorkflowPaneController.SHOW_NEW_TAB = false;
		WorkflowPaneController.SHOW_LOAD_TAB = true;
		WorkflowPaneController.SHOW_SAVE_PANEL = true;
		WorkflowPaneController.loadAdaptor = factory;
		
		WorkflowPaneController workflowPaneController = new WorkflowPaneController();
		JComponent tabbedPane = workflowPaneController.initialize(factory.constructWorkflow());
		
		TestWindow testWindow = new TestWindow("UIMA Workflow Tester", tabbedPane);
		testWindow.setSize(new Dimension(600,800));
		testWindow.setVisible(true);
	}
	
	private class LoadFactory implements WorkflowFactory {
		@Override
		public Workflow constructWorkflow() {
			String filename = "";
			int result = fc.showOpenDialog(null);
			if(result == JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				filename = file.getAbsolutePath();
			}
			
			return  Workflow.constructWorkflowFromXML(filename);
		}
	}
	
	public static void main(String[] args){
		new UIMA_Tester(args[0]);
	}
	
}
