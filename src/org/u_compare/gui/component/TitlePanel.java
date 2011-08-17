package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.guiElements.EditableTextField;
import org.u_compare.gui.guiElements.LukesDragAndDropImplementation;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.DescriptionChangeListener;

@SuppressWarnings("serial")
public class TitlePanel extends JPanel implements DescriptionChangeListener {

	private static final Font font = new Font("sansserif", Font.BOLD, 12);
	private static final Font titleFont = new Font("sansserif", Font.BOLD, 20);

	private final ComponentController controller;
	private final Component component;

	EditableTextField titleField;

	public TitlePanel(ComponentController controller, Component component,
			boolean isWorkflow, ComponentTitleBar topPanel) {
		super();

		this.controller = controller;
		this.component = component;

		setLayout(new BorderLayout());
		
		titleField = new EditableTextField(component.getName());
	
		LukesDragAndDropImplementation.registerDragSource(titleField, controller);
		
		if (isWorkflow) {
			titleField.setFont(titleFont);
		} else {
			titleField.setFont(font);
		}

		add(titleField, BorderLayout.CENTER);

		if (!isWorkflow) {
			setBackground(Color.WHITE);
		}
		
		titleField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				titleChangeRequest(titleField.getText());
			}
		});
		
		component.registerComponentDescriptionChangeListener(this);
	}

	protected void titleChangeRequest(String title) {
		titleField.setText(component.getName());//TODO refactor Name/Title
		this.controller.setTitle(title);
	}

	@Override
	public void ComponentDescriptionChanged(Component component) {
		titleField.setText(component.getName());
	}

}
