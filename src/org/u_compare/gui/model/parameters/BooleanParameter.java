package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class BooleanParameter extends
		AbstractParameter<Boolean> {

	private Boolean parameter; //null represents unset.
	
	/**
	 * 
	 * @param name
	 * @param description
	 * @param value Null value represents value unset.
	 */
	public BooleanParameter(String name, String description, boolean mandatory, Boolean value) {
		super(name, description, mandatory, false);
		this.parameter = value;
	}
	
	public BooleanParameter(String name, String description, boolean mandatory, Boolean[] values) {
		super(name, description, mandatory, true);
		//this.parameter = value; TODO
	}
	
	public Boolean getParameter(){
		return parameter;
	}

	@Override
	public void setValue(Boolean input) throws ConstraintFailedException {
		
		if(input != parameter){
			parameter = input;
			//No necessity to validate against constraints as can't constrain boolean parameters
			notifyParameterValueChangedListeners();
		}
	}

	public String getParameterString() {
		if(parameter!=null){
			return String.valueOf(parameter);
		}else{
			return null;
		}
	}	

}
