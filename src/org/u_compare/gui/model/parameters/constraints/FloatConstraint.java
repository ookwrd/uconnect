package org.u_compare.gui.model.parameters.constraints;

public class FloatConstraint extends AbstractWhitelistBlacklistConstraint<Float> {

	public FloatConstraint(){
		
	}
	
	@Override
	public void validate(String in) throws ConstraintFailedException {
		Float value;
		try{
			value = Float.parseFloat(in);
		}catch(NumberFormatException ex){
			throw new ConstraintFailedException("Input not a float.", ex);
		}
		validate(value);
	}
	
	@Override
	public void validate(Float value) throws ConstraintFailedException {
		super.validateBlacklistWhitelistConstraints(value);
	}
}
