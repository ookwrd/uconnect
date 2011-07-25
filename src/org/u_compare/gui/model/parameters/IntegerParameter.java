package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;
import org.u_compare.gui.model.parameters.constraints.IntegerConstraint;

public class IntegerParameter extends AbstractParameter<Integer>{
	
	public IntegerParameter(String name, String description, boolean mandatory, Integer parameter) {
		super(name, description, mandatory, false);
		try {
			add(parameter);
		} catch (ConstraintFailedException e) {
			System.out.println("Constraint failed on Parameter construction, this should not be possible.");
			e.printStackTrace();
		}
		addConstraint(new IntegerConstraint());
	}
	
	public IntegerParameter(String name, String description, boolean mandatory, Integer[] values) {
		super(name, description, mandatory, true);
		
		//this.value = value; TODO
		addConstraint(new IntegerConstraint());
	}	
	
	@Override
	public void setValue(String input) throws ConstraintFailedException {
		validateConstraints(input);
		Integer inputInt = Integer.parseInt(input);
		simpleSet(inputInt);
	}

	@Override
	public String getParameterString() {
		Integer value = getParameter();
		if(value!=null){
			return String.valueOf(value);
		}else{
			return "";
		}
	}

}
