package org.u_compare.gui;

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
 * View component which handles the display of multiple workflows in separate
 * tabs.
 * 
 * @author pontus
 * @author luke
 * @version 2009-09-27
 */

@SuppressWarnings("serial")
// TODO: Should have mnemonics
public class WorkflowViewer extends ButtonTabbedPane implements
		WorkflowStatusListener, ChangeListener {

	// Configuration
	private static final String TOOLTIP_TOOLTIP = "Your current workflow(s)";
	private static final String NEW_TAB_TOOLTIP = "Construct a new workflow";
	private static final String LOAD_TAB_TOOLTIP = "Load a workflow";

	/* The maximum number of characters displayed for a tab name */
	private static final int MAX_TITLE_LENGTH = 15;

	// XXX: We will conflict with the "Change tab" key shortcuts, override!

	private WorkflowViewerController controller;

	public WorkflowViewer(WorkflowViewerController controller) {
		super();

		IconFactory.loadIcons();

		assert IconFactory.iconsLoaded == true;

		this.controller = controller;

		this.setToolTipText(WorkflowViewer.TOOLTIP_TOOLTIP);

		this.initializeTabButtons();

		this.addChangeListener(this);
	}

	private void initializeTabButtons() {

		if (WorkflowViewerController.SHOW_NEW_TAB) {
			ButtonTabFlap newWorkflowButton = addButtonTab(
					WorkflowViewerController.NEW_TAB_NAME, controller);
			newWorkflowButton
					.setActionCommand(WorkflowViewerController.NEW_ACTION_COMMAND);
			newWorkflowButton.setToolTipText(NEW_TAB_TOOLTIP);
			newWorkflowButton.addActionListener(controller);
		}

		if (WorkflowViewerController.SHOW_LOAD_TAB) {
			ButtonTabFlap loadWorkflowButton = addButtonTab(
					WorkflowViewerController.LOAD_TAB_NAME, controller);
			loadWorkflowButton
					.setActionCommand(WorkflowViewerController.LOAD_ACTION_COMMAND);
			loadWorkflowButton.setToolTipText(LOAD_TAB_TOOLTIP);
			loadWorkflowButton.addActionListener(controller);
		}
	}

	// TODO should this really be public? and if so, should it be here? /luke
	public static String cleanTitle(String title) {
		if (title.length() < MAX_TITLE_LENGTH) {
			return title;
		} else {
			return title.substring(0, MAX_TITLE_LENGTH - 3) + "...";
		}
	}

	public void addWorkflow(WorkflowHorizontalSplitPane splitPane) {

		Workflow workflow = splitPane.getWorkflowPane().getAssociatedWorkflow();
		workflow.registerWorkflowStatusListener(this);

		Component topComponent = splitPane.getWorkflowPane()
				.getTopWorkflowComponent().getComponent();

		final int inserted_index = numberOfNonButtonTabs();

		splitPane.setName(cleanTitle(topComponent.getName()));
		this.add(splitPane, inserted_index);
		// Why does the API force a fully specified tab when using insert
		// rather than add?

		
		// Setup switching between tabs on component drag.
		DropTargetListener dropListener = new DropTargetAdapter() {
			@Override
			public void drop(DropTargetDropEvent dtde) {
				// Do Nothing
			}

			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				// Get entered tabs index and set that tab selected.
				setSelectedIndex(indexOfTabComponent(((DropTarget) dtde
						.getSource()).getComponent()));
			}
		};

		final IconizedCloseableTabFlapComponent tabFlapComponent = new IconizedCloseableTabFlapComponent(
				this, getIcon(STOPPED_ICON), dropListener,
				WorkflowViewerController.ALLOW_TAB_CLOSE);
		
		
		setTooltip(topComponent, inserted_index, tabFlapComponent);
		//Update tooltips as title and description change
		topComponent.registerComponentDescriptionChangeListener(new Component.ListenerAdaptor(){
			@Override
			public void ComponentDescriptionChanged(Component component) {
				setTooltip(component, inserted_index, tabFlapComponent);
			}
		});

		//Add a "*" to unsaved workflows.
		topComponent.registerSavedStatusChangeListener(new Component.ListenerAdaptor(){
			@Override
			public void savedStatusChanged(Component component) {
				if(component.checkUnsavedChanges()) {
					setTitleAt(inserted_index, "*" + WorkflowViewer.cleanTitle(component.getName()));
				} else {
					setTitleAt(inserted_index, WorkflowViewer.cleanTitle(component.getName()));
				}
			}
		});
		
		splitPane.linkTabbedPane(this, tabFlapComponent);//TODO can I get rid of this?

		this.setTabComponentAt(inserted_index, tabFlapComponent);

		this.setSelectedIndex(inserted_index);
	}

	private void setTooltip(Component component, int index, IconizedCloseableTabFlapComponent tabFlapComponent){
		String tooltip = "<html>" + wrap(component.getName() + "\n" +component.getDescription(),40,4);//Need <html> to allow multiline tooltip
		setToolTipTextAt(index, tooltip);
		tabFlapComponent.setToolTipText(tooltip);
	}
	
	@Override
	public void setIconAt(int index, Icon icon) {
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
				.setStatusIcon(icon, false);
	}

	/**
	 * Use this version if it is an icon that should be cleared when the tab is
	 * visited.
	 * 
	 * @param index
	 * @param icon
	 */
	public void setNotifactionIconAt(int index, Icon icon) {
		if (index == getSelectedIndex()) {
			((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
					.clearIcon();// Clear anything preexisting
			return;
		}

		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
				.setStatusIcon(icon, true);
	}

	/**
	 * see if we need to clear a notification Icon.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		clearNotificationIconAt(getSelectedIndex());
	}

	public void clearNotificationIconAt(int index) {
		if (this.getTabComponentAt(index) instanceof IconizedCloseableTabFlapComponent) {
			IconizedCloseableTabFlapComponent tab = ((IconizedCloseableTabFlapComponent) this
					.getTabComponentAt(index));
			tab.clearNotification();
		}
	}

	/**
	 * Handles user request to close tab i. Checks with associated controller.
	 */
	@Override
	public void remove(int i) {
		controller.requestWorkflowClose(((WorkflowHorizontalSplitPane) this
				.getComponentAt(i)).getWorkflowPane().getAssociatedWorkflow());
	}

	@Override
	public void workflowStatusChanged(Workflow workflow) {
		for (int i = 0; i < numberOfNonButtonTabs(); i++) {
			WorkflowHorizontalSplitPane tab = (WorkflowHorizontalSplitPane) this
					.getComponentAt(i);

			if (tab.getWorkflowPane().getAssociatedWorkflow().equals(workflow)) {
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
					assert false : "Unimplemented WorkflowStatus received";
				}
			}
		}
	}

	/**
	 * Should only be called from the control.
	 * 
	 * @param workflow
	 */
	public void confirmedRemoveWorkflow(Workflow workflow) {
		for (int i = 0; i < numberOfNonButtonTabs(); i++) {
			WorkflowHorizontalSplitPane currentComponent = (WorkflowHorizontalSplitPane) this
					.getComponentAt(i);
			if (currentComponent.getWorkflowPane().getAssociatedWorkflow()
					.equals(workflow)) {
				super.remove(i);
				break;
			}

		}
	}

	/**
	 * Word wrap algorithm for tooltips. 
	 * 
	 * Based on http://ramblingsrobert.wordpress.com/2011/04/13/java-word-wrap-algorithm/
	 */
	public String wrap(String in, int len, int lines) {
		in = in.trim();
		
		if (in.length() < len) {
			return in;
		}else if (lines <= 1){
			return in.substring(0,len-3) + "...";
		}
		
		if (in.substring(0, len).contains("\n")) {
			return in.substring(0, in.indexOf("\n")).trim() + "<br>"
					+ wrap(in.substring(in.indexOf("\n") + 1), len, lines-1);
		}
		
		int place = Math.max(
				Math.max(in.lastIndexOf(" ", len), in.lastIndexOf("\t", len)),
				in.lastIndexOf("-", len));
		
		return in.substring(0, place).trim() + "<br>"
				+ wrap(in.substring(place), len, lines-1);
	}
}