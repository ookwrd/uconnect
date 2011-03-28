package org.u_compare.gui.component;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ConfirmationButton extends JButton {

	private JButton mainButton;
	private JPanel confirmPanel;

	public ConfirmationButton(JButton mainButton, String confirmation){
		
		setOpaque(false);
		setLayout(new FlowLayout(FlowLayout.TRAILING));
		setBorder(new EmptyBorder(0,0,0,0));
		
		this.mainButton = mainButton;
		mainButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actionRequest();
			}
		}
				
		);
		add(mainButton);
		
		confirmPanel = new JPanel();
		confirmPanel.setOpaque(false);
		confirmPanel.setVisible(false);
		confirmPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		confirmPanel.add(new JLabel(confirmation));

		final HighlightButton confirm = new HighlightButton("Yes");
		confirm.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				confirm(e);
			}
		});
		confirmPanel.add(confirm);
		
		final HighlightButton cancel = new HighlightButton("No");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cancel();
			}
		});
		confirmPanel.add(cancel);
		
		add(confirmPanel);
		
		//If mouse leaves the area, take that as a cancel event.
		MouseListener listener = new MouseAdapter(){
			public void mouseExited(MouseEvent e) {
				cancel();
			}
			public void mouseEntered(MouseEvent e){
				actionRequest();
			}
		};
		
		confirmPanel.addMouseListener(listener);
		cancel.addMouseListener(listener);
		confirm.addMouseListener(listener);
		
		
	}

	private void actionRequest(){
		
		mainButton.setVisible(false);
		confirmPanel.setVisible(true);
		
	}
	
	private void confirm(ActionEvent e){
		
		fireActionPerformed(e);
		
	}
	
	private void cancel(){
		
		mainButton.setVisible(true);
		confirmPanel.setVisible(false);
	
	}

	/*private final static String ICON_CLOSE_PATH = "../gfx/icon_close1.png";
	
	private static boolean iconsLoaded = false;
	
	private static ImageIcon closeIcon;

	
	protected static synchronized void loadIcons() {
		if (ConfirmButton.iconsLoaded == true) {
			return;
		}
		
		URL image_url;
		
		image_url = ButtonPanel.class.getResource(ICON_CLOSE_PATH);
		assert image_url != null;
		closeIcon = new ImageIcon(image_url, "Remove");

		iconsLoaded = true;
		return;

	}*/
	
}
