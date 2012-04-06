package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;

/**
 * Abstract base class to extend when implementing parameter constraints.
 * 
 * @author Luke McCrohon
 * 
 */
public abstract class Constraint {

	@SuppressWarnings("serial")
	public class ConstraintFailedException extends Exception {

		private String msgString;

		public ConstraintFailedException(String msgString) {
			this.msgString = msgString;
		}

		public ConstraintFailedException(String msgString, Throwable cause) {
			super(cause);
			this.msgString = msgString;
		}

		public String getUserReadableError() {
			return msgString;
		}

	}

	/**
	 * Everything fails by default, specific constraints override the validation
	 * methods that they implement.
	 * 
	 * For the most part validation will be on string forms as this is what is
	 * received from the user/filesystem. Strings can of course be converted to
	 * the required form and then processed when this is what is required.
	 * 
	 * @param in
	 * @throws InvalidInputException
	 */
	public void validate(String in) throws ConstraintFailedException {
		throw new IllegalArgumentException(
				"Single string validation not allowed by this constraint.");
	}

	// TODO is this needed?
	public void validate(Integer in) throws ConstraintFailedException {
		throw new IllegalArgumentException(
				"Single int validation not allowed by this constraint.");
	}

	// TODO is this needed?
	public void validate(Float in) throws ConstraintFailedException {
		throw new IllegalArgumentException(
				"Single float validation not allowed by this constraint.");
	}

	public void validate(ArrayList<String> in) throws ConstraintFailedException {
		throw new IllegalArgumentException(
				"String set validation not allowed by this constraint.");
	}

}
