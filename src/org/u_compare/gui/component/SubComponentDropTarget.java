package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.control.DropTargetController;
import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;
import org.u_compare.gui.guiElements.DragAndDrop;

/**
 * TODO:
 * 
 * @author olaf
 * @author luke
 * @version 2010-11-10
 */
@SuppressWarnings("serial")
public class SubComponentDropTarget extends JPanel {

	public static final int TARGET_BORDER = 10;

	private static boolean iconsLoaded = false;
	private static Icon intermediateDropTargetIcon;
	private static String intermediateDropTargetIconCaption = "XXX"; // TODO:

	private final static String intermediateDropTargetIconPath = "../gfx/intermediate_drop_target_icon.png";
	private final static String droppableCursorImagePath = //System.getProperty("user.dir")+"/src/org/u_compare/gui"+
		"../gfx/droppable.gif";
	
	private JLabel solitaryLabel = new JLabel("(Drag and drop a component here)");

	private static Toolkit toolkit;
	private static Image image;
	private static Point hotSpot;
	private static Cursor droppableCursor;

	public SubComponentDropTarget(DropTargetController controller) {
		super();
		SubComponentDropTarget.loadIcons();
		
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(TARGET_BORDER, TARGET_BORDER));
		
		DragAndDrop.registerDropTarget(this,controller);
	}

	public SubComponentDropTarget(DropTargetController controller, boolean setSolitary) {
		this(controller);
		if (setSolitary) {
			setSolitaryDropTarget();
		}
		
		DragAndDrop.registerDropTarget(this,controller);
	}

	public void highlightMouseDroppable() {
		setBackground(Color.LIGHT_GRAY);
		setOpaque(true);
		this.repaint();

		// also change the cursor
		//Cursor cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
		setCursor(droppableCursor);
		System.out.println("Cursor is droppableCursor");
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
		if (SubComponentDropTarget.iconsLoaded == true) {
			return;
		}
		URL image_url;

		image_url = IconizedCloseableTabFlapComponent.class
				.getResource(SubComponentDropTarget.intermediateDropTargetIconPath);
		
		assert image_url != null;
		SubComponentDropTarget.intermediateDropTargetIcon = new ImageIcon(image_url,
				SubComponentDropTarget.intermediateDropTargetIconCaption);

		//mouse cursor
		image_url = IconizedCloseableTabFlapComponent.class
		.getResource(SubComponentDropTarget.droppableCursorImagePath);
		
		toolkit = Toolkit.getDefaultToolkit();    
		ImageIcon imageIcon = new ImageIcon("droppable.gif");//TODO check path ??
		image = imageIcon.getImage();//toolkit.getImage("droppableCursorImagePath");
		hotSpot = new Point(0,0);
		droppableCursor = toolkit.createCustomCursor(image, hotSpot, "droppable");
		System.out.println("Cursor created from "+droppableCursorImagePath);
		
		//assert image_url != null;
		//SubComponentDropTarget.droppableCursorImage = new ImageIcon(image_url,
		//		SubComponentDropTarget.droppableCursorImageCaption);
		
		iconsLoaded = true;
		return;
	}
}