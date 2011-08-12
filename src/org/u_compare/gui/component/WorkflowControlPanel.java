package org.u_compare.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.control.WorkflowController;
import org.u_compare.gui.guiElements.HighlightButton;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.Workflow.WorkflowStatus;
import org.u_compare.gui.model.Workflow.WorkflowStatusListener;

/**
 * Workflow level panel allowing the workflow to be run/stopped/paused.
 * 
 * @author Luke McCrohon
 */

@SuppressWarnings("serial")
public class WorkflowControlPanel extends JPanel implements
		WorkflowStatusListener {

	public static final String ICON_RUN_PATH = "../gfx/icon_start.png";
	public static final String ICON_STOP_PATH = "../gfx/icon_stop.png";
	public static final String ICON_PAUSE_PATH = "../gfx/icon_pause.png";

	private final static String STATUS_PREFIX = "Workflow Status: ";
	private static final String RUN_TOOLTIPTEXT = "Run workflow";
	private static final String PAUSED_TOOLTIPTEXT = "Resume Processing";
	private static final String PAUSE_TOOLTIPTEXT = "Pause Processing";
	private static final String STOP_TOOLTIPTEXT = "Stop workflow";
	
	private static boolean iconsLoaded = false;

	private static ImageIcon runIcon;
	private static ImageIcon stopIcon;
	private static ImageIcon pauseIcon;

	private Workflow component;
	private WorkflowController controller;

	private JLabel statusLabel;

	private HighlightButton runButton;
	private HighlightButton stopButton;

	public WorkflowControlPanel(Workflow component,
			WorkflowController controller) {
		super();

		this.component = component;
		this.controller = controller;

		ActionListener playListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playWorkflow();
			}
		};

		ActionListener stopListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopWorkflow();
			}
		};

		setOpaque(false);

		statusLabel = new JLabel(STATUS_PREFIX
				+ ((Workflow) component).getStatus());
		this.add(statusLabel);

		WorkflowControlPanel.loadIcons();

		// run button
		runButton = new HighlightButton(runIcon);
		runButton.addActionListener(playListener);
		runButton.setToolTipText(RUN_TOOLTIPTEXT);
		this.add(runButton);
		
		// stop button
		stopButton = new HighlightButton(stopIcon);
		stopButton.addActionListener(stopListener);
		stopButton.setToolTipText(STOP_TOOLTIPTEXT);
		this.add(stopButton);

		component.registerWorkflowStatusListener(this);

	}
	
	protected static synchronized void loadIcons() {
		if (WorkflowControlPanel.iconsLoaded == true) {
			return;
		}

		URL image_url;
		image_url = ComponentPanel.class
				.getResource(WorkflowControlPanel.ICON_RUN_PATH);
		assert image_url != null;
		WorkflowControlPanel.runIcon = new ImageIcon(image_url, "Run");

		image_url = ComponentPanel.class
				.getResource(WorkflowControlPanel.ICON_STOP_PATH);
		assert image_url != null;
		WorkflowControlPanel.stopIcon = new ImageIcon(image_url, "Stop");

		image_url = ComponentPanel.class
				.getResource(WorkflowControlPanel.ICON_PAUSE_PATH);
		assert image_url != null;
		WorkflowControlPanel.pauseIcon = new ImageIcon(image_url, "Pause");

		WorkflowControlPanel.iconsLoaded = true;
		return;
	}

	private void stopWorkflow() {
		WorkflowStatus currentStatus = ((Workflow) component).getStatus();
		if (currentStatus == WorkflowStatus.READY
				|| currentStatus == WorkflowStatus.PAUSED) {
			controller.workflowPlayRequest();
		} else if (currentStatus == WorkflowStatus.RUNNING
				|| currentStatus == WorkflowStatus.INITIALIZING) {
			controller.workflowPauseRequest();
		}
	}

	private void playWorkflow() {
		controller.workflowStopRequest();
	}

	private void setPlayButton(WorkflowStatus status) {
		
		switch (status) {
		case READY:
		case ERROR:
		case FINISHED:
			runButton.setEnabled(true);
			runButton.setIcon(runIcon);
			runButton.setToolTipText(RUN_TOOLTIPTEXT);
			break;
			
		case PAUSED:
			runButton.setEnabled(true);
			runButton.setIcon(runIcon);
			runButton.setToolTipText(PAUSED_TOOLTIPTEXT);
			break;
			
		case LOADING:
		case INITIALIZING:
			runButton.setEnabled(false);
			break;

		case RUNNING:
			runButton.setEnabled(true);
			runButton.setIcon(pauseIcon);
			runButton.setToolTipText(PAUSE_TOOLTIPTEXT);
			break;
		}
		
	}
	
	private void setStopButton(WorkflowStatus status){
		
		switch (status) {
		case READY:
		case LOADING:
		case INITIALIZING:
		case ERROR:
		case FINISHED:
			stopButton.setEnabled(false);
			break;
			
		case PAUSED:
		case RUNNING:
			stopButton.setEnabled(true);
			break;
		}
		
	}

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		
		System.out.println("Status Change" + workflow.getStatus());
		
		WorkflowStatus status = workflow.getStatus();
		statusLabel.setText(STATUS_PREFIX + status);
		setPlayButton(status);
		setStopButton(status);
	}

}
