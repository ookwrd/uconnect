package org.u_compare.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Component to be used as a tab flap that can display an icon and
 * is close-able using a button.
 * 
 * @author pontus
 * @author olaf
 * @version 2010-05-28
 */
@SuppressWarnings("serial")
public class IconizedCloseableTabFlapComponent
		extends JPanel implements ActionListener {
	/* The pane that holds this tab */
	private final JTabbedPane parentPane;
	private JLabel statusIconLabel;
	private MouseListener tabListener;
	private JButton close_button;
	
	private static Icon closeTabIcon;
	private static Icon closeTabMouseOverIcon;
	private final static String closeTabIconPath =
		"gfx/close_tab.png";
	private final static String closeTabMouseOverIconPath =
		"gfx/close_tab_mouseover.png";
	private static boolean iconsLoaded = false;
	
	private final static String CLOSE_BUTTON_TOOLTIP =
		"Close this workflow";
	private final static String CLOSE_BUTTON_ICON_CAPTION =
		"Icon indicating closing a tab";
	
	public IconizedCloseableTabFlapComponent(
			final JTabbedPane parentPane, Icon statusIcon) {
		// Disable the spacing usually implied by the FlowLayout
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		this.setOpaque(false);
		
		IconizedCloseableTabFlapComponent.loadIcons();
		assert IconizedCloseableTabFlapComponent.iconsLoaded == true:
			"Failed to lab tab icons";
		
		if(parentPane == null) {
			throw new IllegalArgumentException(
					"Passed null as parent_pane argument");
		}
		else {
			this.parentPane = parentPane;
		}
		
		// First, we add the icon
		this.statusIconLabel = new JLabel(statusIcon);
		this.statusIconLabel.setPreferredSize(new Dimension(14, 14));
		this.statusIconLabel.setBorder(
				BorderFactory.createEmptyBorder(0, 0, 0, 5));
		this.add(this.statusIconLabel);
		
		// Second, we add the title
		JLabel title = new JLabel() {
			public String getText() {
				int i = parentPane.indexOfTabComponent(
						IconizedCloseableTabFlapComponent.this);
                if (i != -1) {
                	return parentPane.getTitleAt(i);
                }
                else {
                	return null;
                }
			}
		};
		title.setBorder(
				BorderFactory.createEmptyBorder(0, 0, 0, 7));
		this.add(title);
		
		// Last of all, we add the close button
		close_button = new JButton(
				IconizedCloseableTabFlapComponent.closeTabIcon);
		// Force it to be a square
		close_button.setPreferredSize(new Dimension(9, 9));
		close_button.setToolTipText(
				IconizedCloseableTabFlapComponent.CLOSE_BUTTON_TOOLTIP);
		// Don't catch the user focus since this would be confusing
		close_button.setFocusable(false);
		
		//let the close button appear only when mouseover on the tab  
		//close_button.setVisible(false); //TODO fix the component resizing
		tabListener = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				//setCloseButtonVisible(false); //TODO fix the component resizing
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				//setCloseButtonVisible(true); //TODO fix the component resizing
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		this.addMouseListener(tabListener);
		
		// Button looks
		close_button.setUI(new BasicButtonUI());
		close_button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		close_button.addActionListener(this);
		
		// Roll-over is Java terminology for mouse over
		close_button.setRolloverEnabled(true);
		close_button.setRolloverIcon(
				IconizedCloseableTabFlapComponent.closeTabMouseOverIcon);
		this.add(close_button);
		
		// Create some distance between the edge and the close button
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 3));
	}
	
	private void setCloseButtonVisible(boolean visible) {
		close_button.setVisible(visible);
	}
	
	private static synchronized void loadIcons() {
		if (IconizedCloseableTabFlapComponent.iconsLoaded == false) {
			URL image_url;
			
			image_url = IconizedCloseableTabFlapComponent.class.getResource(
					IconizedCloseableTabFlapComponent.closeTabIconPath);
			assert image_url != null;
			IconizedCloseableTabFlapComponent.closeTabIcon =
				new ImageIcon(image_url, IconizedCloseableTabFlapComponent
						.CLOSE_BUTTON_ICON_CAPTION);
			
			image_url = IconizedCloseableTabFlapComponent.class.getResource(
					IconizedCloseableTabFlapComponent
							.closeTabMouseOverIconPath);
			assert image_url != null;
			IconizedCloseableTabFlapComponent.closeTabMouseOverIcon =
				new ImageIcon(image_url, IconizedCloseableTabFlapComponent
						.CLOSE_BUTTON_ICON_CAPTION);
			
			IconizedCloseableTabFlapComponent.iconsLoaded = true;
			return;
		}
		else {
			return;
		}
	}

//TODO: Is never activated, investigate as to why.
//	@Override
//	public String getToolTipText() {
//		if(parentPane.indexOfTabComponent(this)
//				== parentPane.getSelectedIndex()) {
//			return "Your current workflow";
//		}
//		else {
//			return "Click to view workflow";
//		}
//	}

	//TODO: Where does this really stem from?
	public void actionPerformed(ActionEvent e) {
		int i = parentPane.indexOfTabComponent(this);
		if (i != -1) {
			parentPane.remove(i);
		}
	}
	
	/**
	 * Set the status icon for this tab, the status icon precedes the tab
	 * name text.
	 * 
	 * @param icon New icon to use as our status icon
	 */
	public void setStatusIcon(Icon icon) {
		this.statusIconLabel.setIcon(icon);
	}
}