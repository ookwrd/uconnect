package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class BooleanParameter extends
		AbstractParameter {

	private boolean parameter;
	
	public BooleanParameter(String name, String description, boolean value){
		super(name, description);
		this.parameter = value;
	}
	
	public boolean getParameter(){
		return parameter;
	}

	public void update(boolean input) throws ConstraintFailedException {
		
		if(input != parameter){
			parameter = input;
			//No necessity to validate against constraints as can't constrain boolean parameters
			notifyParameterSettingsChangedListeners();
		}
	}

	public String getParameterString() {
		return String.valueOf(parameter);
	}
	

}
