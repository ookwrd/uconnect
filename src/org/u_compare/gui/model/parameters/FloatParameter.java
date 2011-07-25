package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;
import org.u_compare.gui.model.parameters.constraints.FloatConstraint;

//TODO
public class FloatParameter extends AbstractParameter<Float> {

	public FloatParameter(String name, String description, boolean mandatory, Float parameter) {
		super(name, description, mandatory, false);
		try {
			add(parameter);
		} catch (ConstraintFailedException e) {
			System.out.println("Constraint failed on Parameter construction, this should not be possible.");
			e.printStackTrace();
		}
		addConstraint(new FloatConstraint());
	}
	
	public FloatParameter(String name, String description, boolean mandatory, Float[] values) {
		super(name, description, mandatory, true);
		
		//this.value = value;TODO
		addConstraint(new FloatConstraint());
	}

	@Override
	public void setValue(String input) throws ConstraintFailedException {
		validateConstraints(input);
		Float inputFloat = Float.parseFloat(input);
		simpleSet(inputFloat);
	}
}
