package org.u_compare.gui.model.parameters;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter<String>{

	public StringParameter(String name, String description, boolean mandatory, String parameter){
		super(name, description, mandatory, false);
		parametersArrayList.add(0, parameter);//TODO this should use an add method locally to ensure constraints.
	}
	
	public StringParameter(String name, String description, boolean mandatory, String[] parameter){
		super(name, description, mandatory, true);
		
		//this.parameter = parameter;TODO
	}

	public void setValue(String input) throws ConstraintFailedException {
		
		if(!input.equals(getParameter())){
			validateConstraints(input);	
			parametersArrayList.set(0, input);
			notifyParameterValueChangedListeners();
		}
		
	}

	public String getParameterString() {
		String parameter = getParameter();
		if(parameter != null){
			return parameter;
		}else{
			return "";
		}
	}
}
