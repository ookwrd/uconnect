package org.u_compare.gui.control;

/**
 * Singleton class for storing the most recently dragged Transferable object.
 * 
 */
public class DragAndDropController {

    static private final DragAndDropController thereCanOnlyBeOne = new DragAndDropController();
    
    private ComponentController dragged;
    
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
    
    public void setDragged(ComponentController dragged){
        this.dragged = dragged;
    }
    
    public void resetDragged(){
        this.dragged = null;
    }
 
    public ComponentController getDraggedComponent() throws ClassCastException{
    	
    	if(dragged instanceof ComponentController){
    		return (ComponentController)dragged;
    	} else if ( false /*dragged instance of Library Descriptor*/ ){
    		
    		//TODO build the component from the descriptor
    		return null;
    		
    	} else{
    		throw new ClassCastException("DragAndDropController can't convert input to a ComponentController: " + dragged.getClass());
    	}
    }
}