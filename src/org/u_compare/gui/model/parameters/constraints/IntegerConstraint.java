package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;

import org.u_compare.gui.model.parameters.InvalidInputException;

/**
 * Constraint class for expressing several common constraint types on individual integers.
 * 
 * @author Luke Mccrohon
 *
 */
public class IntegerConstraint extends Constraint{

	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	
	private ArrayList<Integer> whiteList; //Null corresponds to no whiteList restriction
	private ArrayList<Integer> blackList; //Null corresponds to no blackList restriction
	
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
	
	public void validate(String in) throws InvalidInputException{		
		
		int value;
		
		try{
			value = Integer.parseInt(in);
		}catch(NumberFormatException ex){
			throw new InvalidInputException("Input not an Integer.");
		}
		
		if(value < min){
			throw new InvalidInputException("Input value to low. Please input a value of " + min + " or higher.");			
		}
		
		if(value > max){
			throw new InvalidInputException("Input value to high. Please input a value of " + max + "or lower.");			
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
				throw new InvalidInputException("Input value does not belong to whiteListed set of acceptable values");
			}	
		}
		
		if(blackList != null){
			
			for(int i : blackList){
				
				if(i == value){
					throw new InvalidInputException("Input belongs to blackListed set of unacceptable values");
				}
			}
		}
		
	}
	
}
