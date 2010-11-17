package org.u_compare.gui.control;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.StringParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.InvalidInputException;

public class IntegerConfigController implements ParameterController, ActionFocusListener{

	private IntegerParameter param;
	private ComponentController parent;
	private StringParameterPanel view;//They look the same
	private Component component;
	
	public IntegerConfigController(ComponentController control,
			IntegerParameter param, Component component) {
		this.parent = control;
		this.param = param;
		this.component = component;
		this.view = param.getConfigurationPanel(this, component);
	}

	public ParameterPanel getView() {
		return view;
	}

	public void actionPerformed(ActionEvent e) {
		attemptUpdate(e);
	}

	public void focusGained(FocusEvent e) {
		//IGNORE
	}

	public void focusLost(FocusEvent e) {
		attemptUpdate(e);
	}
	
	private void attemptUpdate(AWTEvent e){
		
		if(!parent.isLocked()){
			
			String input = view.getString();
			
			try{
				param.update(input);
			}catch(InvalidInputException ex) {//TODO
				System.out.println(ex.getMessage());
				
				resetUpdate();
			}
			
		}else{
			//TODO
		}	
	}
	
	private void resetUpdate(){
		view.setString(param.getParameterString());
	}
}
