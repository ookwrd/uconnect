package org.u_compare.gui.control;

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
		
		AnnotationTypeOrFeature newType = WorkflowViewerController.typeChooser.getNewAnnotation();
		
		if(newType == null || newType.getTypeName().equals("")){
			assert(false);
			System.out.println("Invalid Type returned by type chooser.");
			return;
		}
		
		if(listType == TypeListPanel.LIST_TYPES.INPUTS){
			component.addInputType(newType);
		}else if (listType == TypeListPanel.LIST_TYPES.OUTPUTS){
			component.addOutputType(newType);
		}else{
			assert(false);
			//This state should never occur
		}
		
	}
	
	public void removeAnnotation(String selected){
		AnnotationTypeOrFeature type = new AnnotationTypeOrFeature(selected);
		
		if(listType == TypeListPanel.LIST_TYPES.INPUTS){
			component.removeInputType(type);
		}else if (listType == TypeListPanel.LIST_TYPES.OUTPUTS){
			component.removeOutputType(type);
		}else{
			assert(false);
			//this state shouldnt occur
		}
	}
}
