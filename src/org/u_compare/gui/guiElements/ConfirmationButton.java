package org.u_compare.gui.guiElements;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


@SuppressWarnings("serial")
public class ConfirmationButton extends JButton {

	private static final String NORMAL = "Normal";
	private static final String CONFIRM = "Confirm";
	
	private JPanel normalPanel;
	private JButton mainButton;
	private JPanel confirmPanel;

	public ConfirmationButton(JButton mainButton, String confirmation){
		super();
		
		// TODO change the layout to cardlayout, creating the cards as on
		// http://download.oracle.com/javase/tutorial/uiswing/layout/card.html
		
		setOpaque(false);
		
		setBorder(new EmptyBorder(0,0,0,0));
		
		setLayout(new CardLayout(){//TODO extract this to a new layout,cardlayout that dynamically changes size
			@Override
			public Dimension preferredLayoutSize(Container parent){
				return cards.get(current).getPreferredSize();
			}
			
			private Map<Object, Component> cards = new HashMap<Object, Component>();
			private String current;
			
			@Override
			public void addLayoutComponent(Component comp, Object constraints) {
				cards.put(constraints, comp);
				super.addLayoutComponent(comp, constraints);
			}
			
			@Override
			public void show(Container parent, String key){
				current = key;
				super.show(parent, key);
			}
		});
		
		normalPanel = new JPanel();
		normalPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		normalPanel.setBorder(new EmptyBorder(0,0,0,0));
		normalPanel.setOpaque(false);
		
		this.mainButton = mainButton;
		mainButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				actionRequest();
			}
		}
				
		);
		normalPanel.add(mainButton);
		
		add(normalPanel, NORMAL);
		
		confirmPanel = new JPanel();
		confirmPanel.setOpaque(false);
		confirmPanel.setVisible(false);
		confirmPanel.setLayout(new FlowLayout(FlowLayout.TRAILING,5,0));
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
		
		add(confirmPanel, CONFIRM);
		
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
		
		CardLayout cl = (CardLayout)(getLayout());
	    cl.show(this, NORMAL);
	}

	private void actionRequest(){
		CardLayout cl = (CardLayout)(getLayout());
	    cl.show(this, CONFIRM);
		
	}
	
	private void confirm(ActionEvent e){
		fireActionPerformed(e);
	}
	
	private void cancel(){
		CardLayout cl = (CardLayout)(getLayout());
	    cl.show(this, NORMAL);
	}

	@Override
	public void setEnabled(boolean enabled){
		
		if(!enabled){
			cancel();
		}
		
		mainButton.setEnabled(enabled);
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
