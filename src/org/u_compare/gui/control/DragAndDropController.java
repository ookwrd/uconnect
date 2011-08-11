package org.u_compare.gui.control;

import org.u_compare.gui.DraggableJPanel;

/**
 * Singleton class for storing the most recently dragged Transferable object.
 * 
 */
public class DragAndDropController {

    static private final DragAndDropController thereCanOnlyBeOne = new DragAndDropController();
    
    private DraggableJPanel dragged;
    
    /**
     * Private to ensure singleton.
     */
    private DragAndDropController(){
    }
    
    /**
     * Factory method for getting reference to singleton DragAndDropController.
     * 
     * @return    The singleton DragAndDropController.
     */
    static public DragAndDropController getController(){
        return thereCanOnlyBeOne;
    }
    
    public void setDragged(DraggableJPanel dragged){
        this.dragged = dragged;
    }
    
    public void resetDragged(){
        this.dragged = null;
    }
    
    public DraggableJPanel getDragged(){
        return dragged;
    }
 
    public ComponentController getDraggedComponent() throws ClassCastException{
    	
    	if(dragged.getController() instanceof ComponentController){
    		return (ComponentController)dragged.getController();
    	} else if ( false /*dragged.getController() instance of Library Descriptor*/ ){
    		
    		//TODO build the component from the descriptor
    		return null;
    		
    	} else{
    		throw new ClassCastException("DragAndDropController can't convert input DraggableJPanel to a ComponentController");
    	}
    }
}