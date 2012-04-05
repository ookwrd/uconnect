package org.u_compare.gui.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.dnd.DragSource;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.u_compare.gui.control.DragAndDropController;
import org.u_compare.gui.control.DropTargetController;
import org.u_compare.gui.guiElements.IconizedCloseableTabFlapComponent;

/**
 * View component depicting the space on which dragged components can be dropped. 
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

	private final static String intermediateDropTargetIconPath = "../gfx/intermediate_drop_target_icon.png";
	
	private final JLabel solitaryLabel = new JLabel("(Drag and drop a component here)");
	{
		solitaryLabel.setPreferredSize(new Dimension(solitaryLabel.getPreferredSize().width,120));
	}
	
	public SubComponentDropTarget(DropTargetController controller) {
		super();
		SubComponentDropTarget.loadIcons();
		
		this.setOpaque(false);
		this.setPreferredSize(new Dimension(TARGET_BORDER, TARGET_BORDER));
		
		DragAndDropController.registerDropTarget(this,controller);
	}

	public SubComponentDropTarget(DropTargetController controller, boolean setSolitary) {
		this(controller);
		if (setSolitary) {
			setSolitaryDropTarget();
		}
		
		DragAndDropController.registerDropTarget(this,controller);
	}

	public void highlightMouseDroppable() {
		setBackground(Color.LIGHT_GRAY);
		setOpaque(true);
		this.repaint();
		setCursor(DragSource.DefaultMoveDrop);
	}

	public void setDragOverHighlightingUndroppable() {
		setBackground(Color.RED);
		setOpaque(true);
		this.repaint();
		setCursor(DragSource.DefaultMoveNoDrop);
	}

	public void clearDragOverHighlighting() {
		this.setOpaque(false);
		this.repaint();
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	/**
	 * Set special display property for drop target intermediate between two
	 * components.
	 */
	public void setIntermediate() {
		this.setPreferredSize(new Dimension(TARGET_BORDER * 3,
				TARGET_BORDER * 3));
		JLabel interImage = new JLabel(intermediateDropTargetIcon);

		this.add(interImage);
	}

	/**
	 * Set special display property for drop target that is isolated (i.e. the initial drop target).
	 */
	public void setSolitaryDropTarget() {
		this.removeAll();
		this.add(solitaryLabel);
		this.setPreferredSize(null);
	}

	public void clearSolitaryDropTarget() {
		this.removeAll();
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
		SubComponentDropTarget.intermediateDropTargetIcon = new ImageIcon(image_url);

		iconsLoaded = true;
		return;
	}
}
