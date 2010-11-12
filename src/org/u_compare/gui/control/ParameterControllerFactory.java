package org.u_compare.gui.control;

import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.Parameter;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.StringParameter;

public class ParameterControllerFactory {

	public static ParameterController getController(ComponentController control, Parameter param){
		
		if(param instanceof BooleanParameter){
		
			return new BooleanParameterController(control, (BooleanParameter)param);
			
		}else if (param instanceof StringParameter){
			
			return new StringParamaterController(control, (StringParameter)param);
			
		}else if (param instanceof IntegerParameter){
			
			return new IntegerConfigController(control, (IntegerParameter)param);

		}else{
		
			//TODO FIX THIS
			System.out.println("Invalid type passed in to parameter controller factory");
			return null;
			
		}
	} 
	
}
