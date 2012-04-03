package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.DragAndDropController;
import org.u_compare.gui.guiElements.EditableTextPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.DescriptionChangeListener;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;

/**
 * Panel for displaying/editing a components description.
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class ComponentDescriptionPanel extends JPanel implements
		DescriptionChangeListener, LockedStatusChangeListener {

	private final ComponentController controller;
	private final Component component;
	private final EditableTextPanel textPanel;

	public ComponentDescriptionPanel(ComponentController controller, Component component) {
		super();

		this.controller = controller;
		this.component = component;

		textPanel = new EditableTextPanel(component.getDescription());
		textPanel.registerActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textChangeRequest(textPanel.getContentText());
			}
		});
		
		DragAndDropController.registerDragSource(textPanel.getContent(),controller);
		
		setLayout(new BorderLayout());
		setOpaque(false);
		this.add(textPanel);

		// Register Listeners
		component.registerComponentDescriptionChangeListener(this);
		component.registerLockedStatusChangeListener(this);
	}

	protected void textChangeRequest(String descriptionText) {
		descriptionText = descriptionText.trim();
		textPanel.setContentText(component.getDescription());
		this.controller.setDescription(descriptionText);
	}

	@Override
	public void ComponentDescriptionChanged(Component component) {
		textPanel.setContentText(component.getDescription());
	}

	@Override
	public void lockStatusChanged(Component component) {
		textPanel.setEnabled(!component.getLockedStatus());
	}

}
