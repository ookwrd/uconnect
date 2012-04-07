package org.u_compare.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.control.WorkflowViewerController;
import org.u_compare.gui.model.Workflow;

/**
 * View element which is optionally included at the end of workflows providing
 * the functionality for them to be saved.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class WorkflowSavePanel extends JPanel {

	protected Workflow component;

	public WorkflowSavePanel(Workflow component) {
		super();

		this.component = component;

		add(new JLabel("Save workflow? "));

		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveButtonClicked();
			}
		});

		setOpaque(false);

		add(saveButton);
	}

	private void saveButtonClicked() {
		WorkflowViewerController.saveAdaptor.saveWorkflow(component
				.getWorkflowDescription());

	}
}
