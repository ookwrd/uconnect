package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;

/**
 * Constraint class for expressing several common constraint types on individual integers.
 * 
 * @author Luke McCrohon
 *
 */
public class IntegerConstraint extends Constraint{

	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	
	//Null corresponds to no whiteList restriction
	private ArrayList<Integer> whiteList;//TODO push whitelist/blacklist into superclass.
	//Null corresponds to no blackList restriction
	private ArrayList<Integer> blackList;
	
	public IntegerConstraint(){}
	
	public IntegerConstraint(int min, int max){
		this.min = min;
		this.max = max;
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
	
	public void setWhiteList(ArrayList<Integer> whiteList){
		this.whiteList = whiteList;
	}
	
	public void setBlackList(ArrayList<Integer> blackList){
		this.blackList = blackList;
	}
	
	@Override
	public void validate(String in) throws ConstraintFailedException{		
		
		int value;
		
		try{
			value = Integer.parseInt(in);
		}catch(NumberFormatException ex){
			throw new ConstraintFailedException("Input not an Integer.");
			//TODO wrap the number format exception
		}
		
		validate(value);
		
	}
	
	@Override
	public void validate(int value) throws ConstraintFailedException{		
		if(value < min){
			throw new ConstraintFailedException("Input value to low. "
					+ "Please input a value of " + min + " or higher.");			
		}
		
		if(value > max){
			throw new ConstraintFailedException("Input value to high. "
					+ "Please input a value of " + max + " or lower.");			
		}
		
		if(whiteList != null){
			boolean found = false;
			for(int i : whiteList){
				if (i == value) {
					found = true;
					break;
				}
			}	
			
			if(!found){
				throw new ConstraintFailedException(
						"Input value does not belong "
						+ "to set of acceptable values");
			}	
		}
		
		if(blackList != null){
			
			for(int i : blackList){
				
				if(i == value){
					throw new ConstraintFailedException(
							"Input belongs to set of unacceptable values");
				}
			}
		}
	}
}
