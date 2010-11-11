package org.u_compare.gui.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import org.u_compare.gui.DraggableJPanel;
import org.u_compare.gui.RoundedBorder;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.debugging.GUITestingHarness;
import org.u_compare.gui.model.DescriptionChangeListener;
import org.u_compare.gui.model.SubComponentsChangedListener;
import org.u_compare.gui.model.AggregateComponent;
import org.u_compare.gui.model.Component;
import org.u_compare.gui.model.Workflow;

/**
 * TODO: We do need to separate this monster into multiple classes.
 * 
 * @author pontus
 * @author olaf
 * @author luke
 * @version 2009-08-26
 */
@SuppressWarnings("serial")
public class ComponentPanel extends DraggableJPanel implements
		SubComponentsChangedListener, DescriptionChangeListener {

	private final static int PREFERRED_WIDTH = 300;

	private static final int DESCRIPTION_PANEL_PADDING = 5;

	private static final int BORDER_ROUNDING = 5;
	private static final int BORDER_WIDTH = 2;
	private static final Color BORDER_COLOR = Color.DARK_GRAY;
	private static Color HEADER_COLOR = Color.WHITE;
	private static Color BODY_COLOR;

	private JPanel innerPanel;
	private JPanel topPanel;
	protected String title;

	private ActionListener titleListener;
	private ActionListener descriptionListener;
	
	private FocusListener titleFocusListener;
	private FocusListener descriptionFocusListener;

	private Component component;

	private ComponentController controller;
	private JPanel titlePanel;
	private JLabel titleLabel;
	private JTextField titleTextField;

	private JPanel descriptionPanel;
	private JTextArea description;
	private JTextField editableDescription;
	private String descriptionText;
	private java.awt.Component textField;
	
	private ButtonPanel buttonPanel;
	private WorkflowControlPanel workflowControlPanel;
	private InputOutputPanel inputOutputPanel;
	private ParametersPanel parametersPanel;
	private SubComponentsPanel subComponentsPanel;
	private JPanel subComponentsContainer;
	
	protected ComponentPanel(ComponentController controller){
		super(controller);
	}
	
	public ComponentPanel(Component component,
			ComponentController controller) {
		
		super(controller);
		initialConfiguration(component, controller);
		
		if (component.isAggregate()) {
			((AggregateComponent) component)
					.registerSubComponentsChangedListener(this);
		}
		
		if (!component.isWorkflow()) {
			this.setBorder(new RoundedBorder(null, BORDER_COLOR, BODY_COLOR,
					BORDER_ROUNDING, BORDER_WIDTH, false));
		}
		
		//LUKES: WorkflowPanel up to here


		// set up a top panel (inside the inner panel) and layout
		topPanel = new JPanel();
		BorderLayout topLayout = new BorderLayout();
		topPanel.setLayout(topLayout);
		topPanel.setOpaque(false);

		setupInnerPanel();
		
		if (!component.isWorkflow()) {
			topPanel.setBorder(new RoundedBorder(null, BORDER_COLOR,
					HEADER_COLOR, BORDER_ROUNDING, BORDER_WIDTH, true));

			setupTitlePanel();
			setupButtonPanel(topPanel, innerPanel);
			this.add(topPanel, BorderLayout.NORTH);
		} else {
			setupTitlePanel();
			this.add(topPanel, BorderLayout.NORTH);
		}

		setupDescriptionPanel();
		if(component.isWorkflow()){
			setupWorkflowControlPanel(innerPanel);
		}
		if(!component.isWorkflow()){
			setupInputOutputPanel(innerPanel);
		}
		setupParametersPanel(innerPanel);
		
		
		if(!component.isWorkflow()){
			
			JPanel subComponentsBorder = new JPanel();
			subComponentsBorder.setLayout(new GridLayout());
			subComponentsBorder.setOpaque(false);
			subComponentsBorder.setBorder(new TitledBorder("Subcomponents:"));
			setupSubComponentsPanel(subComponentsBorder);
			innerPanel.add(subComponentsBorder);
			
		}else{
			
			setupSubComponentsPanel(innerPanel);
		}
		
		this.add(innerPanel);
	}
	
	protected void initialConfiguration(Component component,
			ComponentController controller){
		
		this.controller = controller;
		this.component = component;
		
		//Register Listeners
		component.registerComponentDescriptionChangeListener(this);
		
		//Set display properties
		BODY_COLOR = defaultColor;
		this.setOpaque(false);
		
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		
	}
	
	protected void setupTitlePanel(){
		
		titleListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTitle(titleTextField.getText());
				titleTextField.setVisible(false);
				titleLabel.setVisible(true);
			}
		};
		
		titleFocusListener = new FocusListener() {
			
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				setTitle(titleTextField.getText());
				titleTextField.setVisible(false);
				titleLabel.setVisible(true);
			}
		};
		
		// add a title panel
		titlePanel = new JPanel(new CardLayout());

		title = component.getName();
		titleLabel = new JLabel(title);
										
		titleTextField = new JTextField(title);
		titleLabel.setFont(new Font("sansserif", Font.BOLD, 12));
		titleTextField.setFont(new Font("sansserif", Font.BOLD, 12));
		titleTextField.setVisible(false);

		titlePanel.add(titleLabel, BorderLayout.LINE_START);
		titlePanel.add(titleTextField, BorderLayout.LINE_START);
		titlePanel.setBackground(Color.WHITE);
		topPanel.add(titlePanel, BorderLayout.LINE_START);

		titlePanel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//JPanel target = (JPanel) e.getSource();
					if(!component.getLockedStatus()){
						titleLabel.setVisible(false);
						titleTextField.setVisible(true);
					}
				}
			}
		});

		// the title JTextField has listeners 
		titleTextField.addActionListener(titleListener);
        titleTextField.addFocusListener(titleFocusListener);
	}
	
	protected void setupButtonPanel(JPanel target, JPanel minimizable){
		
		buttonPanel = new ButtonPanel(controller, component, minimizable);
		target.add(buttonPanel, BorderLayout.LINE_END);
		
	}
	
	protected void setupInnerPanel(){
		// set up an inner panel and its layout
		innerPanel = new JPanel();
		BoxLayout innerLayout = new BoxLayout(innerPanel, BoxLayout.Y_AXIS);
		innerPanel.setLayout(innerLayout);
		innerPanel.setOpaque(false);
		innerPanel.setBorder(new EmptyBorder(BORDER_WIDTH, BORDER_WIDTH,
				BORDER_WIDTH, BORDER_WIDTH));
		
	}
	
	protected void setupDescriptionPanel(){
		
		
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
				System.out.println("voilà");
			}
		};
		
		// add a description panel under the top panel
		CardLayout descriptionLayout = new CardLayout();
		descriptionPanel = new JPanel(descriptionLayout);
		
		descriptionPanel.setOpaque(false);
		descriptionPanel.setBorder(new EmptyBorder(
				new Insets(DESCRIPTION_PANEL_PADDING, DESCRIPTION_PANEL_PADDING,
						DESCRIPTION_PANEL_PADDING, DESCRIPTION_PANEL_PADDING)));
		
		descriptionText = component.getDescription();
		
		description = new JTextArea(descriptionText);
		description.setBackground(defaultColor);
		description.setLineWrap(true);
		description.setWrapStyleWord(true);
		description.setEditable(false);
		
		editableDescription = new JTextField(descriptionText);
		editableDescription.setBackground(defaultColor);
		//editableDescription.setLineWrap(true);
		//editableDescription.setWrapStyleWord(true);
		//editableDescription.setEditable(true);
		editableDescription.setVisible(false);
		
		descriptionPanel.add(description, BorderLayout.LINE_START);
		descriptionPanel.add(editableDescription, BorderLayout.LINE_START);
		descriptionPanel.setBackground(Color.WHITE);
		
		description.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					//JPanel target = (JPanel) e.getSource();
					if(!component.getLockedStatus()){
						description.setVisible(false);
						editableDescription.setVisible(true);
					}
				}
			}
		});

		editableDescription.addActionListener(descriptionListener);
		editableDescription.addFocusListener(descriptionFocusListener);

		innerPanel.add(descriptionPanel);
	}
	
	protected void setupWorkflowControlPanel(JPanel target){
		
		workflowControlPanel = new WorkflowControlPanel((Workflow)component,
				controller);
		target.add(workflowControlPanel);
		
	}

	protected void setupInputOutputPanel(JPanel target){
		
		inputOutputPanel = new InputOutputPanel(component, controller);
		target.add(inputOutputPanel);

	}
	
	protected void setupParametersPanel(JPanel target){
		
		parametersPanel = new ParametersPanel(component, controller);
		target.add(parametersPanel);
	
	}
	
	protected void setupSubComponentsPanel(JPanel target) {
		
		//TODO make sure it is only setup in one location
		
		subComponentsContainer = target;
		
		// set up the aggregate panel if necessary
		if (component.isAggregate()) {

			if(subComponentsPanel != null){
				target.remove(subComponentsPanel);
				target.validate();
			}
			
			subComponentsPanel = new SubComponentsPanel(component, controller);
			target.add(subComponentsPanel);
			
		}
	}
	
	private void resetSubComponents(){
		
		subComponentsContainer.remove(subComponentsPanel);
		setupSubComponentsPanel(subComponentsContainer);
		
	}

	//TODO this might be set directly by the controller
	protected void setTitle(String title) {
		this.title = title;
		titleLabel.setText(title);
		titleTextField.setText(title);
		this.controller.setTitle(title);
	}
	
	//TODO this might be set directly by the controller
	protected void setDescription(String descriptionText) {
		this.descriptionText = descriptionText;
		description.setText(descriptionText);
		editableDescription.setText(descriptionText);
		this.controller.setDescription(descriptionText);
	}
	
	public void subComponentsChanged() {

		controller.resetSubComponents();
		resetSubComponents();
		controller.validateWorkflow();// TODO this needs to validate at a higher
										// level
		
	}

	public Component getComponent() {
		return this.component;
	}

	public void setDragOverHighlightingDroppable() {
		setBackground(Color.LIGHT_GRAY);

	}

	public void setDragOverHighlightingUndroppable() {
		setBackground(Color.RED);

	}

	public void clearDragOverHighlighting() {
		setBackground(defaultColor);
	}

	/**
	 * Instead of setting the preferred size directly which would require
	 * specifying both horizontal and vertical dimensions, this method allows us
	 * to use the automatically calculated vertical dimension and override just
	 * the horizontal value.
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension ret = super.getPreferredSize();
		ret.width = PREFERRED_WIDTH;
		return ret;
	}

	/**
	 * Dynamically sets the maximum height to the current preferred height of
	 * the component to prevent stretching.
	 */
	@Override
	public Dimension getMaximumSize() {
		Dimension ret = super.getPreferredSize();
		Dimension max = super.getMaximumSize();
		ret.width = max != null ? max.width : 2000;
		return ret;
	}
	
	public static void main(String[] argv) {
		GUITestingHarness.main(argv);
	}



	@Override
	public void ComponentDescriptionChanged(Component component1) {
		
		System.out.println("Components name changed to: "
				+ component.getName());
		//TODO
	}
	
	public Workflow getWorkflow() {
		if (this.component.isWorkflow() == false) {
			//TODO: The proper exception here
			throw new IllegalArgumentException(
					"Can't get the workflow "
					+ "from a non-workflow ComponentPanel");
		}
		else {
			return (Workflow) this.component;
		}
	}
}