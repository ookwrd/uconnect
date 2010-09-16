package org.u_compare.gui.control;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.StringConfigPanel;
import org.u_compare.gui.model.parameters.InvalidInputException;
import org.u_compare.gui.model.parameters.StringParameter;

public class StringConfigController implements ConfigController, ActionFocusListener {

	private StringParameter param;
	private ComponentController parent;
	private StringConfigPanel view;
	
	public StringConfigController(ComponentController control,
			StringParameter param) {
		this.parent = control;
		this.param = param;
		this.view = param.getConfigurationPane(this);
	}

	public ParameterPanel getView() {//TODO who calls this?
		return view;
	}

	public void actionPerformed(ActionEvent e) {
		
		attemptUpdate(e);
		
	}

	public void focusGained(FocusEvent e) {
		//IGNORE
	}

	//Moving the mouse away
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
			}
			
		}else{
			//TODO
		}
		
	}
	
}
