package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;
import org.u_compare.gui.model.parameters.constraints.FloatConstraint;

//TODO
public class FloatParameter extends AbstractParameter<Float> {

	public FloatParameter(String name, String description, boolean mandatory, Float parameter) {
		super(name, description, mandatory, false);
		setInitial(parameter);
		addConstraint(new FloatConstraint());
	}
	
	public FloatParameter(String name, String description, boolean mandatory, Float[] values) {
		super(name, description, mandatory, true);
		setInitials(values);
		addConstraint(new FloatConstraint());
	}

	@Override
	public void setValue(String input) throws ConstraintFailedException {
		assert(!owner.getLockedStatus());
		validateConstraints(input);
		Float inputFloat = Float.parseFloat(input);
		simpleSet(inputFloat);
	}
	
	@Override
	public void setValues(String[] input) throws ConstraintFailedException{
		assert(!owner.getLockedStatus());
		validateConstraints(input);
		ArrayList<Float> values = new ArrayList<Float>();
		for(String string : input){
			values.add(Float.parseFloat(string));
		}
		simpleSet((Float[])values.toArray());
	}
}
