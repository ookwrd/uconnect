/**
 * 
 */
package org.u_compare.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.LayoutManager;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serializable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.DragAndDropComponentController;
import org.u_compare.gui.control.DragAndDropController;
import org.u_compare.gui.debugging.GUITestingHarness;

/**
 * Define a draggable JPanel
 * 
 * @author olaf
 * @version 2010-06-01
 * 
 * Implementing DragGestureListener makes recognizing mouse gestures easier, since it varies from platform to platform 
 * 
 */
public abstract class DraggableJPanel extends DroppableJPanel implements
		DragGestureListener, Transferable, Serializable {

	//protected DragAndDropComponentController controller; //now IF : DragAndDropComponentController
	//protected DragAndDropComponentController controller; // defined in parent class !
	public final Color defaultColor = getBackground(); 
	private JPanel dropArea;
	private DropTarget dt;
	private boolean debug = true;
	
	protected static DataFlavor componentFlavor =
        new DataFlavor(Color.class, "A Color Object");
    protected static DataFlavor[] supportedFlavors = {
        componentFlavor,
        DataFlavor.stringFlavor,
    };
	
	/**
	 * JPanel that is draggable, and droppable
	 */
	public DraggableJPanel(ComponentController controller) {
		
		super(controller);
		//this.control = controller;
		
		/*dropArea = new JPanel();
		this.add(dropArea, BorderLayout.CENTER);
		dt = new DropTarget(dropArea, this);*/
		//System.out.println("created drop target");

		new MyDropTargetListener(this);

        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(this,
            DnDConstants.ACTION_COPY, this);
		
        //WorkflowComponentTransferHandler th = new WorkflowComponentTransferHandler();
		//this.setTransferHandler(th);
		
	}
	
	/**
	 * Used to identify what was being dragged, should be a more elegant solution.
	 * 
	 * @return
	 */
	public DragAndDropComponentController getController(){
		return controller;
	}

	/**
	 * Drag enters the component area
	 */
	public void dragEnter(DropTargetDragEvent dtde) {
		this.controller.setDragEnter();
		this.setBackground(Color.LIGHT_GRAY);
		System.out.println("DraggableJPanel: Drag enter");
	}

	/**
	 * Drag exits the component area
	 */
	public void dragExit(DropTargetEvent dte) {
		this.controller.setDragExit();
		this.setBackground(this.defaultColor);
		System.out.println("DraggableJPanel: Drag enter");
	}
	
	/**
	 * Drag over the component area
	 */
	public void dragOver(DropTargetDragEvent dtde) {
		// do nothing
	}

	/**
	 * 
	 */
	public void drop(DropTargetDropEvent dtde) {
		if (debug) System.out.println("Drop gesture recognized. ");
		// do nothing
	}

	public void dropActionChanged(DropTargetDragEvent ev) {
		if (debug) System.out.println("Drop action changed. ");
		this.setBackground(this.defaultColor);
		//ev.acceptDrag(ev.getDropAction());
	}
	
	// Methods implemented as a DragGestureListener
	
	public void dragGestureRecognized(DragGestureEvent evt) {
		if (debug) System.out.println("Drag gesture recognized. ");
		this.setBackground(this.defaultColor);
		Cursor cursor = null;
        JPanel panel = (JPanel) evt.getComponent();
        if (debug) if (controller==null) System.out.println("The variable <controller> is null.");
        this.controller.setDragged(); // inform the controller of the component being dragged 

        if (evt.getDragAction() == DnDConstants.ACTION_COPY) {
            cursor = DragSource.DefaultCopyDrop;
        }

        evt.startDrag(cursor, this); // TODO the parameters in this method call can be set to match a custom image -> to be modified later
	}
	
	/** Methods implemented as a Transferable */
	
	/**
	 * 
	 */
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	{
		/*
		if (flavor.equals(componentFlavor))
	         return this;
	    else if (flavor.equals(DataFlavor.stringFlavor)) 
	         return "A UIMA Workflow Component";
	    else 
	         //throw new UnsupportedFlavorException(flavor);
	    	System.out.println("Error under controller. ");
	    	*/
		return null;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return supportedFlavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(componentFlavor) || 
		        flavor.equals(DataFlavor.stringFlavor)) return true;
		return false;
	}
	
	// Methods implemented as a Serializable
	/*
	 private void writeObject(java.io.ObjectOutputStream out)
     throws IOException {
		 
	 }
     
	 private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException {
    	 
     }
	*/

	// Main Method
	
	public static void main(String[] args) {
		
		/*try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    } catch (Exception evt) {} // doesn't work, but well at least I tried
	  	*/
		GUITestingHarness.main(args);
	}
	
	/**
	 * Inner class defining a drop target listener for this JPanel
	 * 
	 * @author olaf
	 */
	class MyDropTargetListener extends DropTargetAdapter {

		private DropTarget dropTarget;
		private DraggableJPanel panel;

		public MyDropTargetListener(DraggableJPanel panel) {
			this.panel = panel;

			dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this,
					true, null);
		}

		/**
		 * Defines what happens when a drop occurs on the component
		 */
		public void drop(DropTargetDropEvent event) {

			System.out.println("Dropped on draggableJPanel. ");
			if (debug) this.panel.setBackground(Color.BLUE);
			//this.panel.setVisible(false);
			
			this.panel.controller.somethingDroppedOnComponent();
			
	        try {

	          Transferable tr = event.getTransferable();
	          String s = (String) tr.getTransferData(
	        		  new DataFlavor(WorkflowComponent.class, "WorkflowComponent"));
	          //System.out.println("String transfered : "+s);
	            if (event.isDataFlavorSupported(DataFlavor.stringFlavor)) {

	              event.acceptDrop(DnDConstants.ACTION_COPY);
	              //this.panel.setBackground(Color.BLUE);
	              event.dropComplete(true);
	              return;
	            }
	          event.rejectDrop();
	        } catch (Exception e) {
	          e.printStackTrace();
	          event.rejectDrop();
	        }
	      }

		//TODO ??? Is there any reason these following two methods need their own implementation here overiding those in DroppableJPanel?
		
		/**
		 * Drag enters the component area
		 */
		public void dragEnter(DropTargetDragEvent dtde) {
			this.panel.controller.setDragEnter();
			//this.panel.setBackground(Color.LIGHT_GRAY);
			//System.out.println("Drag enter");
		}

		/**
		 * Drag exits the component area
		 */
		public void dragExit(DropTargetEvent dte) {
			this.panel.controller.setDragExit();
			//this.panel.setBackground(this.panel.defaultColor);
		}

	}

}
