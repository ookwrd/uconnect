package org.u_compare.gui.guiElements;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Wrapper for a JList that adds "Add" and "Delete" buttons when the list is
 * selected.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class ControlList extends JPanel {
	
	private static final String EMPTY_LIST_MESSAGE = "(Empty. Click to edit)";
	private static final String DELETE_MSG = "Delete";
	private static final String ADD_MSG = "Add";
	private static final String UP_MSG = "Up";
	private static final String DOWN_MSG = "Down";

	private JPanel buttons;
	private HighlightButton deleteButton;
	private HighlightButton addButton;
	private HighlightButton upButton;
	private HighlightButton downButton;
	private JList list;
	private DefaultListModel listModel;
	private boolean allowReordering;
	
	private Map<Object, String> tooltips;

	/**
	 * Create a new ControlList with the specified background color.
	 * 
	 * @param background
	 */
	public ControlList(Color background) {
		this(background, true, false);
	}

	/**
	 * Create a new ControlList with the specified background color and item
	 * centering.
	 * 
	 * @param background
	 *            Background color
	 * @param centering
	 *            Should items be centered
	 */
	public ControlList(Color background, boolean centering, boolean allowReordering) {
		super();
		this.allowReordering = allowReordering;
		
		listModel = new DefaultListModel();
		list = new AutoscrollList(listModel){
			@Override
			public String getToolTipText(MouseEvent evt){
				return lookupToolTipText(evt);
			}
		};

		if (centering) {
			((JLabel) (list.getCellRenderer()))
					.setHorizontalAlignment(JLabel.CENTER);
		}

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		list.setBackground(background);

		list.setFixedCellWidth(140);// TODO Would be nice to make this expand
									// and contract as needed.

		buttons = new JPanel();
		buttons.setOpaque(false);
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		FocusListener listFocusListener = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				toEditMode();
			}

			@Override
			public void focusLost(FocusEvent e) {
				Object source = e.getOppositeComponent();
				if (source == null || source.equals(this)
						|| source.equals(addButton)
						|| source.equals(deleteButton)
						|| source.equals(upButton)
						|| source.equals(downButton)) {
					return;
				}
				toFixedMode();
			}
		};
		list.addFocusListener(listFocusListener);
		
		ActionListener reactive = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.requestFocusInWindow(); // To reopen the button panel.
			}
		};
		
		deleteButton = new HighlightButton(DELETE_MSG);
		addButton = new HighlightButton(ADD_MSG);

		deleteButton.addFocusListener(listFocusListener);
		addButton.addFocusListener(listFocusListener);
		
		// Makes reselection possible in a single click.
		deleteButton.addActionListener(reactive);
		addButton.addActionListener(reactive);
		
		upButton = new HighlightButton(UP_MSG);
		downButton = new HighlightButton(DOWN_MSG);
		
		if(allowReordering){
			upButton.addFocusListener(listFocusListener);
			downButton.addFocusListener(listFocusListener);

			upButton.addActionListener(reactive);
			downButton.addActionListener(reactive);
		}
		
		buttons.add(deleteButton);
		buttons.add(addButton);
		
		if(allowReordering){
			buttons.add(upButton);
			buttons.add(downButton);
		}
		
		buttons.setVisible(false);

		add(list);
		add(buttons);

		rebuildListContents(new ArrayList<Object>());
	}

	/**
	 * Redirects to the wrapped JList's getSelectedValues().
	 * 
	 * @return
	 */
	public Object[] getSelectedValues() {
		return list.getSelectedValues();
	}

	/**
	 * Add a listener to be called when the "add" button is clicked.
	 * 
	 * @param al
	 */
	public void registerAddActionListener(ActionListener al) {
		addButton.addActionListener(al);
	}

	/**
	 * Add a listener to be called when the "delete" button is clicked.
	 * 
	 * @param al
	 */
	public void registerDeleteActionListener(ActionListener al) {
		deleteButton.addActionListener(al);
	}
	
	/**
	 * Add a listener to be called when the "up" button is clicked.
	 * 
	 * @param al
	 */
	public void registerUpActionListener(ActionListener al){
		assert(upButton != null);
		upButton.addActionListener(al);
	}
	
	/**
	 * Add a listener to be called when the "down" button is clicked.
	 * 
	 * @param al
	 */
	public void registerDownActionListener(ActionListener al){
		assert(downButton != null);
		downButton.addActionListener(al);
	}

	/**
	 * Rebuilds the wrapped JList with the specified set of values
	 * 
	 * @param values
	 */
	public void rebuildListContents(@SuppressWarnings("rawtypes") ArrayList values) {
		assert (list != null);
		listModel.clear();

		deleteButton.setEnabled(true);
		if(allowReordering){
			upButton.setEnabled(true);
			downButton.setEnabled(true);
		}
		
		for (Object str : values) {
			listModel.addElement(str);
		}

		if (listModel.isEmpty()) {
			listModel.addElement(EMPTY_LIST_MESSAGE);
			deleteButton.setEnabled(false);
		}
		
		if (allowReordering && listModel.size() < 2){
			upButton.setEnabled(false);
			downButton.setEnabled(false);
		}
	}

	/**
	 * Redirects to the wrapped JLists setBorder(..) method.
	 */
	@Override
	public void setBorder(Border bord) {
		if (list != null) {
			list.setBorder(bord);
		}
	}

	/**
	 * Set a border around the entire component.
	 * 
	 * @param bord
	 */
	public void setExternalBorder(Border bord) {
		setBorder(bord);
	}

	@Override
	public void setEnabled(boolean enabled) {
		list.setEnabled(enabled);
	}

	/**
	 * Change to non-editable mode.
	 */
	protected void toFixedMode() {
		list.clearSelection();
		buttons.setVisible(false);
	}

	/**
	 * Change to editable mode.
	 */
	protected void toEditMode() {
		buttons.setVisible(true);
	}
	
	/**
	 * Register a map of object-string associations to specifying tooltips for items in the lists underlying list model.
	 */
	public void registerTooltips(Map<Object, String> tooltips){
		this.tooltips = tooltips;
	}
	
	private String lookupToolTipText(MouseEvent evt) {
		
		if(tooltips != null){//Tooltips have been registered
	        // Get item index
	        int index = list.locationToIndex(evt.getPoint());
	        // Get item
	        Object item = listModel.getElementAt(index);
	        // Lookup the tool tip text
	        return tooltips.get(item);
		}else{
			return null;
		}
    }
}
