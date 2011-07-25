package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class BooleanParameter extends
		AbstractParameter<Boolean> {

	public BooleanParameter(String name, String description, boolean mandatory, Boolean parameter) {
		super(name, description, mandatory, false);
		try {
			add(parameter);
		} catch (ConstraintFailedException e) {
			System.out.println("Constraint failed on Parameter construction, this should not be possible.");
			e.printStackTrace();
		}
	}
	
	public BooleanParameter(String name, String description, boolean mandatory, Boolean[] values) {
		super(name, description, mandatory, true);
		//this.parameter = value; TODO
	}

	@Override
	public String getParameterString() {
		Boolean parameter = getParameter();
		if(parameter!=null){
			return String.valueOf(parameter);
		}else{
			return null;
		}
	}	

}
