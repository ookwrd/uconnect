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
	private final boolean debug = false;
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

			new DropTarget(dropTargetPanel, DnDConstants.ACTION_COPY, this,
					true, null);
		}

		/**
		 * Defines what happens when a drop occurs on the drop target
		 */
		public void drop(DropTargetDropEvent event) {
			this.panel.controller.somethingDroppedOnComponent();
		}

		/**
		 * Drag enters the component area
		 */
		public void dragEnter(DropTargetDragEvent dtde) {
			this.panel.controller.setDragEnter();
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
