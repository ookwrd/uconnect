package org.u_compare.gui.component.gui_elements;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.CompoundBorder;

/**
 * An extension of the Standard JButton Class that allows mouseover highlighting.
 * 
 * @author Luke Mccrohon
 *
 */
@SuppressWarnings("serial")
public class HighlightButton extends JButton {

	private Border highlighted;
	private Border unhighlighted;
	
	private boolean highlightingEnabled = true;
	
	public HighlightButton(Icon icon){
		super(icon);
		configure();
	}
	
	public HighlightButton(String string){
		super(string);
		configure();
	}
	
	public HighlightButton(String string, Icon icon){
		super(string, icon);
		configure();
	}
	
	public void enableHighlighting(){
		enableHighlighting(true);
	}
	
	public void disableHighlighting(){
		enableHighlighting(false);
	}
	
	public void enableHighlighting(boolean enabled){
		highlightingEnabled = enabled;
	}
	
	@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		enableHighlighting(enabled);
	}
	
	private void configure(){

		setOpaque(false);
		setFocusPainted(false);// This may be needed for a mac specific behaviour
		
		//Add mouse over highlighting
		highlighted = new BevelBorder(BevelBorder.LOWERED, Color.LIGHT_GRAY,
				Color.DARK_GRAY);
		unhighlighted = new CompoundBorder(new LineBorder(Color.LIGHT_GRAY,1), new EmptyBorder(1,1,1,1));
		
		setBorder(unhighlighted);
		
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if(highlightingEnabled){
					setBorder(highlighted);
				}
			}

			public void mouseExited(MouseEvent e) {
				setBorder(unhighlighted);
			}
		});
		
	}
}
