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

@SuppressWarnings("serial")
public class TitleButtonPanel extends JPanel implements
		MinimizedStatusChangeListener, LockedStatusChangeListener {

	private Minimizable minimizeTarget;
	private ComponentController controller;
	private Component component;

	private HighlightButton minButton;
	private HighlightButton lockButton;
	private ConfirmationButton removeButton;

	public TitleButtonPanel(ComponentController controller, Component component,
			Minimizable minimizeTarget ) {
		super();

		this.minimizeTarget = minimizeTarget;
		this.controller = controller;
		this.component = component;

		// add buttons to expand/collapse/remove the component
		setOpaque(false);
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
		setLayout(buttonLayout);

		IconFactory.loadIcons();

		// set the buttons
		ActionListener removeListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				removeComponent();
			}
		};
		
		ActionListener lockListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleLock();
			}
		};
		
		ActionListener minListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleSize();
			}
		};
		
		minButton = new HighlightButton(getIcon(MIN_ICON));
		minButton.setActionCommand("hide component");
		minButton.addActionListener(minListener);
		add(minButton);		
				
		lockButton = new HighlightButton(component.getLockedStatus() ? getIcon(LOCKED_ICON)
				: getIcon(UNLOCKED_ICON));
		lockButton.setActionCommand("show component");//needed <- olaf why?
		lockButton.addActionListener(lockListener);
		if (controller.allowEditing()) {
			add(lockButton);
		}
		
		removeButton = new ConfirmationButton(new HighlightButton(getIcon(CLOSE_ICON)), "Remove?");
		removeButton.setActionCommand("remove component"); //needed <- olaf why?
		removeButton.addActionListener(removeListener);
		if(controller.allowEditing()){
			add(removeButton);
		}
		
		component.registerLockedStatusChangeListener(this);
		component.registerParentLockedStatusChangeListener(this);
		component.registerMinimizedStatusChangeListener(this);

		setMinimizedStatus();
	}

	protected void removeComponent() {
		this.controller.removeComponent();
	}

	protected void toggleSize() {
		controller.toggleMinimized();
	}

	protected void toggleLock() {
		controller.setLocked(!controller.isLocked());//TODO add toggleLocked to the controller
	}

	protected void setMinimizedStatus() {

		switch (component.getMinimizedStatus()) {
		case MINIMIZED:
			this.minButton.setIcon(getIcon(EXP_ICON));
			minimizeTarget.setMinimizeStatus(MinimizedStatusEnum.MINIMIZED);
			break;
			
		case PARTIAL:
			this.minButton.setIcon(getIcon(MAX_ICON));
			minimizeTarget.setMinimizeStatus(MinimizedStatusEnum.PARTIAL);
			break;

		case MAXIMIZED:
			this.minButton.setIcon(getIcon(MIN_ICON));
			minimizeTarget.setMinimizeStatus(MinimizedStatusEnum.MAXIMIZED);
			break;
		}
	}

	@Override
	public void minimizedStatusChanged(Component component) {

		setMinimizedStatus();

	}

	@Override
	public void lockStatusChanged(Component component) {
		if(component.equals(this.component)){
			if (component.getLockedStatus()) {
				this.lockButton.setIcon(getIcon(LOCKED_ICON));
			} else {
				this.lockButton.setIcon(getIcon(UNLOCKED_ICON));
			}
		}else{//Need to check its the parent
			if (component.getLockedStatus()) {
				this.removeButton.setEnabled(false);
			} else {
				this.removeButton.setEnabled(true);
			}
		}
	}
}
