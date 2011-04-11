package org.u_compare.gui;

import java.awt.Graphics;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ButtonTabbedPane extends JTabbedPane {

	private final ArrayList<ButtonTabFlap> buttons = new ArrayList<ButtonTabFlap>();
	
	public ButtonTabFlap addButtonTab(String title, DropTargetListener dropListener){
		
		ButtonTabFlap tabFlap = new ButtonTabFlap(title, dropListener);
		
		//Create a component that whenever it is displayed switches the tab to a previous one.
		addTab(title, new JLabel(){
			public void paint(Graphics g){//Makes the tab undisplayable, on attempts to draw it opens the last non-button
				swtichToLastTab();
			}
		});
		
		setTabComponentAt(getTabCount() - 1, tabFlap);
		
		buttons.add(tabFlap);
		
		return tabFlap;
			
	}
	
	/**
	 * Can't override getTabCount as it is used internally in JTabbedPane
	 * 
	 * @return
	 */
	public int numberOfNonButtonTabs(){
		return super.getTabCount() - (buttons==null?0:buttons.size());//Somehow this seems to be called at construction time prior to initialization of buttons.
	}
	
	public int numberOfButtonTabs(){
		return buttons==null?0:buttons.size();
	}
	
	/**
	 * Switch to the last non-button tab.
	 */
	public void swtichToLastTab(){
		System.out.println(getTabCount() + "   " + buttons.size());
		setSelectedIndex(numberOfNonButtonTabs()-1);
	}
	
}
