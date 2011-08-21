package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter<String>{

	public StringParameter(String name, String description, boolean mandatory, String parameter) {
		super(name, description, mandatory, false);
		setInitial(parameter);
	}
	
	public StringParameter(String name, String description, boolean mandatory, String[] parameter) {
		super(name, description, mandatory, true);
		setInitials(parameter);
	}

	@Override
	public void setValue(String input) throws ConstraintFailedException {
		assert(!owner.getLockedStatus());
		validateConstraints(input);
		simpleSet(input);
	}
	
	@Override
	public void setValues(String[] input) throws ConstraintFailedException{
		assert(!owner.getLockedStatus());
		validateConstraints(input);
		simpleSet(input);
	}
}
