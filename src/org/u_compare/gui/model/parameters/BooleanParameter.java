package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class BooleanParameter extends
		AbstractParameter<Boolean> {

	public BooleanParameter(String name, String description, boolean mandatory, Boolean parameter) {
		super(name, description, mandatory, false);
		setInitial(parameter);
	}
	
	public BooleanParameter(String name, String description, boolean mandatory, Boolean[] values) {
		super(name, description, mandatory, true);
		setInitials(values);
	}
	
	public void setValue(Boolean value){
		simpleSet(value);
	}
	
	@Override
	public void setValue(String input) throws ConstraintFailedException {
		assert(!owner.getLockedStatus());
		validateConstraints(input);
		Boolean in = Boolean.parseBoolean(input);
		simpleSet(in);
	}
}
