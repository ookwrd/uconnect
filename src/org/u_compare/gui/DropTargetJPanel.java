package org.u_compare.gui;

import java.awt.Color;
import java.awt.Dimension;
import org.u_compare.gui.control.DropTargetController;

@SuppressWarnings("serial")
public class DropTargetJPanel extends DroppableJPanel {

    public static final int TARGET_BORDER = 10;

	public DropTargetJPanel(DropTargetController controller) {
        super(controller);
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(TARGET_BORDER, TARGET_BORDER));
    }

    public void setDragOverHighlightingDroppable() {
       
    	setBackground(Color.BLUE);
    	setOpaque(true);
        this.repaint();
        
    }

    public void setDragOverHighlightingUndroppable() {
        setBackground(Color.RED);
        setOpaque(true);
        this.repaint();
    }

    public void clearDragOverHighlighting() {
        this.setOpaque(false);
        this.repaint();
    }
    
    /**
     * Special display property for drop target intermediate between two components.
     */
    public void setIntermediate(){

        this.setPreferredSize(new Dimension(TARGET_BORDER*2, TARGET_BORDER*2));
        
    }
    
    //TODO special display property for 

}
