package org.u_compare.gui.guiElements;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Component to be used as a tab flap that can display an icon as a status
 * indicator and is close-able using a button.
 * 
 * //TODO extract close button tab to its own class.
 * 
 * @author pontus
 * @author olaf
 * @author lukemccrohon
 * @version 2010-05-28
 */
@SuppressWarnings("serial")
public class IconizedCloseableTabFlapComponent extends JPanel {

	private static final Icon EMPTY_ICON = new Icon() {// An empty place holder icon.
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

	/* The pane that holds this tab */
	private final JTabbedPane parentPane;
	private JLabel statusIconLabel;
	private JButton close_button;

	private static Icon closeTabIcon;
	private static Icon closeTabMouseOverIcon;
	private final static String closeTabIconPath = "../gfx/close_icon.png";
	private final static String closeTabMouseOverIconPath = "../gfx/close_icon_mouseover.png";
	private static boolean iconsLoaded = false;

	private final static String CLOSE_BUTTON_TOOLTIP = "Close this workflow";
	private final static String CLOSE_BUTTON_ICON_CAPTION = "Icon indicating closing a tab";

	private boolean iconIsNotification = false;

	public IconizedCloseableTabFlapComponent(final JTabbedPane parentPane,
			Icon statusIcon, DropTargetListener dropListener,
			boolean showCloseIcon) {
		this(parentPane, statusIcon, showCloseIcon);

		new DropTarget(this, dropListener);
	}

	public IconizedCloseableTabFlapComponent(final JTabbedPane parentPane,
			Icon statusIcon, boolean showCloseIcon) {

		// Disable the spacing usually implied by the FlowLayout
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));

		this.setOpaque(false);

		IconizedCloseableTabFlapComponent.loadIcons();
		assert IconizedCloseableTabFlapComponent.iconsLoaded == true : "Failed to load tab icons";

		if (parentPane == null) {
			throw new IllegalArgumentException(
					"Passed null as parent_pane argument");
		} else {
			this.parentPane = parentPane;
		}

		// First, we add the icon
		this.statusIconLabel = new JLabel(statusIcon);
		this.statusIconLabel.setSize(new Dimension(16, 16));
		this.statusIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0,
				3));
		this.add(this.statusIconLabel);

		// Second, we add the title
		JLabel title = new JLabel() {
			public String getText() {
				int i = parentPane
						.indexOfTabComponent(IconizedCloseableTabFlapComponent.this);
				if (i != -1) {
					return parentPane.getTitleAt(i);
				} else {
					return null;
				}
			}
		};
		title.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
		this.add(title);

		if (showCloseIcon) {
			// Last of all, we add the close button
			close_button = new JButton(
					IconizedCloseableTabFlapComponent.closeTabIcon);
			// Force it to be a square
			close_button.setPreferredSize(new Dimension(9, 9));
			close_button
					.setToolTipText(IconizedCloseableTabFlapComponent.CLOSE_BUTTON_TOOLTIP);
			// Don't catch the user focus since this would be confusing
			close_button.setFocusable(false);

			// Button looks
			close_button.setUI(new BasicButtonUI());
			close_button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			close_button.setOpaque(false);
			close_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int i = parentPane
							.indexOfTabComponent(IconizedCloseableTabFlapComponent.this);
					if (i != -1) {
						parentPane.remove(i);
					}
				}
			});

			// Roll-over is Java terminology for mouse over
			close_button.setRolloverEnabled(true);
			close_button
					.setRolloverIcon(IconizedCloseableTabFlapComponent.closeTabMouseOverIcon);
			this.add(close_button);

			close_button.setIcon(null);
			;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					setCloseButtonVisible(false);
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					setCloseButtonVisible(true);
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					// Fixes issue as described here:
					// http://stackoverflow.com/questions/4348293/tab-component-consuming-mouse-so-tabs-wont-change
					parentPane
							.setSelectedIndex(parentPane
									.indexOfTabComponent(IconizedCloseableTabFlapComponent.this));
				}
			});

			setCloseButtonVisible(true);
			parentPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					setCloseButtonVisible(false);// false unless its selected.
				}
			});
		}

		// Create some distance between the edge and the close button
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 0));
	}

	/**
	 * Adds mouse listener to all subcomponents.
	 */
	@Override
	public void addMouseListener(MouseListener listener) {
		super.addMouseListener(listener);
		close_button.addMouseListener(listener);
		statusIconLabel.addMouseListener(listener);
	}

	private void setCloseButtonVisible(boolean visible) {
		if (visible
				|| parentPane
						.indexOfTabComponent(IconizedCloseableTabFlapComponent.this) == parentPane
						.getSelectedIndex()) {
			close_button
					.setIcon(IconizedCloseableTabFlapComponent.closeTabIcon);
		} else {
			close_button.setIcon(null);
		}
	}

	/**
	 * Set the status icon for this tab, the status icon precedes the tab name
	 * text.
	 * 
	 * @param icon
	 *            New icon to use as our status icon
	 */
	public void setStatusIcon(Icon icon, boolean iconIsNotification) {
		this.statusIconLabel.setIcon(icon);
		this.iconIsNotification = iconIsNotification;
	}

	/**
	 * Is this an icon that should be cleared when the tab is selected?
	 * 
	 * @return
	 */
	public boolean iconIsNotification() {
		return iconIsNotification;
	}

	/**
	 * If the icon is a notification icon, clear it.
	 */
	public void clearNotification() {
		if (iconIsNotification) {
			clearIcon();
		}
	}

	/**
	 * Clear the current icon.
	 */
	public void clearIcon() {
		setStatusIcon(EMPTY_ICON, false);
	}

	/**
	 * Load the image resources needed by this component. Not moved to
	 * IconFactory so that the component can be more easily reused.
	 */
	private static synchronized void loadIcons() {
		if (IconizedCloseableTabFlapComponent.iconsLoaded == true) {
			return;
		}
		
		URL image_url;

		image_url = IconizedCloseableTabFlapComponent.class
				.getResource(IconizedCloseableTabFlapComponent.closeTabIconPath);
		assert image_url != null;
		IconizedCloseableTabFlapComponent.closeTabIcon = new ImageIcon(
				image_url,
				IconizedCloseableTabFlapComponent.CLOSE_BUTTON_ICON_CAPTION);

		image_url = IconizedCloseableTabFlapComponent.class
				.getResource(IconizedCloseableTabFlapComponent.closeTabMouseOverIconPath);
		assert image_url != null;
		IconizedCloseableTabFlapComponent.closeTabMouseOverIcon = new ImageIcon(
				image_url,
				IconizedCloseableTabFlapComponent.CLOSE_BUTTON_ICON_CAPTION);

		IconizedCloseableTabFlapComponent.iconsLoaded = true;
	}
}