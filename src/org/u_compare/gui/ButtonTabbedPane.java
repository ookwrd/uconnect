package org.u_compare.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class ButtonTabbedPane extends JTabbedPane {

	private final ArrayList<ButtonTabFlap> buttons = new ArrayList<ButtonTabFlap>();
	
	private JPanel panelZero = new JPanel(){
		{
			add(new JLabel("There are no Tabbed panes to display."));
			setName("      ");
		}
	};
	
	public ButtonTabFlap addButtonTab(String title, DropTargetListener dropListener){
		
		ButtonTabFlap tabFlap = new ButtonTabFlap(title, dropListener);
		
		//Create a component that whenever it is displayed switches the tab to a previous one.
		addTab(title, new JLabel(){
			public void paint(Graphics g){//Makes the tab undisplayable, on attempts to draw it opens the last non-button
				switchToLastTab();
			}
		});
		
		setTabComponentAt(getTabCount() - 1, tabFlap);
		
		buttons.add(tabFlap);
		
		return tabFlap;
			
	}
	
	@Override
	public Component add(Component comp, int i){
		
		if(zeroInUse()){
			remove(panelZero);
			Component compent = super.add(comp, i);
			return compent;
		}else{
			return super.add(comp, i);
		}
	}
	
	/**
	 * Can't override getTabCount as it is used internally in JTabbedPane
	 * 
	 * @return
	 */
	public int numberOfNonButtonTabs(){
		return super.getTabCount() - (buttons==null?0:buttons.size()) - (zeroInUse()?1:0);//Somehow this seems to be called at construction time prior to initialization of buttons.
	}
	
	public boolean zeroInUse(){
		for(Component comp :getComponents()){
			if(comp.equals(panelZero)){
				return true;
			}
		}
		return false;
	}
	
	public int numberOfButtonTabs(){
		return buttons==null?0:buttons.size();
	}
	
	/**
	 * Switch to the last non-button tab.
	 */
	public void switchToLastTab(){
		if(numberOfNonButtonTabs()>0){
			setSelectedIndex(numberOfNonButtonTabs()-1);
		}else{
			add(panelZero, 0);
			setSelectedIndex(0);
		}
	}
	
	public void setEmptyTab(JPanel panelZero){
		this.panelZero = panelZero;
		//TODO check if the old zero tab is in use and replace it
	}
}
