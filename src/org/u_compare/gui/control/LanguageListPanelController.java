package org.u_compare.gui.control;

import javax.swing.JOptionPane;

import org.u_compare.gui.model.Component;

public class LanguageListPanelController {

	private Component component;
	
	public LanguageListPanelController(Component component){
		this.component = component;
	}
	
	public void addLanguage(){
		String value =  JOptionPane.showInputDialog("Please enter the value to add:");
		
		if(value == null){
			assert(false);
			System.out.println("Invalid value returned by string chooser.");
			return;
		}
		
		component.addLanguageType(value);
	}
	
	public void removeLanguage(String selected){
		component.removeLanguageType(selected);
	}
	
}
