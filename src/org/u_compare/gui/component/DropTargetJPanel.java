package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.u_compare.gui.DroppableJPanel;
import org.u_compare.gui.control.DropTargetController;
import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;

/**
 * TODO:
 * 
 * @author olaf
 * @author luke
 * @version 2010-11-10
 */
@SuppressWarnings("serial")
public class DropTargetJPanel extends DroppableJPanel {

	public static final int TARGET_BORDER = 10;

	private static boolean iconsLoaded = false;
	private static Icon intermediateDropTargetIcon;
	private static String intermediateDropTargetIconCaption = "XXX"; // TODO:

	private final static String intermediateDropTargetIconPath = "../gfx/intermediate_drop_target_icon.png";

	private JLabel solitaryLabel = new JLabel("Drag and drop a component here.");

	public DropTargetJPanel(DropTargetController controller) {
		super(controller);
		DropTargetJPanel.loadIcons();
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(TARGET_BORDER, TARGET_BORDER));
	}

	public DropTargetJPanel(DropTargetController controller, boolean setSolitary) {
		this(controller);
		if (setSolitary) {
			setSolitaryDropTarget();
		}
	}

	public void highlightMouseDroppable() {
		setBackground(Color.LIGHT_GRAY);
		setOpaque(true);
		this.repaint();

		// also change the cursor
		Cursor cursor = new Cursor(Cursor.MOVE_CURSOR);
		setCursor(cursor);
	}

	public void highlightLocationsDroppable() {
		setBackground(Color.CYAN);
		setOpaque(true);
		this.repaint();

		// also change the cursor
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		setCursor(cursor);
	}

	public void setDragOverHighlightingUndroppable() {
		setBackground(Color.RED);
		setOpaque(true);
		this.repaint();

		// also change the cursor
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cursor);
	}

	public void clearDragOverHighlighting() {
		this.setOpaque(false);
		this.repaint();

		// also change the cursor
		Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
		setCursor(cursor);
	}

	/**
	 * Special display property for drop target intermediate between two
	 * components.
	 */
	public void setIntermediate() {
		this.setPreferredSize(new Dimension(TARGET_BORDER * 3,
				TARGET_BORDER * 3));
		JLabel interImage = new JLabel(intermediateDropTargetIcon);
		// interImage.setSize(intermediateDropTargetIcon.getIconHeight(),
		// intermediateDropTargetIcon.getIconWidth());

		this.add(interImage);
	}

	public void setSolitaryDropTarget() {
		this.removeAll();
		this.add(solitaryLabel);
		this.setPreferredSize(null);
	}

	public void clearSolitaryDropTarget() {
		this.remove(solitaryLabel);
		this.setPreferredSize(new Dimension(TARGET_BORDER, TARGET_BORDER));
	}

	private static synchronized void loadIcons() {
		if (DropTargetJPanel.iconsLoaded == true) {
			return;
		}
		URL image_url;

		image_url = IconizedCloseableTabFlapComponent.class
				.getResource(DropTargetJPanel.intermediateDropTargetIconPath);
		
		assert image_url != null;
		DropTargetJPanel.intermediateDropTargetIcon = new ImageIcon(image_url,
				DropTargetJPanel.intermediateDropTargetIconCaption);

		return;
	}
}