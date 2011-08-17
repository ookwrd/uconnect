package org.u_compare.gui.guiElements;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.IOException;

public class DragAndDrop {
	
	public static void registerDropTarget(final Component target, final DropController controller){
		new DropTarget(target, new DropTargetAdapter(){		
			@Override
			public void drop(DropTargetDropEvent arg0) {
				controller.somethingDroppedOnComponent();	
			}
			
			@Override
			public void dragExit(DropTargetEvent arg0) {
				controller.setDragExit();
			}
			
			@Override
			public void dragEnter(DropTargetDragEvent arg0) {
				controller.setDragEnter();			
			}
		});
	}
	
	public interface DropController {
		/**
		 * The current dragged component has been dropped on this component.
		 */
		public void somethingDroppedOnComponent();
		
		/**
		 * The currently dragged component is now being dragged over this component.
		 */
		public void setDragEnter();
		
		/**
		 * The currently dragged component is no longer being dragged over this component.
		 */
		public void setDragExit();
	}
	
	public static void registerDragSource(final Component component, final DragController controller){
		DragSource ds = DragSource.getDefaultDragSource();
		ds.createDefaultDragGestureRecognizer(component, DnDConstants.ACTION_COPY,
				new DragGestureListener(){
					@Override
					public void dragGestureRecognized(DragGestureEvent dge) {
						controller.setDragged();
						dge.startDrag(DragSource.DefaultCopyDrop, new FakeTransferable());
					}
			
					class FakeTransferable implements Transferable {
						@Override public boolean isDataFlavorSupported(DataFlavor flavor) {
							return false; 
						}
						@Override public DataFlavor[] getTransferDataFlavors() {
							return new DataFlavor[]{ };
						} 
						@Override public Object getTransferData(DataFlavor flavor)
								throws UnsupportedFlavorException, IOException {
							return null;
						}
					}
		});
	}
	
	public interface DragController{
		/**
		 * This component has been dragged. Should notify central component dragged location.
		 */
		public void setDragged();
	}
	
}
