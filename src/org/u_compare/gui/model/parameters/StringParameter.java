package org.u_compare.gui.model.parameters;
import org.u_compare.gui.model.parameters.constraints.ConstraintFailedException;

public class StringParameter extends AbstractParameter{

	private String parameter; //null represents unset.
	
	public StringParameter(String name, String description, Boolean mandatory, String parameter){
		super(name, description, mandatory);
		
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
		if(parameter != null){
			return parameter;
		}else{
			return "";
		}
	}

	@Override
	public boolean isMultivalued() {
		return false;
	}
	
}
