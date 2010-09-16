package org.u_compare.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.u_compare.gui.control.ComponentController;
import org.u_compare.gui.control.DropTargetController;
import org.u_compare.gui.debugging.GUITestingHarness;
import org.u_compare.gui.model.LockedStatusChangeListener;
import org.u_compare.gui.model.MinimizedStatusChangeListener;
import org.u_compare.gui.model.SubComponentsChangedListener;
import org.u_compare.gui.model.UIMAAggregateComponent;
import org.u_compare.gui.model.UIMAComponent;

/**
 * TODO:
 * 
 * @author pontus
 * @author olaf
 * @version 2009-08-26
 */
@SuppressWarnings("serial")
public class WorkflowComponent extends DraggableJPanel implements
		SubComponentsChangedListener, LockedStatusChangeListener, MinimizedStatusChangeListener {

	private final static String ICON_CLOSE_PATH = "gfx/icon_close1.png";
	private final static String ICON_CLOSE_PATH_HIGHLIGHT = "gfx/icon_close1highlight.png";
	private final static String ICON_MAX_PATH = "gfx/icon_maximize1.png";
	private final static String ICON_MIN_PATH = "gfx/icon_minimize1.png";
	private final static String ICON_LOCKED_PATH = "gfx/icon_locked.png";
	private final static String ICON_UNLOCKED_PATH = "gfx/icon_unlocked.png";

	private final static int PREFERRED_WIDTH = 300;

	private static final int INNER_PADDING = 5;
	private static final int AGGREGATE_PADDING = 5;

	private static final int BORDER_ROUNDING = 7;
	private static final int BORDER_WIDTH = 3;
	private static final Color BORDER_COLOR = Color.DARK_GRAY;
	private static Color HEADER_COLOR = Color.WHITE;
	private static Color BODY_COLOR = Color.LIGHT_GRAY;

	private static final int BUTTON_DECREMENT = 0;

	private static boolean iconsLoaded = false;
	private static ImageIcon minIcon;
	private static ImageIcon maxIcon;
	private static ImageIcon lockedIcon;
	private static ImageIcon unlockedIcon;
	private static ImageIcon closeIcon;

	private JPanel innerPanel;
	private JPanel topPanel;
	private JPanel aggregatePanel;
	private JButton minButton; // = new
								// BasicArrowButton(BasicArrowButton.SOUTH);
	private JButton lockButton;
	protected String title;
	private JPanel buttonPanel;
	private JButton closeButton;

	private ActionListener closeListener;
	private ActionListener minListener;
	private ActionListener lockListener;
	private ActionListener titleListener;
	private ActionListener descriptionListener;

	private UIMAComponent component;
	// private BasicArrowButton button = new
	// BasicArrowButton(BasicArrowButton.SOUTH);

	private ComponentController controller;
	private JPanel titlePanel;
	private JLabel titleLabel;
	private JTextField titleTextField;
	private ActionListener closeHighlight;
	private ActionListener closeUnhighlight;
	private BevelBorder highlighted;
	private Border empty;
	private JButton button;
	private JPanel descriptionPanel;
	private JTextArea description;
	private JTextField editableDescription;
	private String descriptionText;

	public WorkflowComponent(/* ArrayList<WorkflowComponent> subComponents, */
	ArrayList<ParameterPanel> paramPanels, UIMAComponent component,
			ComponentController controller) {
		super(controller);
		this.controller = controller;

		component.registerLockedStatusChangeListener(this);
		component.registerMinimizedStatusChangeListener(this);
		
		this.setAutoscrolls(true); //TODO TEST
		BODY_COLOR = defaultColor;

		this.setOpaque(false);
		if (!component.isWorkflow()) {
			this.setBorder(new RoundedBorder(null, BORDER_COLOR, BODY_COLOR,
					BORDER_ROUNDING, BORDER_WIDTH, false));
		}

		// set the buttons
		closeListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				removeComponent();
			}
		};

		minListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				toggleSize();
			}
		};

		lockListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				toggleLock();
			}
		};

		titleListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setTitle(titleTextField.getText());
				titleTextField.setVisible(false);
				titleLabel.setVisible(true);
			}
		};
		
		descriptionListener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setDescription(editableDescription.getText());
				editableDescription.setVisible(false);
				description.setVisible(true);
			}
			
		};

		closeHighlight = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				closeButton.setBorderPainted(true);
			}
		};

		closeUnhighlight = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				closeButton.setBorderPainted(false);
			}
		};

		// set the layout
		this.component = component;

		if (component.isAggregate()) {
			((UIMAAggregateComponent) component)
					.registerSubComponentsChangedListener(this);
		}

		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		// set up an inner panel and its layout
		innerPanel = new JPanel();
		BoxLayout innerLayout = new BoxLayout(innerPanel, BoxLayout.Y_AXIS);
		innerPanel.setLayout(innerLayout);
		innerPanel.setOpaque(false);
		innerPanel.setBorder(new EmptyBorder(INNER_PADDING, INNER_PADDING,
				INNER_PADDING, INNER_PADDING));

		// set up a top panel (inside the inner panel) and layout
		topPanel = new JPanel();
		BorderLayout topLayout = new BorderLayout();
		topPanel.setLayout(topLayout);
		topPanel.setOpaque(false);
		if (!component.isWorkflow()) {
			topPanel.setBorder(new RoundedBorder(null, BORDER_COLOR,
					HEADER_COLOR, BORDER_ROUNDING, BORDER_WIDTH, true));

			// add a title panel
			CardLayout titleLayout = new CardLayout();
			titlePanel = new JPanel(titleLayout);

			title = component.getName();
			titleLabel = new JLabel(title); // title = new
											// JLabel(component.getName());
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
						JPanel target = (JPanel) e.getSource();
						titleLabel.setVisible(false);
						titleTextField.setVisible(true);
					}
				}
			});

			titleTextField.addActionListener(titleListener);

			// add buttons to expand/collapse/remove the component
			buttonPanel = new JPanel();
			buttonPanel.setOpaque(false);
			FlowLayout buttonLayout = new FlowLayout(FlowLayout.TRAILING);
			buttonPanel.setLayout(buttonLayout);

			WorkflowComponent.loadIcons(); // TODO figure out how to load the
											// icons in a better way (xml ?)

			Dimension buttonSize;
			minButton = new JButton(minIcon);
			buttonSize = new Dimension(minIcon.getIconWidth()
					- BUTTON_DECREMENT, minIcon.getIconHeight()
					- BUTTON_DECREMENT);
			minButton.setPreferredSize(buttonSize);
			minButton.setActionCommand("hide component");
			minButton.addActionListener(minListener);
			buttonPanel.add(minButton);

			ImageIcon lockIcon = component.getLockedStatus()?lockedIcon:unlockedIcon;
			lockButton = new JButton(lockIcon);
			buttonSize = new Dimension(unlockedIcon.getIconWidth()
					- BUTTON_DECREMENT, unlockedIcon.getIconHeight()
					- BUTTON_DECREMENT);
			lockButton.setPreferredSize(buttonSize);
			lockButton.setActionCommand("show component");
			lockButton.addActionListener(lockListener);
			buttonPanel.add(lockButton);

			closeButton = new JButton(closeIcon);
			buttonSize = new Dimension(closeIcon.getIconWidth()
					- BUTTON_DECREMENT, closeIcon.getIconHeight()
					- BUTTON_DECREMENT);
			closeButton.setPreferredSize(buttonSize);
			closeButton.setActionCommand("remove component");
			closeButton.addActionListener(closeListener);
			buttonPanel.add(closeButton);
			
			// set button highlighting
			button = closeButton; 
		    highlighted = new BevelBorder(BevelBorder.RAISED, Color.LIGHT_GRAY, Color.DARK_GRAY);
		    
		    empty = closeButton.getBorder();
		    closeButton.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					closeButton.setBorder(highlighted);
				}

				public void mouseExited(MouseEvent e) {
					closeButton.setBorder(empty);
				}
			});
		    
		    empty = lockButton.getBorder();
		    lockButton.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					lockButton.setBorder(highlighted);
				}

				public void mouseExited(MouseEvent e) {
					lockButton.setBorder(empty);
				}
			});
		    
		    empty = minButton.getBorder();
		    minButton.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					minButton.setBorder(highlighted);
				}

				public void mouseExited(MouseEvent e) {
					minButton.setBorder(empty);
				}
			});
		    
			
			topPanel.add(buttonPanel, BorderLayout.LINE_END);

			this.add(topPanel, BorderLayout.NORTH);
			//String descriptionText = "<html><body><"+component.getDescription()+"</body></html>";
			
			
			// add a description panel under the top panel
			CardLayout descriptionLayout = new CardLayout();
			descriptionPanel = new JPanel(descriptionLayout);
			
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
						description.setVisible(false);
						editableDescription.setVisible(true);
					}
				}
			});

			editableDescription.addActionListener(descriptionListener);

			innerPanel.add(descriptionPanel);
		}

		this.add(innerPanel);

		// Set up the parameter panel if necessary
		if (paramPanels.size() > 0) {

			JPanel parameterPanel = new JPanel();
			parameterPanel.setBorder(new TitledBorder(new EtchedBorder(),
					"Configuration Parameters:"));
			parameterPanel.setLayout(new BoxLayout(parameterPanel,
					BoxLayout.Y_AXIS));
			parameterPanel.setOpaque(false);
			innerPanel.add(parameterPanel);

			for (ParameterPanel paramPanel : paramPanels) {
				paramPanel.setOpaque(false);
				parameterPanel.add(paramPanel);
			}
		}

		//Check if the component is minimized
		if(component.getMinimizedStatus()){
			this.innerPanel.setVisible(false);
			this.minButton.setIcon(maxIcon);
		}
		
		setupSubComponents();

	}

	private void setupSubComponents() {
		// set up the aggregate panel if necessary
		if (component.isAggregate()) {

			if (aggregatePanel == null) {
				JPanel aggregatePanelBorder = new JPanel();
				aggregatePanelBorder.setLayout(new GridLayout());
				aggregatePanelBorder.setOpaque(false);
				aggregatePanelBorder
						.setBorder(new TitledBorder(new EtchedBorder(),
								component.isWorkflow() ? "Workflow:"
										: "Subcomponents:"));
				innerPanel.add(aggregatePanelBorder);

				aggregatePanel = new JPanel();
				BoxLayout aggregateLayout = new BoxLayout(aggregatePanel,
						BoxLayout.Y_AXIS);
				aggregatePanel.setLayout(aggregateLayout);
				aggregatePanel.setOpaque(false);
				aggregatePanel.setBorder(new EmptyBorder(0, AGGREGATE_PADDING,
						0, AGGREGATE_PADDING));
				aggregatePanelBorder.add(aggregatePanel);
			}

			DropTargetController initialControl = new DropTargetController(
					controller);
			controller.addFirstDropTarget(initialControl);
			DropTargetJPanel initial = new DropTargetJPanel(initialControl);
			initialControl.setView(initial);
			aggregatePanel.add(initial);

			/*
			 * for (WorkflowComponent subComponent : subComponents) {
			 * aggregatePanel.add(subComponent); DropTargetController control =
			 * new DropTargetController(controller);
			 * controller.addDropTarget(control); DropTargetJPanel following =
			 * new DropTargetJPanel(control); control.setView(following);
			 * aggregatePanel.add(following); }
			 */

			for (UIMAComponent subModel : component.getSubComponents()) {
				ComponentController subController = new ComponentController(
						subModel);
				WorkflowComponent subView = subController.getView();
				aggregatePanel.add(subView);
				DropTargetController control = new DropTargetController(
						controller);
				DropTargetJPanel following = new DropTargetJPanel(control);
				control.setView(following);
				aggregatePanel.add(following);
				controller.insert(subController, control);

			}
		}
	}

	protected void toggleLock() {
		if (this.controller.isLocked()) {
			this.controller.setLocked(false);
		} else {
			this.controller.setLocked(true); 
		}
	}

	
	protected void setTitle(String title) { //TODO this might be set directly by the controller
		this.title = title;
		titleLabel.setText(title);
		titleTextField.setText(title);
		this.controller.setTitle(title);
	}
	
	protected void setDescription(String descriptionText) { //TODO this might be set directly by the controller
		this.descriptionText = descriptionText;
		description.setText(descriptionText);
		editableDescription.setText(descriptionText);
		this.controller.setDescription(descriptionText);
	}

	protected String getTitle() {
		return title;
	}

	protected void toggleSize() {
		
		controller.toggleMinimized();

	}

	private static void loadIcons() {
		if (iconsLoaded)
			return;
		URL image_url;
		image_url = WorkflowComponent.class
				.getResource(WorkflowComponent.ICON_MIN_PATH);
		WorkflowComponent.minIcon = new ImageIcon(image_url, "Minimize");

		image_url = WorkflowComponent.class
				.getResource(WorkflowComponent.ICON_MAX_PATH);
		WorkflowComponent.maxIcon = new ImageIcon(image_url, "Maximize");

		image_url = WorkflowComponent.class
				.getResource(WorkflowComponent.ICON_LOCKED_PATH);
		WorkflowComponent.lockedIcon = new ImageIcon(image_url, "Lock");

		image_url = WorkflowComponent.class
				.getResource(WorkflowComponent.ICON_UNLOCKED_PATH);
		WorkflowComponent.unlockedIcon = new ImageIcon(image_url, "Unlock");

		image_url = WorkflowComponent.class
				.getResource(WorkflowComponent.ICON_CLOSE_PATH);
		WorkflowComponent.closeIcon = new ImageIcon(image_url, "Remove");

		iconsLoaded = true;
	}

	private void removeComponent() {
		System.out.println("Remove the component, please.");
		this.controller.removeComponent();
	}

	private void resetSubComponents() {

		aggregatePanel.removeAll();

		// TODO is there anything else I need to clear here?
		controller.resetSubComponents();

	}

	public void subComponentAddRemoved(ArrayList<UIMAComponent> subComponents) {

		// TODO remove the argument from the listener interface

		System.out
				.println("View: subComponentAddRemoved Listner called on component: "
						+ component.getName());

		resetSubComponents();
		setupSubComponents();
		controller.validateWorkflow();// TODO this needs to validate at a higher
										// level

		// Reset both the view and controller for these subcomponents

		// TODO Auto-generated method stub
	}

	public UIMAComponent getUIMAComponent() {
		return this.component;
	}

	public static void main(String[] argv) {
		// flowComponent = new WorkflowComponent();
		//
		// TestWindow("WorkFlowComponent Test", flow);

		GUITestingHarness.main(argv);
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

	@Override
	public void minimizedStatusChanged(UIMAComponent component) {

		if (component.getMinimizedStatus()) {
			this.innerPanel.setVisible(false);
			this.minButton.setIcon(maxIcon);
		} else {
			this.innerPanel.setVisible(true);
			this.minButton.setIcon(minIcon);
		}
		
	}

	@Override
	public void lockStatusChanged(UIMAComponent component) {
		
		if(component.getLockedStatus()){
			this.lockButton.setIcon(lockedIcon);
		}else{
			this.lockButton.setIcon(unlockedIcon);
		}
	}

}