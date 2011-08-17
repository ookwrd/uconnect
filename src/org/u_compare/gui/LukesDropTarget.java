package org.u_compare.gui;

import java.awt.Color;
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

import org.u_compare.gui.control.DragAndDropComponentController;

public class LukesDropTarget {
	
	public static void registerDropTarget(final Component target, final DragAndDropComponentController controller){
		new DropTarget(target, new DropTargetAdapter(){
			
			@Override
			public void drop(DropTargetDropEvent arg0) {
				System.out.println("Drop Lukes Target");
				controller.somethingDroppedOnComponent();	
			}
			
			@Override
			public void dragExit(DropTargetEvent arg0) {
				System.out.println("Drag exit Lukes target");
				controller.setDragExit();
			}
			
			@Override
			public void dragEnter(DropTargetDragEvent arg0) {
				controller.setDragEnter();
				System.out.println("Drag enter Lukes target");				
			}
		});
	}
	
	public static void registerDragSource(final Component component, final DragAndDropComponentController controller){
		DragSource ds = DragSource.getDefaultDragSource();
		ds.createDefaultDragGestureRecognizer(component, DnDConstants.ACTION_COPY,
				new DragGestureListener(){
			
					@Override
					public void dragGestureRecognized(DragGestureEvent dge) {
						System.out.println("Lukes drag gesture recognized");
						
						controller.setDragged();
						
						dge.startDrag(DragSource.DefaultCopyDrop, new Transferable() {//TODO extract to fakeTransferable
							protected /*static*/ DataFlavor componentFlavor = new DataFlavor(Color.class,
							"A Color Object/wtf in DragSource??");
							@Override public boolean isDataFlavorSupported(DataFlavor flavor) {
								return flavor.equals(componentFlavor);
							}
							@Override public DataFlavor[] getTransferDataFlavors() {
								return new DataFlavor[]{ componentFlavor };
							} 
							@Override public Object getTransferData(DataFlavor flavor)
									throws UnsupportedFlavorException, IOException {
								return null;
							}
						});
					}
			
		});
	}
	
}
