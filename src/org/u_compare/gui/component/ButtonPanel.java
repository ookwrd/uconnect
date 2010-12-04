package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.MinimizedStatusChangeListener;

@SuppressWarnings("serial")
public class ButtonPanel extends JPanel implements MinimizedStatusChangeListener, 
	LockedStatusChangeListener {

	private JPanel minimizeTarget;
	private ComponentController controller;
	private Component component;
	
	private ActionListener closeListener;
	private ActionListener minListener;
	private ActionListener lockListener;
	
	private JButton closeButton;
	private JButton minButton;
	private JButton lockButton;
	
	private BevelBorder highlighted;
	private Border empty;
	
	public ButtonPanel(ComponentController controller, Component component, JPanel minimizeTarget){
		super();
		
		this.minimizeTarget = minimizeTarget;
		this.controller = controller;
		this.component = component;
		
		// set the buttons
		closeListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				removeComponent();
			}
		};

		minListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				toggleSize();
			}
		};

		lockListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				toggleLock();
			}
		};

		new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				closeButton.setBorderPainted(true);
			}
		};

		new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				closeButton.setBorderPainted(false);
			}
		};
		
		// add buttons to expand/collapse/remove the component
		setOpaque(false);
		FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
		setLayout(buttonLayout);

		ButtonPanel.loadIcons(); // TODO figure out how to load the
										// icons in a better way (xml ?)

		Dimension buttonSize;
		minButton = new JButton(minIcon);
		minButton.setFocusPainted(false); //This may be needed for a mac specific behaviour
		buttonSize = new Dimension(minIcon.getIconWidth()
				- BUTTON_DECREMENT, minIcon.getIconHeight()
				- BUTTON_DECREMENT);
		minButton.setPreferredSize(buttonSize);
		minButton.setActionCommand("hide component");
		minButton.addActionListener(minListener);
		add(minButton);
		
		ImageIcon lockIcon = component.getLockedStatus()?lockedIcon:unlockedIcon;
		lockButton = new JButton(lockIcon);
		lockButton.setFocusPainted(false); //This may be needed for a mac specific behaviour
		buttonSize = new Dimension(unlockedIcon.getIconWidth()
				- BUTTON_DECREMENT, unlockedIcon.getIconHeight()
				- BUTTON_DECREMENT);
		lockButton.setPreferredSize(buttonSize);
		lockButton.setActionCommand("show component");
		lockButton.addActionListener(lockListener);
		if(controller.allowEditing()){
			add(lockButton);
		}
		
		closeButton = new JButton(closeIcon);
		buttonSize = new Dimension(closeIcon.getIconWidth()
				- BUTTON_DECREMENT, closeIcon.getIconHeight()
				- BUTTON_DECREMENT);
		closeButton.setPreferredSize(buttonSize);
		closeButton.setActionCommand("remove component");
		closeButton.addActionListener(closeListener);
		if(controller.allowEditing()){
			add(closeButton);
		}
		
		// set button highlighting
	    highlighted = new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY,
	    		Color.DARK_GRAY);
	    
	    empty = closeButton.getBorder();
	    closeButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				closeButton.setBorder(highlighted);
			}

			public void mouseExited(MouseEvent e) {
				closeButton.setBorder(empty);
			}
		});
	    
	    empty = lockButton.getBorder();
	    lockButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				lockButton.setBorder(highlighted);
			}

			public void mouseExited(MouseEvent e) {
				lockButton.setBorder(empty);
			}
		});
	    
	    empty = minButton.getBorder();
	    minButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				minButton.setBorder(highlighted);
			}

			public void mouseExited(MouseEvent e) {
				minButton.setBorder(empty);
			}
		});
	    
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
		if (this.controller.isLocked()) {
			this.controller.setLocked(false);
		}
		else {
			this.controller.setLocked(true); 
		}
	}
	
	protected void setMinimizedStatus(){
		
		if(component.getMinimizedStatus()){
			this.minButton.setIcon(maxIcon);
			minimizeTarget.setVisible(false);
		}else{
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
		
		if(component.getLockedStatus()){
			this.lockButton.setIcon(lockedIcon);
		}else{
			this.lockButton.setIcon(unlockedIcon);
		}
	}
	
	private final static String ICON_CLOSE_PATH = "../gfx/icon_close1.png";
//	private final static String ICON_CLOSE_PATH_HIGHLIGHT =
//		"../gfx/icon_close1highlight.png";
	private final static String ICON_MAX_PATH = "../gfx/icon_maximize1.png";
	private final static String ICON_MIN_PATH = "../gfx/icon_minimize1.png";
	private final static String ICON_LOCKED_PATH = "../gfx/icon_locked.png";
	private final static String ICON_UNLOCKED_PATH = "../gfx/icon_unlocked.png";
	
	private static final int BUTTON_DECREMENT = 0;
	
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
		image_url = ComponentPanel.class
				.getResource(ButtonPanel.ICON_MIN_PATH);
		assert image_url != null;
		ButtonPanel.minIcon = new ImageIcon(image_url, "Minimize");

		image_url = ComponentPanel.class
				.getResource(ButtonPanel.ICON_MAX_PATH);
		assert image_url != null;
		ButtonPanel.maxIcon = new ImageIcon(image_url, "Maximize");

		image_url = ButtonPanel.class
				.getResource(ButtonPanel.ICON_LOCKED_PATH);
		assert image_url != null;
		ButtonPanel.lockedIcon = new ImageIcon(image_url, "Lock");

		image_url = ComponentPanel.class
				.getResource(ButtonPanel.ICON_UNLOCKED_PATH);
		assert image_url != null;
		ButtonPanel.unlockedIcon = new ImageIcon(image_url, "Unlock");

		image_url = ButtonPanel.class
				.getResource(ButtonPanel.ICON_CLOSE_PATH);
		assert image_url != null;
		ButtonPanel.closeIcon = new ImageIcon(image_url, "Remove");

		ButtonPanel.iconsLoaded = true;
		return;
	}
}
