package org.u_compare.gui.model.parameters.constraints;

/**
 * Constraint specifying that the input value is a float.
 * 
 * @author Luke McCrohon
 */
public class FloatConstraint extends
		AbstractWhitelistBlacklistConstraint<Float> {

	private float min = -Float.MAX_VALUE;
	private float max = Float.MAX_VALUE;
	
	public FloatConstraint() {
	}

	public FloatConstraint(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	public void setMax(float max) {
		this.max = max;
	}

	public void setMin(float min) {
		this.min = min;
	}

	public void setRange(float min, float max) {
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void validate(String in) throws ConstraintFailedException {
		Float value;
		try {
			value = Float.parseFloat(in);
		} catch (NumberFormatException ex) {
			throw new ConstraintFailedException("Input not a float.", ex);
		}
		validate(value);
	}

	@Override
	public void validate(Float value) throws ConstraintFailedException {
		if (value < min) {
			throw new ConstraintFailedException("Input value too low. "
					+ "Please input a value of " + min + " or higher.");
		}

		if (value > max) {
			throw new ConstraintFailedException("Input value too high. "
					+ "Please input a value of " + max + " or lower.");
		}
		
		super.validateBlacklistWhitelistConstraints(value);
	}
}
