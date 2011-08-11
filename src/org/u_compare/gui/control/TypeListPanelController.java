package org.u_compare.gui.control;

import org.u_compare.gui.annotationTypeChooser.AnnotationTypeChooser;
import org.u_compare.gui.annotationTypeChooser.BasicAnnotationTypeChooser;
import org.u_compare.gui.component.TypeListPanel;
import org.u_compare.gui.component.TypeListPanel.LIST_TYPES;
import org.u_compare.gui.model.AnnotationTypeOrFeature;
import org.u_compare.gui.model.Component;

public class TypeListPanelController {
	
	private ComponentController parentComponentController;
	private Component component;
	private LIST_TYPES listType;
	
	public TypeListPanelController(
			ComponentController parentComponentController,
			Component component, LIST_TYPES outputs){
		
		this.parentComponentController = parentComponentController;
		this.component = component;
		this.listType = outputs;
		
	}
	
	public void addAnnotation(){
		
		AnnotationTypeOrFeature newType = WorkflowPaneController.typeChooser.getNewAnnotation();
		
		if(newType == null || newType.getTypeName().equals("")){
			System.out.println("Invalid Type returned by type chooser.");
			return;
		}
		
		if(listType == TypeListPanel.LIST_TYPES.INPUTS){
			component.addInputType(newType);
		}else if (listType == TypeListPanel.LIST_TYPES.OUTPUTS){
			component.addOutputType(newType);
		}else{
			//TODO this state shouldnt occur
		}
		
	}
	
	public void removeAnnotation(String selected){
		
		System.out.println("Removing");
		
		AnnotationTypeOrFeature type = new AnnotationTypeOrFeature(selected);
		
		if(listType == TypeListPanel.LIST_TYPES.INPUTS){
			component.removeInputType(type);
		}else if (listType == TypeListPanel.LIST_TYPES.OUTPUTS){

			System.out.println("here, tyring to remove outptu type");
			component.removeOutputType(type);
		}else{
			//TODO this state shouldnt occur
		}
		
	}
	
}
