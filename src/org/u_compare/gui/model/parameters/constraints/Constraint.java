package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;

import org.u_compare.gui.model.parameters.InvalidInputException;

/**
 * Abstract base class to extend when implementing parameter constraints.
 * 
 * @author Luke Mccrohon
 *
 */
public abstract class Constraint {

	/**
	 * Everything fails by default.
	 * 
	 * TODO explain why inputs are strings
	 * 
	 * TODO explain addative constraints.
	 * 
	 * @param in
	 * @throws InvalidInputException
	 */
	public void validate(String in) throws ConstraintFailedException {
		throw new IllegalArgumentException(
				"Single string validation not allowed by this constraint.");
	}
	
	public void validate(Integer in) throws ConstraintFailedException {
		throw new IllegalArgumentException(
				"Single int validation not allowed by this constraint.");
	}
	
	public void validate(Float in) throws ConstraintFailedException {
		throw new IllegalArgumentException(
			"Single float validation not allowed by this constraint.");
	}
	
	public void validate(ArrayList<String> in)
			throws ConstraintFailedException {
		throw new IllegalArgumentException(
				"String set validation not allowed by this constraint.");
	}
	
}
