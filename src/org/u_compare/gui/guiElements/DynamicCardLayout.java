package org.u_compare.gui.guiElements;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DynamicCardLayout extends CardLayout {

	private Map<Object, Component> cards = new HashMap<Object, Component>();
	private String current;
	
	@Override
	public Dimension preferredLayoutSize(Container parent){
		Component currentComponent = cards.get(current);
		if(currentComponent.isVisible()){
			return currentComponent.getPreferredSize();
		}else{
			return new Dimension(0,0);
		}
	}
	
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
}
