package org.u_compare.gui.model.parameters.constraints;

/**
 * Constraint class for expressing several common constraint types on individual integers.
 * 
 * @author Luke McCrohon
 *
 */
public class IntegerConstraint extends AbstractWhitelistBlacklistConstraint<Integer> {

	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
		
	public IntegerConstraint(){}
	
	public IntegerConstraint(int min, int max){
		setRange(min, max);
	}
	
	public void setMax(int max){
		this.max = max;
	}
	
	public void setMin(int min){
		this.min = min;
	}
	
	public void setRange(int min, int max){
		this.min = min;
		this.max = max;
	}
	
	@Override
	public void validate(String in) throws ConstraintFailedException{		
		Integer value;
		try{
			value = Integer.parseInt(in);
		}catch(NumberFormatException ex){
			throw new ConstraintFailedException("Input not an Integer.", ex);
		}
		validate(value);
	}
	
	@Override
	public void validate(Integer value) throws ConstraintFailedException{		
		if(value < min){
			throw new ConstraintFailedException("Input value too low. "
					+ "Please input a value of " + min + " or higher.");			
		}
		
		if(value > max){
			throw new ConstraintFailedException("Input value too high. "
					+ "Please input a value of " + max + " or lower.");			
		}

		super.validateBlacklistWhitelistConstraints(value);
	}
}
