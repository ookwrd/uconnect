package org.u_compare.gui.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.uima.resource.metadata.MetaDataObject;
import org.u_compare.gui.ConsolePane;
import org.u_compare.gui.WorkflowPane;
import org.u_compare.gui.WorkflowHorizontalSplitPane;
import org.u_compare.gui.WorkflowViewer;
import org.u_compare.gui.model.AnnotationTypeOrFeature;
import org.u_compare.gui.model.Workflow;
import org.xml.sax.SAXException;

/**
 * This class serves as the controller for the toplevel WorkflowViewer component
 * and so serves as the main point of interaction for most people wishing to use
 * the UConnect libraries.
 * 
 * After construction with the default constructor, WorkflowViewer behavior can
 * be configured by setting the public static configuration parameters. These
 * include more complex adaptor parameters such as the typeChooser and
 * defaultWorkflowFactory which allow full configuration of many aspects of
 * library behavior. The basic parameter settings should be sufficient in many
 * cases.
 * 
 * Once configuration is complete, call one of the initialization methods to
 * configure the WorkflowViewerController and produce its associated view
 * component.
 * 
 * @author Luke McCrohon
 * 
 */
public class WorkflowViewerController {

	public interface WorkflowFactory {
		public Workflow constructWorkflow();
	}

	public interface AnnotationTypeChooser {
		public AnnotationTypeOrFeature getNewAnnotation();
	}

	public interface WorkflowSaveAdaptor {
		public void saveWorkflow(MetaDataObject descriptor);
	}

	// Configuration parameters

	/**
	 * Determines whether the console should be shown for workflows. Defaults to
	 * true.
	 */
	public static boolean SHOW_CONSOLE = true;

	/**
	 * Determines whether workflow controls (Stop/Play) should be shown for the
	 * workflow. Defaults to true.
	 */
	public static boolean SHOW_WORKFLOW_CONTROL = true;

	/**
	 * Determines whether the title and description of the workflow are
	 * displayed in the workflowPanel. CPE workflows do not store this data, so
	 * may wish to hide it. Deafults to true.
	 */
	public static boolean SHOW_WORKFLOW_DETAILS = true;

	/**
	 * Determines whether a panel with a "save workflow" button is included at
	 * the end of the workflow. Defaults to false.
	 */
	public static boolean SHOW_SAVE_PANEL = false;

	/**
	 * Determines whether to allow multiple workflows to be displayed in tabs.
	 * Defaults to true.
	 */
	public static boolean ALLOW_TABS = true;

	/**
	 * Determines whether to show a "new tab" button. Requires ALLOW_TABS to be
	 * set to true. Defaults to true.
	 */
	public static boolean SHOW_NEW_TAB = true;

	/**
	 * Specifies the text of the "new tab" button enabled by SHOW_NEW_TAB.
	 * Defaults to "New".
	 */
	public static String NEW_TAB_NAME = "New";

	/**
	 * Determines whether to show a "load tab" button. Requires ALLOW_TABS to be
	 * set to true. Defaults to false.
	 */
	public static boolean SHOW_LOAD_TAB = false;

	/**
	 * Specifies the text of the "load tab" button enabled by SHOW_LOAD_TAB.
	 * Defaults to "Load".
	 */
	public static String LOAD_TAB_NAME = "Load";

	/**
	 * Determines whether components are editable. Defaults to true.
	 */
	public static boolean ALLOW_EDITING = true;

	/**
	 * Determines whether tabs can be closed. Defaults to true.
	 */
	public static boolean ALLOW_TAB_CLOSE = true;

	/**
	 * Specifies how to choose types when required.
	 */
	public static AnnotationTypeChooser typeChooser = new AnnotationTypeChooser() {
		@Override
		public AnnotationTypeOrFeature getNewAnnotation() {
			String typeName = JOptionPane
					.showInputDialog("Please enter the type name to add:");
			return new AnnotationTypeOrFeature(typeName);
		}
	};

