package org.u_compare.gui;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;

/**
 * Component to be used as a tab in a JTabbedPane which switches tab when dragging.
 * 
 * @author Luke McCrohon
 *
 */
@SuppressWarnings("serial")
public class WorkflowTabFlap extends IconizedCloseableTabFlapComponent {

	final private JTabbedPane tabbedPane;
	
	public WorkflowTabFlap(JTabbedPane parentPane, Icon statusIcon, boolean tabsCloseable) {
		super(parentPane, statusIcon, tabsCloseable);
		
		tabbedPane = parentPane;
		
		new DropTarget(this, new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
			}
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {//Switch to that tab
				tabbedPane.setSelectedIndex(tabbedPane.indexOfTabComponent(WorkflowTabFlap.this));
			}
		});		
	}
}
