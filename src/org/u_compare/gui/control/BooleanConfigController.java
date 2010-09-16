package org.u_compare.gui.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.u_compare.gui.BooleanConfigPanel;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.InvalidInputException;


public class BooleanConfigController implements ActionListener, ConfigController {

	private BooleanParameter param;
	private ComponentController parent;
	private BooleanConfigPanel view;
	
	public BooleanConfigController(ComponentController parent, BooleanParameter param){
		this.param = param;
		this.parent = parent;
		this.view = param.getConfigurationPane(this);
	}
	
	public ParameterPanel getView(){
		return view;
	}

	public void actionPerformed(ActionEvent e) {
		
		if(parent.allowChanges()){
			
			boolean value = view.getValue();
			
			try{
				param.update(value);
			
			}catch(InvalidInputException ex) {//TODO
				System.out.println(ex.getMessage());
			}
			
		}else{
			//TODO
		}
		
	}
}
