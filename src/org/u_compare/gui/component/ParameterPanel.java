package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.control.WorkflowViewerController;
import org.u_compare.gui.guiElements.AutoscrollTextField;
import org.u_compare.gui.guiElements.ControlList;
import org.u_compare.gui.guiElements.TooltipTools;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.Parameter.ParameterValueChangedListener;

/**
 * 
 * View element for displaying/editing a parameter setting of a String, Integer
 * or Float parameter. For Boolean Parameter, a special BooleanParameterPanel is
 * used.
 * 
 * @author Luke McCrohon
 * 
 */
public class ParameterPanel implements LockedStatusChangeListener,
		ParameterValueChangedListener {

	private static final int DESCRIPTION_LENGTH = 43;
	
	private static final String MANDATORY_STRING = "*";

	protected JComponent field;
	protected Parameter param;
	protected ParameterController controller;

	public ParameterPanel(Parameter param,
			final ParameterController controller, Component component) {
		this.param = param;
		this.controller = controller;

		if (!param.isMultivalued()) {// Single valued parameter

			// Setup default field
			final JTextField textField = new AutoscrollTextField(
					param.getParameterString());
			textField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					textFieldChanged(textField);
				}
			});
			textField.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					textFieldChanged(textField);
				}
			});

			// Use file chooser?
			if (WorkflowViewerController.USE_FILE_CHOOSER && (
					param.getName().toLowerCase().endsWith("file") || 
					param.getName().toLowerCase().endsWith("directory"))) {
				field = new FileChooserPanel(textField);
			} else {
				field = textField;
			}

		} else { // Multivalued parameter
			final ControlList list = new ControlList(Color.white, true, true);
			list.setBorder(new EtchedBorder());

			ActionListener addListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.addValue();
				}
			};

			ActionListener removeListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*
					 * JList and DefaultListModel provide no way for accessing
					 * contents as anything other than an Object[]. Hence having
					 * to go through the array element by element and casting
					 * each selected name individually.
					 */
					for (Object name : list.getSelectedValues()) {
						controller.removeValue((String) name);
					}
				}
			};

			list.registerAddActionListener(addListener);
			list.registerDeleteActionListener(removeListener);

			field = list;

			rebuildListContents();
		}

		component.registerLockedStatusChangeListener(this);
		param.registerParameterValueChangedListener(this);

		updateLockedStatus(component);
	}

	public JLabel getLabel() {
		String title = param.getName();
		if (title == null || title.length() == 0) {
			title = param.getDescription();
		}
		if (title.length() > DESCRIPTION_LENGTH) {
			title = title.substring(0, DESCRIPTION_LENGTH - 3)
					+ "...";
		}
		title += ":";
		JLabel descriptionLabel = new JLabel(title);
		descriptionLabel.setToolTipText(TooltipTools.formatTooltip("<b>"
				+ param.getName() + "</b>\n" + param.getDescription()));// Unabridged
		// description
		descriptionLabel.setHorizontalAlignment(JLabel.TRAILING);

		return descriptionLabel;
	}

	public JLabel getMandatoryLabel() {
		JLabel mandatory;
		if (param.isMandatory()) {
			mandatory = new JLabel(MANDATORY_STRING);
			mandatory.setToolTipText("Mandatory Parameter");
		} else {
			mandatory = new JLabel(" ");
		}
		return mandatory;
	}

	public JComponent getField() {
		return field;
	}

	@Override
	public void lockStatusChanged(Component component) {
		updateLockedStatus(component);
	}

	protected void updateLockedStatus(Component component) {
		field.setEnabled(!component.getLockedStatus());
	}

	@Override
	public void parameterSettingsChanged(Parameter param) {
		if (field instanceof JTextField) {
			((JTextField) field).setText(param.getParameterString());
		} else if (field instanceof ControlList) {
			rebuildListContents();
		} else if (field instanceof FileChooserPanel) { // FileChooser
			((FileChooserPanel) field).update(param);
		} else {
			System.err
					.println("If a class overriding parameter panel, changes the 'field' it also needs to override the parameterSettingsChanged method");
		}
	}

	protected void textFieldChanged(JTextField field) {
		String value = field.getText();
		String originalValue = param.getParameterString();
		if (!value.equals(originalValue)) {
			((JTextField) field).setText(originalValue);
			controller.setValue(value);
		}
	}

	private void rebuildListContents() {
		((ControlList) field).rebuildListContents(new ArrayList<String>(Arrays
				.asList(param.getParameterStrings())));
	}

	@SuppressWarnings("serial")
	private class FileChooserPanel extends JPanel {
		private JTextField field;

		FileChooserPanel(JTextField textField) {
			field = textField;
			setLayout(new BorderLayout());
			add(textField, BorderLayout.CENTER);

			JButton addButton = new JButton("Browse");
			addButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String fileName = WorkflowViewerController.fileNameChooser
							.chooseFileName(field.getText());
					controller.setValue(fileName);
				}
			});
			add(addButton, BorderLayout.EAST);
		}

		private void update(Parameter param) {
			field.setText(param.getParameterString());
		}
	}
}
