package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import org.u_compare.gui.control.WorkflowController;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.WorkflowStatusListener;
import org.u_compare.gui.model.Workflow.WorkflowStatus;

/**
 * TODO:
 * 
 * @author luke
 * @version 2010-11-10
 */

@SuppressWarnings("serial")
public class WorkflowControlPanel extends JPanel implements
		WorkflowStatusListener {

	public final static String STATUS_PREFIX = "Workflow Status: ";
	private static final int BUTTON_DECREMENT = 0;
	public static final String ICON_RUN_PATH = "../gfx/icon_start.png";
	public static final String ICON_STOP_PATH = "../gfx/icon_stop.png";
	public static final String ICON_PAUSE_PATH = "../gfx/icon_pause.png";
	private static final String RUN_TOOLTIPTEXT = "Run workflow";
	private static final String STOP_TOOLTIPTEXT = "Stop workflow";
	private static final boolean PAUSE = false;
	private static final boolean PLAY = true;

	private static boolean iconsLoaded = false;

	private static ImageIcon runIcon;
	private static ImageIcon stopIcon;
	private static ImageIcon pauseIcon;

	private Workflow component;
	private WorkflowController controller;

	private ActionListener playListener;
	private ActionListener stopListener;

	private JLabel statusLabel;

	private JButton runButton;
	private JButton stopButton;

	private BevelBorder highlighted;
	private Border empty;

	private ActionListener stopHighlight;
	private ActionListener stopUnhighlight;

	public WorkflowControlPanel(Workflow component,
			WorkflowController controller) {
		super();

		this.component = component;
		this.controller = controller;

		playListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playWorkflow();
			}
		};

		stopListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopWorkflow();
			}
		};

		setOpaque(false);

		statusLabel = new JLabel(STATUS_PREFIX
				+ ((Workflow) component).getStatus());
		this.add(statusLabel);

		WorkflowControlPanel.loadIcons();

		Dimension buttonSize;

		// run button
		runButton = new JButton(runIcon);
		runButton.setBorder(new BevelBorder(BevelBorder.RAISED,
				Color.LIGHT_GRAY, Color.DARK_GRAY));
		buttonSize = new Dimension(runIcon.getIconWidth() - BUTTON_DECREMENT,
				runIcon.getIconHeight() - BUTTON_DECREMENT);
		// stopButton.setPreferredSize(buttonSize);
		runButton.setFocusPainted(false); // This may be needed for a mac
											// specific behaviou
		runButton.addActionListener(playListener);
		runButton.setToolTipText(RUN_TOOLTIPTEXT);
		this.add(runButton);

		// stop button
		stopButton = new JButton(stopIcon);
		stopButton.setBorder(new BevelBorder(BevelBorder.RAISED,
				Color.LIGHT_GRAY, Color.DARK_GRAY));
		buttonSize = new Dimension(stopIcon.getIconWidth() - BUTTON_DECREMENT,
				stopIcon.getIconHeight() - BUTTON_DECREMENT);
		// stopButton.setPreferredSize(buttonSize);
		stopButton.setFocusPainted(false);
		stopButton.addActionListener(stopListener);
		stopButton.setToolTipText(STOP_TOOLTIPTEXT);
		this.add(stopButton);

		component.registerWorkflowStatusListener(this);

		// set highlighting
		highlighted = new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY,
				Color.LIGHT_GRAY);
		empty = runButton.getBorder();
		runButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				runButton.setBorder(highlighted);
			}

			public void mouseExited(MouseEvent e) {
				runButton.setBorder(empty);
			}
		});

		highlighted = new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY,
				Color.LIGHT_GRAY);
		empty = stopButton.getBorder();
		stopButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				stopButton.setBorder(highlighted);
			}

			public void mouseExited(MouseEvent e) {
				stopButton.setBorder(empty);
			}
		});
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
