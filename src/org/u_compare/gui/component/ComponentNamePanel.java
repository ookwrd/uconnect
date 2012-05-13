package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.DragAndDropController;
import org.u_compare.gui.guiElements.EditableTextField;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.DescriptionChangeListener;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;

/**
 * View element for displaying a component/workflow's title.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class ComponentNamePanel extends JPanel implements
		DescriptionChangeListener, LockedStatusChangeListener {

	private static final Font font = new Font("sansserif", Font.BOLD, 12);
	private static final Font workflowFont = new Font("sansserif", Font.BOLD,
			20);

	private final ComponentController controller;
	private final Component component;

	private final EditableTextField nameField;

	public ComponentNamePanel(ComponentController controller,
			Component component, boolean isWorkflow, ComponentTitleBar topPanel) {
		super();

		this.controller = controller;
		this.component = component;

		setLayout(new BorderLayout());

		nameField = new EditableTextField(component.getName());

		if (controller.allowEditing() && !component.isWorkflow()) {
			DragAndDropController.registerDragSource(nameField, controller);
		}

		if (isWorkflow) {
			nameField.setFont(workflowFont);
		} else {
			nameField.setFont(font);
			setBackground(Color.WHITE);
		}

		add(nameField, BorderLayout.CENTER);

		nameField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameChangeRequest(nameField.getText());
			}
		});

		component.registerComponentDescriptionChangeListener(this);
		component.registerLockedStatusChangeListener(this);
	}

	protected void nameChangeRequest(String name) {
		nameField.setText(component.getName());
		this.controller.setName(name);
	}

	@Override
	public void ComponentDescriptionChanged(Component component) {
		nameField.setText(component.getName());
	}

	@Override
	public void lockStatusChanged(Component component) {
		nameField.setEnabled(!component.getLockedStatus());
	}
}
