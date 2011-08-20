package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;
import org.u_compare.gui.model.parameters.constraints.IntegerConstraint;

public class IntegerParameter extends AbstractParameter<Integer>{
	
	public IntegerParameter(String name, String description, boolean mandatory, Integer parameter) {
		super(name, description, mandatory, false);
		setInitial(parameter);
		addConstraint(new IntegerConstraint());
	}
	
	public IntegerParameter(String name, String description, boolean mandatory, Integer[] values) {
		super(name, description, mandatory, true);
		setInitials(values);
		addConstraint(new IntegerConstraint());
	}	
	
	@Override
	public void setValue(String input) throws ConstraintFailedException {
		assert(!owner.getLockedStatus());
		validateConstraints(input);
		Integer inputInt = Integer.parseInt(input);
		simpleSet(inputInt);
	}
}
