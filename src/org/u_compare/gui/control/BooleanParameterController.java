package org.u_compare.gui.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.u_compare.gui.BooleanParameterPanel;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.InvalidInputException;


public class BooleanParameterController implements ActionListener, ParameterController {

	private BooleanParameter param;
	private ComponentController parent;
	private BooleanParameterPanel view;
	
	public BooleanParameterController(ComponentController parent, BooleanParameter param){
		this.param = param;
		this.parent = parent;
		this.view = param.getConfigurationPane(this);
	}
	
	public ParameterPanel getView(){
		return view;
	}

	public void actionPerformed(ActionEvent e) {
		
		if(!parent.isLocked()){
			
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