	/**
	 * Specifies how new blank workflows are constructed (i.e. via the "New tab"
	 * button enabled by SHOW_NEW_TAB). Defaults to creating an empty
	 * gui.model.Workflow workflow. Most systems will want to override this.
	 */
	public static WorkflowFactory defaultWorkflowFactory = new WorkflowFactory() {
		@Override
		public Workflow constructWorkflow() {
			Workflow workflow = new Workflow();
			workflow.setName("Untitled Workflow (Double-Click to edit)");
			workflow.setDescription("This is not a real UIMA Workflow. Set WorkflowPaneController's defaultWorkflowFactory."
					+ ""
					+ " Double-Click here to edit its description. Duis quis arcu id enim elementum gravida quis sit amet justo. Cras non enim nec velit aliquet luctus sed faucibus arcu. Phasellus dolor quam, dapibus a consequat eget, fringilla vitae ipsum. Donec tristique elementum turpis, in pellentesque nulla viverra vitae. Curabitur eget turpis non quam auctor ornare. Aliquam tempus quam vitae lectus consectetur fringilla. Vivamus posuere pharetra elit ac interdum. Aenean vestibulum mattis justo et malesuada. Ut ultrices, nisl sit amet tempor porttitor, nulla ipsum feugiat purus, porta tincidunt sem sapien nec leo. Phasellus rhoncus elit sit amet lectus adipiscing vulputate. ");
			return workflow;
		}
	};

