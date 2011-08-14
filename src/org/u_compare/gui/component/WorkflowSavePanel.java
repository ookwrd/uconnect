package org.u_compare.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.control.WorkflowPaneController;
import org.u_compare.gui.model.Workflow;

public class WorkflowSavePanel extends JPanel {

	protected Workflow component;

	public WorkflowSavePanel(Workflow component){
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
	
	private void saveButtonClicked(){
		WorkflowPaneController.saveAdaptor.saveDescriptor(component.getResourceCreationSpecifier());
		
	}
}
