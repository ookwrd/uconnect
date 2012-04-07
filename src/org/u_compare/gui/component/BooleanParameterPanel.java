package org.u_compare.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import org.u_compare.gui.control.ParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.Parameter.ParameterValueChangedListener;

/**
 * Panel for setting a Boolean Parameter
 * 
 * @author Luke McCrohon
 * 
 */

public class BooleanParameterPanel extends ParameterPanel implements
		ActionListener, LockedStatusChangeListener,
		ParameterValueChangedListener {

	private JCheckBox checkBox;
	private ParameterController controller;
	private BooleanParameter param;

	public BooleanParameterPanel(Parameter parameter,
			ParameterController controller, Component component) {
		super(parameter, controller, component);
		assert (parameter instanceof BooleanParameter);

		this.controller = controller;
		this.param = (BooleanParameter) parameter;

		if (!param.isMultivalued()) {
			checkBox = new JCheckBox();
			if (param.getParameter() != null) {// Needed because multivalued may
												// be null
				checkBox.setSelected(param.getParameter());
			}
			checkBox.addActionListener(this);

			param.registerParameterValueChangedListener(this);

			field = checkBox;
		}

		updateLockedStatus(component);
	}

	@Override
	public void parameterSettingsChanged(Parameter param) {
		if (field instanceof JCheckBox) {
			checkBox.setSelected(this.param.getParameter());
		} else {
			super.parameterSettingsChanged(param);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.setValue(checkBox.isSelected() ? "true" : "false");
	}
}
