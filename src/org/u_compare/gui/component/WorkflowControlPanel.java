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
import org.u_compare.gui.model.WorkflowStatusListener;
import org.u_compare.gui.model.Workflow.WorkflowStatus;

/**
 * Workflow level panel allowing the workflow to be run/stopped/paused.
 * 
 * @author Luke McCrohon
 */

@SuppressWarnings("serial")
public class WorkflowControlPanel extends JPanel implements
		WorkflowStatusListener {

	private static final boolean debug = true;
	public final static String STATUS_PREFIX = "Workflow Status: ";
	public static final String ICON_RUN_PATH = "../gfx/icon_start.png";
	public static final String ICON_STOP_PATH = "../gfx/icon_stop.png";
	public static final String ICON_PAUSE_PATH = "../gfx/icon_pause.png";
	private static final String RUN_TOOLTIPTEXT = "Run workflow";
	private static final String STOP_TOOLTIPTEXT = "Stop workflow";
	private static final boolean PAUSE = false; //TODO are these needed?
	private static final boolean PLAY = true;
	
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
				System.out.println("Play button hit");
			}
		};

		ActionListener stopListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(stopButtonActive()) {
					stopWorkflow();
					//if (debug) System.out.println("Button stop hit, it was active");
				}
				else {
					if (debug) System.out.println("Button stop currently not active");
				}
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

	private boolean stopButtonActive() {
		return ((Workflow) component).getStatus() != WorkflowStatus.READY;
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

	/**
	 * Toggle the button appearance and message. "Play" if the parameter play is
	 * true, "Pause" otherwise.
	 * 
	 * @param play
	 */
	private void togglePlayButton(boolean play) {
		if (play) {
			runButton.setIcon(runIcon);
			runButton.setToolTipText("Play");
		} else {
			runButton.setIcon(pauseIcon);
			runButton.setToolTipText("Pause");
		}
	}

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		statusLabel.setText(STATUS_PREFIX + workflow.getStatus());
		// TODO: Update Buttons
		if (workflow.getStatus().equals(WorkflowStatus.RUNNING))
			togglePlayButton(PAUSE);
		else
			togglePlayButton(PLAY);
	}

}
