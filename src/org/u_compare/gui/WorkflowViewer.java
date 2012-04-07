package org.u_compare.gui;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
public class WorkflowViewer extends ButtonTabbedPane implements
		WorkflowStatusListener, ChangeListener {

	// Configuration
	private static final String TOOLTIP_TOOLTIP = "Your current workflow(s)";
	private static final String NEW_TAB_TOOLTIP = "Construct a new workflow";
	private static final String LOAD_TAB_TOOLTIP = "Load a workflow";

	/* The maximum number of characters displayed for a tab name */
	private static final int MAX_TITLE_LENGTH = 15;

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

	/**
	 * Check to see if tab buttons need to be added. The two possible buttons
	 * are a new workflow button, and a load workflow button. Their presence can
	 * be controlled by setting the WorkflowViewerController.SHOW_NEW_TAB and
	 * WorkflowViewerController.SHOW_LOAD_TAB parameters.
	 */
	private void initializeTabButtons() {

		if (WorkflowViewerController.SHOW_NEW_TAB) {
			ButtonTabFlap newWorkflowButton = addButtonTab(
					WorkflowViewerController.NEW_TAB_NAME, new DropTargetAdapter() {
						@Override
						public void drop(DropTargetDropEvent dtde) {
							controller.requestNewWorkflowDragged();
						}
					});
			newWorkflowButton.setToolTipText(NEW_TAB_TOOLTIP);
			newWorkflowButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.requestNewWorkflow();
				}
			});
			newWorkflowButton.setMnemonic(KeyEvent.VK_N);
		}

		if (WorkflowViewerController.SHOW_LOAD_TAB) {
			ButtonTabFlap loadWorkflowButton = addButtonTab(
					WorkflowViewerController.LOAD_TAB_NAME, null);
			loadWorkflowButton.setToolTipText(LOAD_TAB_TOOLTIP);
			loadWorkflowButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.requestLoadWorkflow();
				}
			});
			loadWorkflowButton.setMnemonic(KeyEvent.VK_L);
		}
	}

	/**
	 * Add a new workflow to be displayed by the WorkflowViewer.
	 * 
	 * @param splitPane
	 */
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

		// Setup switching between tabs on component drag over the tab.
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
		// Update tooltips as title and description (on which they are based)
		// change
		topComponent
				.registerComponentDescriptionChangeListener(new Component.ListenerAdaptor() {
					@Override
					public void ComponentDescriptionChanged(Component component) {
						setTooltip(component, inserted_index, tabFlapComponent);
					}
				});

		// Add a "*" to workflows when they become "unsaved".
		topComponent
				.registerSavedStatusChangeListener(new Component.ListenerAdaptor() {
					@Override
					public void savedStatusChanged(Component component) {
						if (component.checkUnsavedChanges()) {// Unsaved
							setTitleAt(
									inserted_index,
									WorkflowViewer.cleanTitle("*"
											+ component.getName()));
						} else {// Saved
							setTitleAt(inserted_index, WorkflowViewer
									.cleanTitle(component.getName()));
						}
					}
				});

		this.setTabComponentAt(inserted_index, tabFlapComponent);
		this.setSelectedIndex(inserted_index);
	}

	/**
	 * Helper method for adding a tooltip to a tab.
	 * 
	 * @param component
	 * @param index
	 * @param tabFlapComponent
	 */
	private void setTooltip(Component component, int index,
			IconizedCloseableTabFlapComponent tabFlapComponent) {
		String tooltip = "<html>"
				+ wrap(component.getName() + "\n" + component.getDescription(),
						40, 4);// Need <html> to allow multiline tooltip
		setToolTipTextAt(index, tooltip);
		tabFlapComponent.setToolTipText(tooltip);
	}

	/**
	 * Word wrap algorithm to insert line returns at word breaks. Used to create
	 * multiline tooltips.
	 * 
	 * Based on http://ramblingsrobert.wordpress.com/2011/04/13/java-word-wrap-
	 * algorithm/
	 */
	private String wrap(String in, int len, int lines) {
		in = in.trim();

		if (in.length() < len) {
			return in;
		} else if (lines <= 1) {
			return in.substring(0, len - 3) + "...";
		}

		if (in.substring(0, len).contains("\n")) {
			return in.substring(0, in.indexOf("\n")).trim() + "<br>"
					+ wrap(in.substring(in.indexOf("\n") + 1), len, lines - 1);
		}

		int place = Math.max(
				Math.max(in.lastIndexOf(" ", len), in.lastIndexOf("\t", len)),
				in.lastIndexOf("-", len));

		return in.substring(0, place).trim() + "<br>"
				+ wrap(in.substring(place), len, lines - 1);
	}

	/**
	 * Process a string to a length suitable for inclusion as a tab title.
	 * 
	 * @param title
	 * @return
	 */
	private static String cleanTitle(String title) {
		title = title.trim();
		if (title.length() < MAX_TITLE_LENGTH) {
			return title;
		} else {
			return title.substring(0, MAX_TITLE_LENGTH - 3) + "...";
		}
	}

	/**
	 * Use this method to set an icon on a tab.
	 */
	@Override
	public void setIconAt(int index, Icon icon) {
		((IconizedCloseableTabFlapComponent) this.getTabComponentAt(index))
				.setStatusIcon(icon, false);
	}

	/**
	 * Use this method to set an icon on a tab that should be cleared when the
	 * tab is visited by the user. If the icon should remain even when visited,
	 * use the alternative setIconAt method.
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

	/**
	 * Listen for changes in the status of workflows being displayed, and change
	 * the icon displayed on their tabs as appropriate.
	 */
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

}