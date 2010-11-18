package org.u_compare.gui.model.parameters;

import java.security.acl.Owner;

import org.u_compare.gui.BooleanParameterPanel;
import org.u_compare.gui.control.BooleanParameterController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class BooleanParameter extends
		AbstractParameter {

	private boolean parameter;
	
	public BooleanParameter(String description, boolean value){
		super(description);
		this.parameter = value;
	}
	
	public boolean getParameter(){
		return parameter;
	}

	public void update(boolean input) throws ConstraintFailedException {
		
		if(input != parameter){
			parameter = input;
			
			notifyParameterSettingsChangedListeners();
		}
	}

	public String getParameterString() {
		return String.valueOf(parameter);
	}
	

}
