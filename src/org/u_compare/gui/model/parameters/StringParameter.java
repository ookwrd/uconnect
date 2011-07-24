package org.u_compare.gui.model.parameters;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter{

	private String parameter; //null represents unset. TODO change this to an arraylist
	
	public StringParameter(String name, String description, boolean mandatory, String parameter){
		super(name, description, mandatory, false);
		
		this.parameter = parameter;
	}
	
	public StringParameter(String name, String description, boolean mandatory, String[] parameter){
		super(name, description, mandatory, true);
		
		//this.parameter = parameter;TODO
	}
	
	public String getParameter(){
		return parameter;
	}

	public void setValue(String input) throws ConstraintFailedException {
		
		if(!input.equals(parameter)){
			parameter = input;
			validateConstraints(input);	
			notifyParameterValueChangedListeners();
		}
		
	}

	public String getParameterString() {
		if(parameter != null){
			return parameter;
		}else{
			return "";
		}
	}
}
