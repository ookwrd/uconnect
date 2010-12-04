package org.u_compare.gui.control;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.u_compare.gui.component.AnnotationTypeChooser;
import org.u_compare.gui.component.BasicAnnotationTypeChooser;
import org.u_compare.gui.component.TypeListPanel;
import org.u_compare.gui.model.AnnotationType;
import org.u_compare.gui.model.Component;

public class TypeListPanelController {

	//Default Annotation Type Chooser, replaceable via setTypeChooser method
	private AnnotationTypeChooser typeChooser = new BasicAnnotationTypeChooser();
	
	private ComponentController parentComponentController;
	private Component component;
	private int listType;
	
	public TypeListPanelController(ComponentController parentComponentController, Component component, int listType){
		
		this.parentComponentController = parentComponentController;
		this.component = component;
		this.listType = listType;
		
	}
	
	public void addAnnotation(){
		
		AnnotationType newType = typeChooser.getNewAnnotation();
		
		if(listType == TypeListPanel.INPUTS_LIST){
			component.addInputType(newType);
		}else if (listType == TypeListPanel.OUTPUTS_LIST){
			component.addOutputType(newType);
		}else{
			//TODO this state shouldnt occur
		}
		
	}
	
	public void removeAnnotation(String selected){
		
		System.out.println("Removing");
		
		AnnotationType type = new AnnotationType(selected);
		
		if(listType == TypeListPanel.INPUTS_LIST){
			component.removeInputType(type);
		}else if (listType == TypeListPanel.OUTPUTS_LIST){

			System.out.println("here, tyring to remove outptu type");
			component.removeOutputType(type);
		}else{
			//TODO this state shouldnt occur
		}
		
	}
	
	public void setTypeChooser(AnnotationTypeChooser typeChooser){
		
		if(typeChooser == null){
			throw new InvalidParameterException("Specified AnnotationTypeChooser is null");
		}
		
		this.typeChooser = typeChooser;
		
	}
	
}
