package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import commandFramework.SaveCommand;

import controller.StudyController;


public class UnsavedStatePrompt extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private StudyController controller;


	/**
	 * Prompts the user to save the display state
	 * For use when loading a new display state and the current state is unsaved (i.e. would be lost)
	 * @param s DisplayState to save
	 */
	public UnsavedStatePrompt(StudyController s) {
		setTitle("Unsaved Display State");
		setLocationByPlatform(true);
		controller = s;
		setModal(true);
		setBounds(100, 100, 400, 150);
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
				JButton saveButton = new JButton("Save");
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new SaveCommand(controller.curState).execute();
						dispose();
					}
				});
				saveButton.setActionCommand("OK");
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
			}
			{
				JButton exitButton = new JButton("Continue without saving");
				exitButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
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
