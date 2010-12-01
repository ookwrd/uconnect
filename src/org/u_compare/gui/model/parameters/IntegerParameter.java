package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;
import org.u_compare.gui.model.parameters.constraints.IntegerConstraint;

public class IntegerParameter extends AbstractParameter{

	private int value;
	
	public IntegerParameter(String name, String description, int value) {
		super(name, description);
		
		this.value = value;
		addConstraint(new IntegerConstraint());
	}
	
	public int getParameter(){
		return value;
	}	
	
	@Override
	public void update(String input) throws ConstraintFailedException {

		validateConstraints(input);
		
		int inputInt = Integer.parseInt(input);
	
		if(inputInt != value){	
			value = inputInt;
			notifyParameterSettingsChangedListeners();
		}
		
	}

	public String getParameterString() {
		return String.valueOf(value);
	}

}
