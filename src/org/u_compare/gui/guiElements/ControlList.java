package org.u_compare.gui.guiElements;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Wrapper for a JList that adds "Add" and "Delete" buttons when the list is selected.
 * 
 * @author Luke McCrohon
 *
 */
@SuppressWarnings("serial")
public class ControlList extends JPanel {

	private static final String EMPTY_LIST_MESSAGE = "(Empty. Click to edit)";
	private static final String DELETE_MSG = "Delete";
	private static final String ADD_MSG = "Add";
	
	private JPanel buttons;
	private HighlightButton deleteButton;
	private HighlightButton addButton;
	private JList list;
	private DefaultListModel listModel;
	
	/**
	 * Create a new ControlList with the specified background color.
	 * 
	 * @param background
	 */
	public ControlList(Color background){
		this(background, true);
	}
	
	/**
	 * Create a new ControlList with the specified background color and item centering. 
	 * 
	 * @param background Background color
	 * @param centering Should items be centered
	 */
	public ControlList(Color background, boolean centering){
		super();
		listModel = new DefaultListModel();
		list = new AutoscrollList(listModel);
		
		if(centering){
			((JLabel)(list.getCellRenderer())).setHorizontalAlignment(JLabel.CENTER);
		}
		
		setLayout(new BoxLayout(this,
				BoxLayout.Y_AXIS));
		
		list.setBackground(background);

		list.setFixedCellWidth(140);//TODO Would be nice to make this expand and contract as needed.
		
		buttons = new JPanel();
		buttons.setOpaque(false);
		buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		deleteButton = new HighlightButton(DELETE_MSG);
		addButton = new HighlightButton(ADD_MSG);
		
		FocusListener listFocusListener = new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e){
				toEditMode();
			}
			@Override
			public void focusLost(FocusEvent e) {
				Object source = e.getOppositeComponent();
				if(source==null
						|| source.equals(this) 
						|| source.equals(addButton) 
						|| source.equals(deleteButton)){
					return;
				}
				toFixedMode();
			}
		};
		list.addFocusListener(listFocusListener);
		deleteButton.addFocusListener(listFocusListener);
		addButton.addFocusListener(listFocusListener);
		
		ActionListener reactive = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.requestFocusInWindow(); //To reopen the button panel.
			}
		};
		addButton.addActionListener(reactive);
		//Makes reselection possible in a single click.
		deleteButton.addActionListener(reactive);
		
		buttons.add(deleteButton);
		buttons.add(addButton);
		buttons.setVisible(false);
		
		add(list);
		add(buttons);
		
		rebuildListContents(new ArrayList<String>());
	}
	
	/**
	 * Redirects to the wrapped JList's getSelectedValues().
	 * 
	 * @return
	 */
	public Object[] getSelectedValues(){
		return list.getSelectedValues();
	}
	
	/**
	 * Add a listener to be called when the "add" button is clicked.
	 * 
	 * @param al
	 */
	public void registerAddActionListener(ActionListener al){
		addButton.addActionListener(al);
	}
	
	/**
	 * Add a listener to be called when the "delete" button is clicked.
	 * 
	 * @param al
	 */
	public void registerDeleteActionListener(ActionListener al){
		deleteButton.addActionListener(al);
	}
	
	/**
	 * Rebuilds the wrapped JList with the specified set of values
	 * 
	 * @param values
	 */
	public void rebuildListContents(ArrayList<String> values){
		assert(list!=null);
		listModel.clear();	
		
		deleteButton.setEnabled(true);
		
		for(Object str : values){
			listModel.addElement(str);
		}

		if(listModel.isEmpty()){
			listModel.addElement(EMPTY_LIST_MESSAGE);
			deleteButton.setEnabled(false);
		}
	}
	
	/**
	 * Redirects to the wrapped JLists setBorder(..) method.
	 */
	@Override
	public void setBorder(Border bord){
		if(list != null){
			list.setBorder(bord);
		}
	} 
	
	/**
	 * Set a border around the entire component.
	 * 
	 * @param bord
	 */
	public void setExternalBorder(Border bord){
		setBorder(bord);
	}
	
	@Override
	public void setEnabled(boolean enabled){
		list.setEnabled(enabled);
	}
	
	/**
	 * Change to non-editable mode.
	 */
	protected void toFixedMode(){
		list.clearSelection();
		buttons.setVisible(false);
	}
	
	/**
	 * Change to editable mode.
	 */
	protected void toEditMode(){
		buttons.setVisible(true);
	}
}
