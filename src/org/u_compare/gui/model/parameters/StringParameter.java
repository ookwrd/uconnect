package org.u_compare.gui.model.parameters;
import java.util.ArrayList;

import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter{

	private ArrayList<String> parametersArrayList = new ArrayList<String>(); //empty represents unset.
	
	public StringParameter(String name, String description, boolean mandatory, String parameter){
		super(name, description, mandatory, false);
		parametersArrayList.add(0, parameter);//TODO this should use an add method locally to ensure constraints.
	}
	
	public StringParameter(String name, String description, boolean mandatory, String[] parameter){
		super(name, description, mandatory, true);
		
		//this.parameter = parameter;TODO
	}
	
	public String getParameter(){
		if(parametersArrayList.size()>0){
			return parametersArrayList.get(0);
		} else {
			return null;
		}
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
