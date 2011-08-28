package org.u_compare.gui.component;

import static org.u_compare.gui.component.IconFactory.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	private final static String STATUS_PREFIX = "Workflow Status: ";

	private static final String RUN_TOOLTIPTEXT = "Run workflow";
	private static final String PAUSED_TOOLTIPTEXT = "Resume Processing";
	private static final String PAUSE_TOOLTIPTEXT = "Pause Processing";
	private static final String STOP_TOOLTIPTEXT = "Stop workflow";
	
	private WorkflowController controller;

	private JLabel statusLabel;

	private HighlightButton runButton;
	private HighlightButton stopButton;
	
	private boolean pauseMode = false;

	public WorkflowControlPanel(Workflow component,
			WorkflowController controller) {
		super();

		this.controller = controller;

		ActionListener playListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playButtonClicked();
			}
		};

		ActionListener stopListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopButtonClicked();
			}
		};

		setOpaque(false);

		statusLabel = new JLabel(STATUS_PREFIX
				+ ((Workflow) component).getStatus());
		//Adjust preferredSize so Label doesn't resize when messaged changed.
		Dimension labelSize = statusLabel.getPreferredSize();
		statusLabel.setPreferredSize(new Dimension(labelSize.width+40,labelSize.height));
		this.add(statusLabel);

		IconFactory.loadIcons();

		// run button
		runButton = new HighlightButton(getIcon(RUN_ICON));
		runButton.addActionListener(playListener);
		this.add(runButton);
		
		// stop button
		stopButton = new HighlightButton(getIcon(STOP_ICON));
		stopButton.addActionListener(stopListener);
		this.add(stopButton);

		//To configure initial status
		workflowStatusChanged(component);
		
		component.registerWorkflowStatusListener(this);
	}
	


	private void stopButtonClicked() {
		controller.workflowStopRequest();
	}

	private void playButtonClicked() {
		if(pauseMode){
			controller.workflowPauseRequest();
		}else{
			controller.workflowPlayRequest();
		}
	}

	private void setPlayButton(WorkflowStatus status) {
		
		switch (status) {
		case READY:
		case ERROR:
		case FINISHED:
			pauseMode = false;
			runButton.setEnabled(true);
			runButton.setIcon(getIcon(RUN_ICON));
			runButton.setToolTipText(RUN_TOOLTIPTEXT);
			break;
			
		case PAUSED:
			pauseMode = false;
			runButton.setEnabled(true);
			runButton.setIcon(getIcon(RUN_ICON));
			runButton.setToolTipText(PAUSED_TOOLTIPTEXT);
			break;
			
		case LOADING:
		case INITIALIZING:
			pauseMode = false;
			runButton.setEnabled(false);
			break;

		case RUNNING:
			pauseMode = true;
			runButton.setEnabled(true);
			runButton.setIcon(getIcon(PAUSE_ICON));
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
			stopButton.setToolTipText(STOP_TOOLTIPTEXT);
			break;
			
		case PAUSED:
		case RUNNING:
			stopButton.setEnabled(true);
			break;
		}
		
	}

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		WorkflowStatus status = workflow.getStatus();
		statusLabel.setText(STATUS_PREFIX + status);
		setPlayButton(status);
		setStopButton(status);
	}

}
