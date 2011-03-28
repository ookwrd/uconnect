package org.u_compare.gui.component;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.u_compare.gui.component.gui_elements.ConfirmationButton;
import org.u_compare.gui.component.gui_elements.HighlightButton;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.MinimizedStatusChangeListener;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel implements
		MinimizedStatusChangeListener, LockedStatusChangeListener {

	private JPanel minimizeTarget;
	private ComponentController controller;
	private Component component;

	private HighlightButton minButton;
	private HighlightButton lockButton;

	public ButtonPanel(ComponentController controller, Component component,
			JPanel minimizeTarget) {
		super();

		this.minimizeTarget = minimizeTarget;
		this.controller = controller;
		this.component = component;

		// add buttons to expand/collapse/remove the component
		setOpaque(false);
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
		setLayout(buttonLayout);

		ButtonPanel.loadIcons(); // TODO figure out how to load the
									// icons in a better way (xml ?)


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
		
		minButton = new HighlightButton(minIcon);
		minButton.setActionCommand("hide component");
		minButton.addActionListener(minListener);
		add(minButton);		
				
		lockButton = new HighlightButton(component.getLockedStatus() ? lockedIcon
				: unlockedIcon);
		lockButton.setActionCommand("show component");//needed <- olaf why?
		lockButton.addActionListener(lockListener);
		if (controller.allowEditing()) {
			add(lockButton);
		}
		
		ConfirmationButton removeButton = new ConfirmationButton(new HighlightButton(closeIcon), "Remove?");
		removeButton.setActionCommand("remove component"); //needed <- olaf why?
		removeButton.addActionListener(removeListener);
		if(controller.allowEditing()){
			add(removeButton);
		}
		
		component.registerLockedStatusChangeListener(this);
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

		if (component.getMinimizedStatus()) {
			this.minButton.setIcon(maxIcon);
			minimizeTarget.setVisible(false);
		} else {
			this.minButton.setIcon(minIcon);
			minimizeTarget.setVisible(true);
		}
	}

	@Override
	public void minimizedStatusChanged(Component component) {

		setMinimizedStatus();

	}

	@Override
	public void lockStatusChanged(Component component) {

		if (component.getLockedStatus()) {
			this.lockButton.setIcon(lockedIcon);
		} else {
			this.lockButton.setIcon(unlockedIcon);
		}
	}

	private final static String ICON_CLOSE_PATH = "../gfx/icon_close1.png";
	private final static String ICON_MAX_PATH = "../gfx/icon_maximize1.png";
	private final static String ICON_MIN_PATH = "../gfx/icon_minimize1.png";
	private final static String ICON_LOCKED_PATH = "../gfx/icon_locked.png";
	private final static String ICON_UNLOCKED_PATH = "../gfx/icon_unlocked.png";

	private static boolean iconsLoaded = false;
	private static ImageIcon minIcon;
	private static ImageIcon maxIcon;
	private static ImageIcon lockedIcon;
	private static ImageIcon unlockedIcon;
	private static ImageIcon closeIcon;

	protected static synchronized void loadIcons() {
		if (ButtonPanel.iconsLoaded == true) {
			return;
		}

		URL image_url;
		image_url = ComponentPanel.class.getResource(ButtonPanel.ICON_MIN_PATH);
		assert image_url != null;
		ButtonPanel.minIcon = new ImageIcon(image_url, "Minimize");

		image_url = ComponentPanel.class.getResource(ButtonPanel.ICON_MAX_PATH);
		assert image_url != null;
		ButtonPanel.maxIcon = new ImageIcon(image_url, "Maximize");

		image_url = ButtonPanel.class.getResource(ButtonPanel.ICON_LOCKED_PATH);
		assert image_url != null;
		ButtonPanel.lockedIcon = new ImageIcon(image_url, "Lock");

		image_url = ComponentPanel.class
				.getResource(ButtonPanel.ICON_UNLOCKED_PATH);
		assert image_url != null;
		ButtonPanel.unlockedIcon = new ImageIcon(image_url, "Unlock");

		image_url = ButtonPanel.class.getResource(ButtonPanel.ICON_CLOSE_PATH);
		assert image_url != null;
		ButtonPanel.closeIcon = new ImageIcon(image_url, "Remove");

		ButtonPanel.iconsLoaded = true;
		return;
	}
}
