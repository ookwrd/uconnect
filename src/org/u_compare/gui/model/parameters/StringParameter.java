package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter<String>{

	public StringParameter(String name, String description, boolean mandatory, String parameter) {
		super(name, description, mandatory, false);
		try {
			add(parameter);
		} catch (ConstraintFailedException e) {
			System.out.println("Constraint failed on Parameter construction, this should not be possible.");
			e.printStackTrace();
		}
	}
	
	public StringParameter(String name, String description, boolean mandatory, String[] parameter) {
		super(name, description, mandatory, true);
		
		//this.parameter = parameter;TODO
	}

	@Override
	public void setValue(String input) throws ConstraintFailedException {
		validateConstraints(input);
		simpleSet(input);
	}

	@Override //TODO push into abstract base class.
	public String getParameterString() {
		String parameter = getParameter();
		if(parameter != null){
			return parameter;
		}else{
			return "";
		}
	}
}
