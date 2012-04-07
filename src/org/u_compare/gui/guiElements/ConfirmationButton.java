package org.u_compare.gui.guiElements;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Extended JButton that requires the user to confirm the action they have
 * chosen.
 * 
 * @author Luke McCrohon
 * 
 */
@SuppressWarnings("serial")
public class ConfirmationButton extends JButton {

	// Keys of the two views.
	private static final String NORMAL = "Normal";
	private static final String CONFIRM = "Confirm";

	private JButton mainButton;

	/**
	 * Create a ConfirmationButton with the specified mainButton. The
	 * confirmation message will be "Are you sure?", with "Yes" as the accept
	 * option, and "No" as the cancel option.
	 * 
	 * @param mainButton
	 */
	public ConfirmationButton(JButton mainButton) {
		this(mainButton, "Are you sure?");
	}

	/**
	 * Create a ConfirmationButton with the specified mainButton and the
	 * confirmation message. Accept option will be "Yes" and reject option will
	 * be "No".
	 * 
	 * @param mainButton
	 * @param confirmation
	 */
	public ConfirmationButton(JButton mainButton, String confirmation) {
		this(mainButton, confirmation, "Yes", "No");
	}

	/**
	 * Create a ConfirmationButton specifying the mainButton, confirm message
	 * and both accept and cancel messages.
	 * 
	 * @param mainButton
	 *            The Button to add.
	 * @param confirmation
	 *            The confirmation message to be shown when clicked.
	 * @param accept
	 *            The accept option message.
	 * @param cancel
	 *            The reject option message.
	 */
	public ConfirmationButton(JButton mainButton, String confirmation,
			String accept, String cancel) {
		super();

		setOpaque(false);
		setContentAreaFilled(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(new DynamicCardLayout());

		// Create the first "Normal" card
		JPanel normalPanel = new JPanel();
		normalPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 0, 0));
		normalPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		normalPanel.setOpaque(false);

		this.mainButton = mainButton;
		mainButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmationButton.this.requestFocusInWindow();// SO focus is
																// passed
				actionRequest();
			}
		});
		normalPanel.add(mainButton);
		add(normalPanel, NORMAL);

		// Create the second "Confirm" card
		JPanel confirmPanel = new JPanel();
		confirmPanel.setOpaque(false);
		confirmPanel.setVisible(false);
		confirmPanel.setLayout(new FlowLayout(FlowLayout.TRAILING, 5, 0));
		confirmPanel.add(new JLabel(confirmation));

		final HighlightButton confirmButton = new HighlightButton(accept);
		confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmationButton.this.requestFocusInWindow();// So focus is
																// passed
				confirm(e);
			}
		});
		confirmPanel.add(confirmButton);

		final HighlightButton cancelButton = new HighlightButton(cancel);
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConfirmationButton.this.requestFocusInWindow();// So focus is
																// passed
				cancel();
			}
		});
		confirmPanel.add(cancelButton);
		add(confirmPanel, CONFIRM);

		// If mouse leaves the area, take that as a cancel event.
		MouseListener listener = new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				cancel();
			}

			public void mouseEntered(MouseEvent e) {
				actionRequest();
			}
		};

		confirmPanel.addMouseListener(listener);
		cancelButton.addMouseListener(listener);
		confirmButton.addMouseListener(listener);

		CardLayout cl = (CardLayout) (getLayout());
		cl.show(this, NORMAL);
	}

	private void actionRequest() {
		CardLayout cl = (CardLayout) (getLayout());
		cl.show(this, CONFIRM);
	}

	private void confirm(ActionEvent e) {
		fireActionPerformed(e);
	}

	private void cancel() {
		CardLayout cl = (CardLayout) (getLayout());
		cl.show(this, NORMAL);
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			cancel();
		}
		mainButton.setEnabled(enabled);
	}

	@Override
	public void setToolTipText(String tooltip) {
		mainButton.setToolTipText(tooltip);
	}
}
