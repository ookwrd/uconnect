package org.u_compare.gui.component.parameters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import org.u_compare.gui.control.BooleanParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.ParameterValueChangedListener;

/**
 * TODO
 * 
 * @author Luke McCrohon
 *
 */

@SuppressWarnings("serial")
public class BooleanParameterPanel extends ParameterPanel implements ActionListener, LockedStatusChangeListener, ParameterValueChangedListener {

	private JCheckBox checkBox;
	private BooleanParameterController controller;
	private BooleanParameter param;
	
	public BooleanParameterPanel(
			BooleanParameter param, BooleanParameterController controller, Component component) {
			super(param, component);
		
			this.controller = controller;
			this.param = param;
			
			checkBox = new JCheckBox();
			if(param.getParameter()!=null){//Needed because multivalued may be null
				checkBox.setSelected(param.getParameter());
			}
			checkBox.addActionListener(this);
			
			this.add(checkBox);
			
			component.registerLockedStatusChangeListener(this);
			param.registerParameterValueChangedListener(this);
			
			field = checkBox;
			
			updateLockedStatus();
	}
	
	@Override
	public void parameterSettingsChanged(Parameter param) {
		checkBox.setSelected(this.param.getParameter());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		controller.setValue(checkBox.isSelected());
	}
}
