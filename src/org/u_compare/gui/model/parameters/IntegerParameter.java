package org.u_compare.gui.model.parameters;

import org.u_compare.gui.StringParameterPanel;
import org.u_compare.gui.control.IntegerConfigController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.constraints.Constraint;
import org.u_compare.gui.model.parameters.constraints.IntegerConstraint;

public class IntegerParameter extends AbstractParameter{

	private int value;
	
	public IntegerParameter(String description, int value) {
		super(description);
		
		this.value = value;
		addConstraint(new IntegerConstraint());
	}
	
	public int getParameter(){
		return value;
	}
	
	/*public void setParameter(int value){
		if(this.value != value){
			this.value = value;
			setChanged();
		}
	}*/
	
	//TODO should this be here?
	public StringParameterPanel getConfigurationPanel(IntegerConfigController controller, Component component) {
		return new StringParameterPanel(this, controller, Integer.toString(value), component);
	}
	
	@Override
	public boolean valid(String value){
		return false; //TODO make this actually work.
	}
		
	
	@Override
	public void update(String input) throws InvalidInputException {
		
		for(Constraint con : getConstraints()){
			con.validate(input);
		}
		
		//todo
		System.out.println("setting:" + input);
	
	}

	public String getParameterString() {
		return String.valueOf(value);
	}

}
