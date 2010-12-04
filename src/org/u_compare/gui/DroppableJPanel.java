package org.u_compare.gui;

import java.awt.Color;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;

import javax.swing.JPanel;

import org.u_compare.gui.control.DragAndDropComponentController;

/**
 * Drop target component in the GUI able to accept component drops
 * 
 * @author olaf
 * 
 */
@SuppressWarnings("serial")
public abstract class DroppableJPanel extends JPanel {

	// protected DragAndDropComponentController controller;
	private boolean debug = false;
	protected DragAndDropComponentController controller;
	protected final Color defaultColor = getBackground();

	public DroppableJPanel(DragAndDropComponentController controller) {

		this.controller = controller;
		new MyDropTargetListener(this);

	}

	/**
	 * Inner class defining a drop target listener for this JPanel
	 * 
	 * @author olaf
	 */
	class MyDropTargetListener extends DropTargetAdapter {
		private DroppableJPanel panel;

		public MyDropTargetListener(DroppableJPanel dropTargetPanel) {
			this.panel = dropTargetPanel;

			new DropTarget(dropTargetPanel,
					DnDConstants.ACTION_COPY, this, true, null);
		}

		/**
		 * Defines what happens when a drop occurs on the drop target
		 */
		public void drop(DropTargetDropEvent event) {

			System.out.println("Dropped on DroppableJPanel. ");

			this.panel.controller.somethingDroppedOnComponent();

			if (debug)
				this.panel.setBackground(Color.MAGENTA);
			else
				this.panel.setBackground(defaultColor); // if a non accepted
														// drop has been
														// performed, remove the
														// highlighting

			/*
			 * //this was previously to handle drops from outside the app, on
			 * wfcomponents
			 * 
			 * try {
			 * 
			 * Transferable tr = event.getTransferable(); String s = (String)
			 * tr.getTransferData(new DataFlavor( ComponentPanel.class,
			 * "WorkflowComponent")); System.out.println("String transfered : "
			 * + s); if (event.isDataFlavorSupported(DataFlavor.stringFlavor)) {
			 * 
			 * event.acceptDrop(DnDConstants.ACTION_COPY);
			 * event.dropComplete(true); return; } event.rejectDrop(); } catch
			 * (Exception e) { e.printStackTrace(); event.rejectDrop(); }
			 */
		}

		/**
		 * Drag enters the component area
		 */
		public void dragEnter(DropTargetDragEvent dtde) {

			System.out.println("DroppableJPanel: Drag enter");
			this.panel.controller.setDragEnter();
			// this.panel.setBackground(Color.LIGHT_GRAY);
		}

		/**
		 * Drag exits the component area
		 */
		public void dragExit(DropTargetEvent dte) {

			System.out.println("DroppableJPanel: Drag exit");
			this.panel.controller.setDragExit();
			// this.panel.setBackground(this.panel.defaultColor);
		}

	}

}
