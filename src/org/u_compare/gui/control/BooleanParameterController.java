package org.u_compare.gui.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.u_compare.gui.BooleanParameterPanel;
import org.u_compare.gui.ParameterPanel;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.parameters.BooleanParameter;
import org.u_compare.gui.model.parameters.InvalidInputException;


public class BooleanParameterController implements ParameterController {

	private BooleanParameter param;
	private ComponentController parent;
	private BooleanParameterPanel view;
	private Component component;
	
	public BooleanParameterController(ComponentController parent, BooleanParameter param, Component component){
		this.param = param;
		this.parent = parent;
		this.component = component;
		this.view = new BooleanParameterPanel(param, this, component);
	}
	
	public ParameterPanel getView(){
		return view;
	}

/*	public void actionPerformed(ActionEvent e) {
		
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
		
	}*/
	
	public void setValue(boolean value){
		assert(!component.getLockedStatus());
	
		try{
			param.update(value);
		}catch(Exception e){
			//TODO can there be such a thing as an invalid boolean?
		}
	}
}
