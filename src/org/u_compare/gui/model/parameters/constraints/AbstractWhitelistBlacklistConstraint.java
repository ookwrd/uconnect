package org.u_compare.gui.model.parameters.constraints;

import java.util.ArrayList;

public abstract class AbstractWhitelistBlacklistConstraint<T> extends Constraint {

	//Null corresponds to no whiteList restriction
	protected ArrayList<T> whiteList;
	//Null corresponds to no blackList restriction
	protected ArrayList<T> blackList;
	
	
	public void setWhiteList(ArrayList<T> whiteList){
		this.whiteList = whiteList;
	}
	
	public void setBlackList(ArrayList<T> blackList){
		this.blackList = blackList;
	}
	
	public void validateBlacklistWhitelistConstraints(T value) throws ConstraintFailedException {
		
		if(whiteList != null){
			boolean found = false;
			for(T i : whiteList){
				if (i.equals(value)) {
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
			for(T i : blackList){
				if(i.equals(value)){
					throw new ConstraintFailedException(
							"Input belongs to set of unacceptable values");
				}
			}
		}
	}
}
