package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.guiElements.EditableTextField;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.DescriptionChangeListener;

@SuppressWarnings("serial")
public class TitlePanel extends JPanel implements DescriptionChangeListener {

	private static final Font font = new Font("sansserif", Font.BOLD, 12);
	private static final Font titleFont = new Font("sansserif", Font.BOLD, 16);

	private final ComponentController controller;
	private final Component component;

	EditableTextField titleField;

	public TitlePanel(ComponentController controller, Component component,
			boolean isWorkflow, ComponentTitleBar topPanel) {
		super();

		this.controller = controller;
		this.component = component;

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

		titleField = new EditableTextField(component.getName());
	
		if (isWorkflow) {
			titleField.setFont(titleFont);
		} else {
			titleField.setFont(font);
		}

		// TODO fix the centering
		// titleLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		//titleLabel.setAlignmentY(java.awt.Component.CENTER_ALIGNMENT);
		// titleTextField.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
	 //	titleTextField.setAlignmentY(java.awt.Component.CENTER_ALIGNMENT);

		add(titleField);

		if (!isWorkflow) {
			setBackground(Color.WHITE);
		}
	
		// set a minimum size, in case the title is short
		int h = getFontMetrics(titleField.getFont()).getHeight();//TODO move to EditableTextField
		titleField.setMinimumSize(new Dimension(100, h));
		
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
