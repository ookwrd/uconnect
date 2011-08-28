package org.u_compare.gui;

import java.awt.Graphics;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static org.u_compare.gui.component.IconFactory.*;

import org.u_compare.gui.component.IconFactory;
import org.u_compare.gui.control.WorkflowViewerController;
import org.u_compare.gui.guiElements.ButtonTabFlap;
import org.u_compare.gui.guiElements.ButtonTabbedPane;
import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;
import org.u_compare.gui.model.Workflow.WorkflowStatusListener;

/**
 * Handles workflow tabs.
 * 
 * @author pontus
 * @author luke
 * @version 2009-09-27
 */

@SuppressWarnings("serial")
//TODO: Enable scrolling among tabs
//TODO: Should have mnemonics
public class WorkflowViewer extends ButtonTabbedPane
	implements WorkflowStatusListener, ChangeListener {
	
	private static final Icon EMPTY_ICON = new Icon() {//An empty icon.
		@Override
		public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {
		}
		@Override
		public int getIconWidth() {
			return 16;
		}
		@Override
		public int getIconHeight() {
			return 16;
		}
	};
	
	// Configuration
	private static final String TOOLTIP_TEXT =
		"Your current workflow(s)";
	/* The maximum number of characters displayed for a tab name */
	private static final int MAX_TITLE_LENGTH = 15;
	
	//XXX: We will conflict with the "Change tab" key shortcuts, override!
	
	private WorkflowViewerController controller;
	
	public WorkflowViewer(WorkflowViewerController controller) {
		super();
		
		IconFactory.loadIcons();
		
		assert IconFactory.iconsLoaded == true;
		
		this.controller = controller;
		
		this.setToolTipText(WorkflowViewer.TOOLTIP_TEXT);
		
		this.initializeTabButtons();
		
		this.addChangeListener(this);
	}
	
	private void initializeTabButtons(){
		
		if(WorkflowViewerController.SHOW_NEW_TAB){
			ButtonTabFlap newWorkflowButton = addButtonTab(WorkflowViewerController.NEW_TAB_NAME, controller);
			newWorkflowButton.setActionCommand(WorkflowViewerController.NEW_ACTION_COMMAND);
			newWorkflowButton.addActionListener(controller);	
		}
	
		if(WorkflowViewerController.SHOW_LOAD_TAB){
			ButtonTabFlap loadWorkflowButton = addButtonTab(WorkflowViewerController.LOAD_TAB_NAME, controller);
			loadWorkflowButton.setActionCommand(WorkflowViewerController.LOAD_ACTION_COMMAND);
			loadWorkflowButton.addActionListener(controller);
		}
	}
	
	//TODO should this really be public? and if so, should it be here? /luke
	public static String cleanTitle(String title){
		if(title.length() < MAX_TITLE_LENGTH){
			return title;
		}else{
			return title.substring(0, MAX_TITLE_LENGTH-3) + "...";
		}
	}

	public void addWorkflow(WorkflowHorizontalSplitPane splitPane) {
		
		Workflow workflow = splitPane.getWorkflowPane().getAssociatedWorkflow();
		workflow.registerWorkflowStatusListener(this);
		
		Component topComponent = splitPane.getWorkflowPane()
				.getTopWorkflowComponent().getComponent();
		
		int inserted_index = numberOfNonButtonTabs();
		
		splitPane.setName(cleanTitle(topComponent.getName()));
		this.add(splitPane, inserted_index);
		// Why does the API force a fully specified tab when using insert
		// rather than add?
		this.setToolTipTextAt(inserted_index, topComponent.getDescription());
		
		//Setup switching between tabs on component drag.
		DropTargetListener dropListener = new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				//Do Nothing
			}
			
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				//Get entered tabs index and set that tab selected.
				setSelectedIndex(indexOfTabComponent(((DropTarget)dtde.getSource()).getComponent()));	
			}
		};
		
		IconizedCloseableTabFlapComponent tabFlapComponent = 
			new IconizedCloseableTabFlapComponent(this, getIcon(STOPPED_ICON), dropListener, WorkflowViewerController.ALLOW_TAB_CLOSE);
		
		splitPane.linkTabbedPane(this, tabFlapComponent);
		
		this.setTabComponentAt(inserted_index,
				tabFlapComponent);
		
		this.setSelectedIndex(inserted_index);
	}
	
	@Override
	public void setIconAt(int index, Icon icon) {
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
				.setStatusIcon(icon, false);
	}
	
	/**
	 * Use this version if it is an icon that should be cleared when the tab is visited.
	 * 
	 * @param index
	 * @param icon
	 */
	public void setNotifactionIconAt(int index, Icon icon){
		if(index == getSelectedIndex()){
			clearIconAt(index);
			return;
		}
		
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
		.setStatusIcon(icon, true);
	}

	/**
	 *  see if we need to clear a notification Icon.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
        clearNotificationIconAt(getSelectedIndex());
	}
	
	public void clearNotificationIconAt(int index){
		if(this.getTabComponentAt(index) instanceof IconizedCloseableTabFlapComponent){
			IconizedCloseableTabFlapComponent tab = ((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index));
			if(tab.iconIsNotification()){
				clearIconAt(index);
			}
		}
	}
	
	public void clearIconAt(int index){
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index)).setStatusIcon(EMPTY_ICON, false);
	}
	
	@Override
	public void remove(int i) {
		controller.requestWorkflowClose(
				((WorkflowHorizontalSplitPane) this.getComponentAt(i))
				.getWorkflowPane().getAssociatedWorkflow());
	}
	
	// Use this instead of the internal one to conform with our selection
	// policies.
	private void safeRemove(int i) {
		//TODO Check with the control
		super.remove(i);
	}
	
	//TODO: We also need a safe add!

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		for (int i = 0; i < numberOfNonButtonTabs(); i++) {
			WorkflowHorizontalSplitPane tab =
				(WorkflowHorizontalSplitPane) this.getComponentAt(i);
			
			if (tab.getWorkflowPane()
					.getAssociatedWorkflow().equals(workflow)) {
				switch (workflow.getStatus()) {
					case RUNNING:
					case LOADING:
					case INITIALIZING:
						this.setIconAt(i, getIcon(RUNNING_ICON));
						break;
					case READY:
						this.setIconAt(i, getIcon(STOPPED_ICON));
						break;
					case ERROR:
						this.setNotifactionIconAt(i, getIcon(ERROR_ICON));	
						break;
					case PAUSED:
						this.setIconAt(i, getIcon(PAUSED_ICON));
						break;
					case FINISHED:
						this.setNotifactionIconAt(i, getIcon(FINISHED_ICON));	
						break;
					default:
						assert false: "Unimplemented WorkflowStatus received";
				}
			}
		}
	}
	
	public void removeWorkflow(Workflow workflow) {
		for (int i = 0; i < numberOfNonButtonTabs(); i++) {
			WorkflowHorizontalSplitPane currentComponent =
				(WorkflowHorizontalSplitPane) this.getComponentAt(i); 
			if (currentComponent.getWorkflowPane()
					.getAssociatedWorkflow().equals(workflow)) {
				this.safeRemove(i);
				break;
			}
		
		}
	}
}