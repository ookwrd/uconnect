package org.u_compare.gui.guiElements;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

/**
 * Extension of CardLayout which allows cards of different sizes.
 * 
 * @author Luke McCrohon
 */
@SuppressWarnings("serial")
public class DynamicCardLayout extends CardLayout {

	private Map<Object, Component> cards = new HashMap<Object, Component>();
	private String current;

	/**
	 * Return preferredLayoutSize based on current card.
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		Component currentComponent = cards.get(current);
		if (currentComponent.isVisible()) {
			return currentComponent.getPreferredSize();
		} else {
			return new Dimension(0, 0);
		}
	}

	/**
	 * Store cards as they are added. Potential to lead to memory leak as
	 * removes are not currently tracked, override removeLayoutComponent if this
	 * is likely to be an issue.
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		cards.put(constraints, comp);
		super.addLayoutComponent(comp, constraints);
	}

	/**
	 * Track the currently displayed card.
	 */
	@Override
	public void show(Container parent, String key) {
		current = key;
		super.show(parent, key);
	}
}
