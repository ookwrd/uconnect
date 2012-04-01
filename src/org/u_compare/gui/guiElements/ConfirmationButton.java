package org.u_compare.gui.guiElements;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

		setOpaque(false);
		setContentAreaFilled(false);
		
		setBorder(new EmptyBorder(0,0,0,0));
		
		setLayout(new DynamicCardLayout());
		
		normalPanel = new JPanel();
		normalPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		normalPanel.setBorder(new EmptyBorder(0,0,0,0));
		normalPanel.setOpaque(false);
		
		this.mainButton = mainButton;
		mainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmationButton.this.requestFocusInWindow();//SO focus is passed
				actionRequest();
			}
		});
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
				ConfirmationButton.this.requestFocusInWindow();//SO focus is passed
				confirm(e);
			}
		});
		confirmPanel.add(confirm);
		
		final HighlightButton cancel = new HighlightButton("No");
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ConfirmationButton.this.requestFocusInWindow();//SO focus is passed
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
	
}