	/**
	 * Specifies how to handle workflow save events (i.e. from the save prompt
	 * on closing a modified workflow). Defaults to a filechooser prompting the
	 * user to select the location to save the workflow descriptor to disk.
	 */
	public static WorkflowSaveAdaptor saveAdaptor = new WorkflowSaveAdaptor() {
		private final JFileChooser fc = new JFileChooser();

		@Override
		public void saveWorkflow(MetaDataObject descriptor) {
			try {
				int result = fc.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					FileWriter writer = new FileWriter(file.getAbsolutePath());
					descriptor.toXML(writer);
					writer.close();
				}
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * Specifies how to handle user generated load events. Defaults to a
	 * filechoosing to select a UIMA xml descriptor.
	 */
	public static WorkflowFactory loadAdaptor = new WorkflowFactory() {
		private final JFileChooser fc = new JFileChooser();

		@Override
		public Workflow constructWorkflow() {
			int result = fc.showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				return Workflow
						.constructWorkflowFromXML(file.getAbsolutePath());
			}
			// Cancel option selected.
			return null;
		}
	};

	/**
	 * Specifies the panel to be shown when no workflow tabs exist.
	 */
	@SuppressWarnings("serial")
	public static JPanel emptyTabbedPanel = new JPanel() {
		{
			setName("      ");
			add(new JLabel("Create or Load a workflow to begin."));
		}
	};

	private WorkflowViewer tabbedPane;

	private JComponent init(
			ArrayList<WorkflowHorizontalSplitPane> workflowSplitPanes) {

		if (workflowSplitPanes.size() == 0) {
			workflowSplitPanes.add(constructDefaultWorkflow());
		}

		if (ALLOW_TABS) {
			tabbedPane = new WorkflowViewer(this);
			tabbedPane.setEmptyTab(emptyTabbedPanel);

			for (WorkflowHorizontalSplitPane workflowSplitPane : workflowSplitPanes) {
				tabbedPane.addWorkflow(workflowSplitPane);
			}

			return tabbedPane;

		} else {
			assert (workflowSplitPanes.size() == 1);
			return workflowSplitPanes.get(0);
		}

	}

	/**
	 * Initialize the WorkflowViewerComponent with no initial workflow and
	 * produce its view component. The view will be either a WorkflowViewer or
	 * WorkflowHorizontalSplitPane depending on the ALLOW_TABS configuration
	 * parameter.
	 * 
	 * @return view
	 */
	public JComponent initialize() {
		ArrayList<WorkflowHorizontalSplitPane> workflowSplitPanes = new ArrayList<WorkflowHorizontalSplitPane>();
		return init(workflowSplitPanes);
	}

	/**
	 * Initialize the WorkflowViewerComponent with a single initial workflow and
	 * produce its view component. The view will be either a WorkflowViewer or
	 * WorkflowHorizontalSplitPane depending on the ALLOW_TABS configuration
	 * parameter.
	 * 
	 * @return view
	 */
	public JComponent initialize(Workflow workflow) {
		ArrayList<Workflow> workflows = new ArrayList<Workflow>();
		workflows.add(workflow);
		return initialize(workflows);
	}

	/**
	 * Initialize the WorkflowViewerComponent with a set of initial workflows
	 * and produce its view component. The view will be either a WorkflowViewer
	 * or WorkflowHorizontalSplitPane depending on the ALLOW_TABS configuration
	 * parameter.
	 * 
	 * If multiple workflows are provided, the ALLOW_TABS parameter must be set
	 * to true.
	 * 
	 * @return view
	 */
	public JComponent initialize(ArrayList<Workflow> workflows) {
		assert (!(workflows.size() > 1) || ALLOW_TABS);

		ArrayList<WorkflowHorizontalSplitPane> workflowSplitPanes = new ArrayList<WorkflowHorizontalSplitPane>();

		for (Workflow workflow : workflows) {
			workflowSplitPanes.add(constructWorkflow(workflow));
		}

		return init(workflowSplitPanes);
	}

	private WorkflowHorizontalSplitPane constructWorkflow(Workflow workflow) {
		workflow.setComponentSaved();// TODO should this be moved to workflow
										// constructor?

		if (!ALLOW_EDITING) {
			workflow.setLocked();
		}

		WorkflowController workflowController = new WorkflowController(
				workflow, SHOW_WORKFLOW_CONTROL, SHOW_WORKFLOW_DETAILS,
				SHOW_SAVE_PANEL, ALLOW_EDITING);

		// Construct the view
		WorkflowPane workflowPane = new WorkflowPane(
				workflowController.getView());

		ConsolePane consolePane = null;

		if (SHOW_CONSOLE) {
			consolePane = new ConsolePane(workflow);
			consolePane.addConsoleMessage("Workflow Loaded.");
		}

		return new WorkflowHorizontalSplitPane(workflowPane, consolePane);
	}

	private WorkflowHorizontalSplitPane constructDefaultWorkflow() {
		return constructWorkflow(defaultWorkflowFactory.constructWorkflow());
	}

	private WorkflowHorizontalSplitPane constructDraggedWorkflow() {
		return constructWorkflow(WorkflowViewerController.draggedWorkflow());
	}

	public static void setTypeChooser(AnnotationTypeChooser typeChooser) {
		WorkflowViewerController.typeChooser = typeChooser;
	}

	/**
	 * Create a new workflow based on the currently dragged component.
	 * 
	 * @return
	 */
	private static Workflow draggedWorkflow() {

		Workflow workflow = defaultWorkflowFactory.constructWorkflow();

		ComponentController controllerDragged = DragAndDropController
				.getController().getDraggedComponent();
		workflow.addSubComponent(controllerDragged.component);
		controllerDragged.removeComponent();

		return workflow;
	}

	/**
	 * Process a user request to create a new workflow.
	 */
	public void requestNewWorkflow() {
		assert (ALLOW_TABS);
		tabbedPane.addWorkflow(constructDefaultWorkflow());
	}

	/**
	 * Process a user request to load a workflow.
	 */
	public void requestLoadWorkflow() {
		assert (ALLOW_TABS);
		Workflow workflow = loadAdaptor.constructWorkflow();
		if (workflow != null) {
			tabbedPane.addWorkflow(constructWorkflow(workflow));
		}
	}

	/**
	 * Create a new workflow based on what is currently being dragged.
	 */
	public void requestNewWorkflowDragged() {
		assert (ALLOW_TABS);
		tabbedPane.addWorkflow(constructDraggedWorkflow());
	}

	/**
	 * Process a user request to close a specific workflow.
	 * 
	 * @param workflow
	 *            Workflow to close.
	 */
	public void requestWorkflowClose(Workflow workflow) {
		assert (ALLOW_TABS);

		if (workflow.checkUnsavedChanges()) {

			int reply = JOptionPane
					.showOptionDialog(
							null,
							"Unsaved changes exist to this workflow.\n\nDo you wish to save them before closing?",
							"Unsaved Changes Exist!",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, new String[] {
									"Save before Closing",
									"Close without Saving", "Cancel" }, 2);

			if (reply == 0) {// Save and close
				saveWorkflow(workflow);
				closeWorkflow(workflow);
			} else if (reply == 1) {// Close without Saving
				closeWorkflow(workflow);
			}

		} else {

			int reply = JOptionPane.showOptionDialog(null,
					"Do you want to close this workflow?", "Close Workflow?",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, new String[] { "Close", "Cancel" }, 1);

			if (reply == 0) {// Close
				closeWorkflow(workflow);
			}

		}

	}

	private void closeWorkflow(Workflow workflow) {
		tabbedPane.confirmedRemoveWorkflow(workflow);
	}

	private void saveWorkflow(Workflow workflow) {
		saveAdaptor.saveWorkflow(workflow.getWorkflowDescription());
	}
}
