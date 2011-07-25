package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter<String>{

	public StringParameter(String name, String description, boolean mandatory, String parameter) {
		super(name, description, mandatory, false);
		setInitial(parameter);
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
	
	public static void main(String[] args){
		StringParameter param = new StringParameter("Name", "Desc", true, (String)null);
		System.out.println(param.getParameter());
	}
}
