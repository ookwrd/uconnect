package org.u_compare.user_tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.UCompareWindow;
import org.u_compare.gui.control.WorkflowViewerController;
import org.u_compare.gui.control.WorkflowViewerController.WorkflowFactory;
import org.u_compare.gui.model.Workflow;

/**
 * Class for testing and demonstration purposes which implements a workflow
 * viewer using the Uconnect library.
 * 
 * @author Luke McCrohon
 * 
 */
public class UIMA_Tester {

	private static final JFileChooser fc = new JFileChooser();

	@SuppressWarnings("serial")
	public UIMA_Tester(String filename) {
		// TODO use input filename

		WorkflowFactory factory = new LoadFactory();

		WorkflowViewerController.ALLOW_TABS = true;
		WorkflowViewerController.SHOW_NEW_TAB = false;
		WorkflowViewerController.SHOW_LOAD_TAB = true;
		WorkflowViewerController.SHOW_SAVE_PANEL = true;
		WorkflowViewerController.loadAdaptor = factory;
		WorkflowViewerController.emptyTabbedPanel = new JPanel() {
			{
				setName("      ");
				setLayout(new BorderLayout());
				add(new JLabel("Click the load tab to load a new workflow"),
						BorderLayout.CENTER);
			}
		};

		WorkflowViewerController workflowPaneController = new WorkflowViewerController();
		JComponent tabbedPane = workflowPaneController.initialize(factory
				.constructWorkflow());

		UCompareWindow testWindow = new UCompareWindow("UIMA Workflow Tester",
				tabbedPane);
		testWindow.setSize(new Dimension(600, 800));
		testWindow.setVisible(true);
	}

	private class LoadFactory implements WorkflowFactory {
		@Override
		public Workflow constructWorkflow() {
			String filename = "";
			int result = fc.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				filename = file.getAbsolutePath();
			}

			System.out.println("Loading: " + filename);

			return Workflow.constructWorkflowFromXML(filename);
		}
	}

	public static void main(String[] args) {
		new UIMA_Tester(args[0]);
	}

}
