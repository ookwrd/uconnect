package org.u_compare.gui;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import org.u_compare.gui.control.WorkflowPaneController;

@SuppressWarnings("serial")
public class ButtonTab extends JButton implements DropTargetListener {

	private WorkflowPaneController controller;
	
	public ButtonTab(String string, WorkflowPaneController controller){
		super(string);
		
		this.controller = controller;
		
		setOpaque(false);
		setBorder(new EmptyBorder(0,0,0,0));
		
		new DropTarget(this,this);
	}
	
	@Override
	public void drop(DropTargetDropEvent arg0) {
		controller.requestNewWorkflowDragged();
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		
	}

}
