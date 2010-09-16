package org.u_compare.gui.control;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.StringConfigPanel;
import org.u_compare.gui.model.parameters.IntegerParameter;
import org.u_compare.gui.model.parameters.InvalidInputException;

public class IntegerConfigController implements ConfigController, ActionFocusListener{

	private IntegerParameter param;
	private ComponentController parent;
	private StringConfigPanel view;//They look the same
	
	public IntegerConfigController(ComponentController control,
			IntegerParameter param) {
		this.parent = control;
		this.param = param;
		this.view = param.getConfigurationPane(this);
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
		
		if(parent.allowChanges()){
			
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
