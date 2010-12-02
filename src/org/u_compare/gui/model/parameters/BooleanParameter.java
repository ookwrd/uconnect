package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class BooleanParameter extends
		AbstractParameter {

	private Boolean parameter; //null represents unset.
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param value Null value represents value unset.
	 */
	public BooleanParameter(String name, String description, boolean mandatory, Boolean value){
		super(name, description, mandatory);
		this.parameter = value;
	}
	
	public Boolean getParameter(){
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
		if(parameter!=null){
			return String.valueOf(parameter);
		}else{
			return null;
		}
	}

	@Override
	public boolean isMultivalued() {
		return false;
	}
	

}
