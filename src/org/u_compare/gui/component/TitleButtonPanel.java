package org.u_compare.gui.component;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.guiElements.ConfirmationButton;
import org.u_compare.gui.guiElements.HighlightButton;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Component.LockedStatusChangeListener;
import org.u_compare.gui.model.Component.MinimizedStatusChangeListener;

@SuppressWarnings("serial")
public class TitleButtonPanel extends JPanel implements
		MinimizedStatusChangeListener, LockedStatusChangeListener {

	private JPanel minimizeTarget;
	private ComponentController controller;
	private Component component;

	private HighlightButton minButton;
	private HighlightButton lockButton;
	private ConfirmationButton removeButton;

	public TitleButtonPanel(ComponentController controller, Component component,
			JPanel minimizeTarget) {
		super();

		this.minimizeTarget = minimizeTarget;
		this.controller = controller;
		this.component = component;

		// add buttons to expand/collapse/remove the component
		setOpaque(false);
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
		setLayout(buttonLayout);

		TitleButtonPanel.loadIcons(); // TODO figure out how to load the
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
		
		removeButton = new ConfirmationButton(new HighlightButton(closeIcon), "Remove?");
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
			this.removeButton.setEnabled(false);
		} else {
			this.lockButton.setIcon(unlockedIcon);
			this.removeButton.setEnabled(true);
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
		if (TitleButtonPanel.iconsLoaded == true) {
			return;
		}

		URL image_url;//TODO remove references to ComponentPanel
		image_url = ComponentPanel.class.getResource(TitleButtonPanel.ICON_MIN_PATH);
		assert image_url != null;
		TitleButtonPanel.minIcon = new ImageIcon(image_url, "Minimize");

		image_url = ComponentPanel.class.getResource(TitleButtonPanel.ICON_MAX_PATH);
		assert image_url != null;
		TitleButtonPanel.maxIcon = new ImageIcon(image_url, "Maximize");

		image_url = TitleButtonPanel.class.getResource(TitleButtonPanel.ICON_LOCKED_PATH);
		assert image_url != null;
		TitleButtonPanel.lockedIcon = new ImageIcon(image_url, "Lock");

		image_url = ComponentPanel.class
				.getResource(TitleButtonPanel.ICON_UNLOCKED_PATH);
		assert image_url != null;
		TitleButtonPanel.unlockedIcon = new ImageIcon(image_url, "Unlock");

		image_url = TitleButtonPanel.class.getResource(TitleButtonPanel.ICON_CLOSE_PATH);
		assert image_url != null;
		TitleButtonPanel.closeIcon = new ImageIcon(image_url, "Remove");

		TitleButtonPanel.iconsLoaded = true;
		return;
	}
}
