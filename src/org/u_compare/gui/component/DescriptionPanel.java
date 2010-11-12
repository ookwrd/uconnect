package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.DescriptionChangeListener;

@SuppressWarnings("serial")
public class DescriptionPanel extends JPanel implements DescriptionChangeListener{

	private static final int DESCRIPTION_PANEL_PADDING = 5;
	
	public final Color defaultColor = getBackground();
	
	private final ComponentController controller;
	private final Component component;

	private ActionListener descriptionListener;
	private FocusListener descriptionFocusListener;
	
	private JTextArea description;
	private JTextArea editableDescription;
	private String descriptionText;
	
	public DescriptionPanel(ComponentController controller, Component component){
		super();
	
		this.controller = controller;
		this.component = component;
		
		descriptionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDescription(editableDescription.getText());
				editableDescription.setVisible(false);
				description.setVisible(true);
			}
		};
		
		descriptionFocusListener = new FocusListener() {
			
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				setDescription(editableDescription.getText());
				editableDescription.setVisible(false);
				description.setVisible(true);
			}
		};
		
		// add a description panel under the top panel
		CardLayout descriptionLayout = new CardLayout();
		setLayout(descriptionLayout);
		
		setOpaque(false);
		setBorder(new EmptyBorder(
				new Insets(DESCRIPTION_PANEL_PADDING, DESCRIPTION_PANEL_PADDING,
						DESCRIPTION_PANEL_PADDING, DESCRIPTION_PANEL_PADDING)));
		
		descriptionText = component.getDescription();
		
		description = new JTextArea(descriptionText);
		description.setBackground(defaultColor);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		
		editableDescription = new JTextArea(descriptionText);
		editableDescription.setBackground(Color.WHITE);
		editableDescription.setLineWrap(true);
		editableDescription.setWrapStyleWord(true);
		editableDescription.setEditable(true);
		editableDescription.setVisible(false);
		
		add(description, BorderLayout.LINE_START);
		add(editableDescription, BorderLayout.LINE_START);
		setBackground(Color.WHITE);
		
		description.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//JPanel target = (JPanel) e.getSource();
					if(!DescriptionPanel.this.component.getLockedStatus()){
						description.setVisible(false);
						editableDescription.setVisible(true);
					}
				}
			}
		});

		//editableDescription.addActionListener(descriptionListener); // useless: not a text field anymore
		editableDescription.addFocusListener(descriptionFocusListener);
		
		//Register Listeners
		component.registerComponentDescriptionChangeListener(this);
		
	}
	
	//TODO this might be set directly by the controller
	protected void setDescription(String descriptionText) {
		descriptionText = descriptionText.trim();
		/*//description right trim
		String tmp = "";
		for(int i=descriptionText.length()-1; i>=0; i++) {
			if (descriptionText.charAt(i)==a) tmp+="descriptionText.charAt(i);
		}
		descriptionText = tmp;*/ 
		this.descriptionText = descriptionText;
		description.setText(descriptionText);
		editableDescription.setText(descriptionText);
		this.controller.setDescription(descriptionText);
	}
	
	@Override
	public void ComponentDescriptionChanged(Component component1) {
		
		System.out.println("Components name changed to: "
				+ component.getName());
		//TODO
	}
	
}
