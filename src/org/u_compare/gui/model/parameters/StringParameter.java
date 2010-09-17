package org.u_compare.gui.model.parameters;

import org.u_compare.gui.StringConfigPanel;
import org.u_compare.gui.control.StringConfigController;

public class StringParameter extends AbstractParameter{

	private String parameter;
	
	public StringParameter(String description, String parameter){
		super(description);
		
		this.parameter = parameter;
	}
	
	public String getParameter(){
		return parameter;
	}
	
	/*public void setParameter(String parameter){
		if(!this.parameter.equals(parameter)){
			this.parameter = parameter;
			setChanged();
		}
	}*/

	public StringConfigPanel getConfigurationPane(StringConfigController controller) {
		return new StringConfigPanel(this, controller, parameter);
	}

	public void update(String input) throws InvalidInputException {
		
		System.out.println("setting:" + input);
	
	}

	public String getParameterString() {
		return parameter;
	}
	
}
