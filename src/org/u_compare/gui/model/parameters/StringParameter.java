package org.u_compare.gui.model.parameters;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter{

	private String parameter;
	
	public StringParameter(String description, String parameter){
		super(description);
		
		this.parameter = parameter;
	}
	
	public String getParameter(){
		return parameter;
	}

	public void update(String input) throws ConstraintFailedException {
		
		if(!input.equals(parameter)){
			parameter = input;
			validateConstraints(input);	
			notifyParameterSettingsChangedListeners();
		}
		
	}

	public String getParameterString() {
		return parameter;
	}
	
}
