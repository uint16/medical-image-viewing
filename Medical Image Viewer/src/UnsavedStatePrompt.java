import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class UnsavedStatePrompt extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private DisplayState state;

	/**
	 * Create the dialog.
	 */
	public UnsavedStatePrompt(DisplayState s) {
		state = s;
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTextArea txtrYourDisplayState = new JTextArea();
			txtrYourDisplayState.setWrapStyleWord(true);
			txtrYourDisplayState.setLineWrap(true);
			txtrYourDisplayState.setEditable(false);
			txtrYourDisplayState.setText("Your display state is unsaved. \r\nIf you exit without saving your state will not be saved.");
			contentPanel.add(txtrYourDisplayState);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton saveButton = new JButton("Save and exit");
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						state.save();
						System.exit(0);
					}
				});
				saveButton.setActionCommand("OK");
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
			}
			{
				JButton exitButton = new JButton("Exit without saving");
				exitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				exitButton.setActionCommand("Cancel");
				buttonPane.add(exitButton);
			}
		}
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

}
