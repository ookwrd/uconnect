package org.u_compare.gui;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;

/**
 * Sets the tab as a DropTargetListener to allow dragging of components between tabs.
 * 
 * @author Luke Mccrohon
 *
 */
@SuppressWarnings("serial")
public class WorkflowTabFlapComponent extends IconizedCloseableTabFlapComponent implements DropTargetListener {

	JTabbedPane parentPane;
	
	public WorkflowTabFlapComponent(JTabbedPane parentPane, Icon statusIcon) {
		super(parentPane, statusIcon);
		
		this.parentPane = parentPane;
		
		new DropTarget(this, this);
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {

		//Switch to that tab
		parentPane.setSelectedIndex(parentPane.indexOfTabComponent(this));
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		
	}


}
