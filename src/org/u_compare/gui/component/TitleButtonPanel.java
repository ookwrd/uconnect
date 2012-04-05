package org.u_compare.gui.component;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import static org.u_compare.gui.component.IconFactory.*;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.guiElements.ConfirmationButton;
import org.u_compare.gui.guiElements.HighlightButton;
import org.u_compare.gui.model.AbstractComponent.MinimizedStatusEnum;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.Component.MinimizedStatusChangeListener;

/**
 * View element which includes the minimize, lock and close buttons in a
 * components title bar.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class TitleButtonPanel extends JPanel implements
		MinimizedStatusChangeListener, LockedStatusChangeListener {

	private ComponentPanel view;
	private ComponentController controller;
	private Component component;

	private HighlightButton minButton;
	private HighlightButton lockButton;
	private ConfirmationButton removeButton;

	private static final String LOCK_TOOLTIP = "Lock editing";
	private static final String UNLOCK_TOOLTIP = "Unlock editing";
	private static final String REMOVE_TOOLTIP = "Remove component";
	private static final String EXPAND_TOOLTIP = "Expand component view";
	private static final String MINIMIZE_TOOLTIP = "Minimize component view";

	public TitleButtonPanel(ComponentController controller,
			Component component, ComponentPanel view) {
		super();

		this.view = view;
		this.controller = controller;
		this.component = component;

		// add buttons to expand/collapse/remove the component
		setOpaque(false);
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
		setLayout(buttonLayout);

		loadIcons();

		// set the buttons
		minButton = new HighlightButton(getIcon(MIN_ICON));
		minButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleSize();
			}
		});
		add(minButton);

		if (controller.allowEditing()) {
			lockButton = new HighlightButton(getIcon(UNLOCKED_ICON));
			lockButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					toggleLock();
				}
			});
			add(lockButton);
		}

		if (controller.allowEditing()) {
			removeButton = new ConfirmationButton(new HighlightButton(
					getIcon(CLOSE_ICON)), "Remove?");
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					removeComponent();
				}
			});
			removeButton.setToolTipText(REMOVE_TOOLTIP);
			add(removeButton);
		}

		component.registerLockedStatusChangeListener(this);
		component.registerParentLockedStatusChangeListener(this);
		component.registerMinimizedStatusChangeListener(this);

		setMinimizedStatus();
		setLockedStatus();
	}

	protected void removeComponent() {
		this.controller.removeComponent();
	}

	protected void toggleSize() {
		controller.toggleMinimized();
	}

	protected void toggleLock() {
		controller.toggleLocked();
	}

	protected void setMinimizedStatus() {
		switch (component.getMinimizedStatus()) {
		case MINIMIZED:
			this.minButton.setIcon(getIcon(EXP_ICON));
			this.minButton.setToolTipText(EXPAND_TOOLTIP);
			view.setMinimizeStatus(MinimizedStatusEnum.MINIMIZED);
			break;

		case PARTIAL:
			this.minButton.setIcon(getIcon(MAX_ICON));
			this.minButton.setToolTipText(EXPAND_TOOLTIP);
			view.setMinimizeStatus(MinimizedStatusEnum.PARTIAL);
			break;

		case MAXIMIZED:
			this.minButton.setIcon(getIcon(MIN_ICON));
			this.minButton.setToolTipText(MINIMIZE_TOOLTIP);
			view.setMinimizeStatus(MinimizedStatusEnum.MAXIMIZED);
			break;
		}
	}
	
	protected void setLockedStatus() {
		if(component.getLockedStatus()){
			//Locked
			this.lockButton.setIcon(getIcon(LOCKED_ICON));
			this.lockButton.setToolTipText(UNLOCK_TOOLTIP);
		}else{
			//Unlocked
			this.lockButton.setIcon(getIcon(UNLOCKED_ICON));
			this.lockButton.setToolTipText(LOCK_TOOLTIP);
		}
	}

	@Override
	public void minimizedStatusChanged(Component component) {
		setMinimizedStatus();
	}

	@Override
	public void lockStatusChanged(Component component) {
		if (component.equals(this.component)) {
			setLockedStatus();
		} else {// Need to check it is the parent
			if (component.getLockedStatus()) {
				this.removeButton.setEnabled(false);
				this.removeButton.setToolTipText(null);
			} else {
				this.removeButton.setEnabled(true);
				this.removeButton.setToolTipText(REMOVE_TOOLTIP);
			}
		}
	}
}
