package org.u_compare.gui.model.parameters;

import java.util.ArrayList;

import org.u_compare.gui.model.parameters.constraints.Constraint.ConstraintFailedException;
import org.u_compare.gui.model.parameters.constraints.IntegerConstraint;

/**
 * Model representation of an Integer Parameter.
 * 
 * @author Luke McCrohon
 *
 */
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
	
	@Override
	public void setValues(String[] input) throws ConstraintFailedException{
		assert(!owner.getLockedStatus());
		validateConstraints(input);
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(String string : input){
			values.add(Integer.parseInt(string));
		}
		simpleSet(values.toArray(new Integer[values.size()]));
	}
}
