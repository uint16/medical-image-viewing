package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import controller.StudyController;


public class RootDirPrompt extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private StudyController sc;

	/**
	 * Create the dialog.
	 */
	public RootDirPrompt(StudyController s) {
		setModal(true);
		this.sc = s;
		
		setPreferredSize(new Dimension(450, 150));
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel labelDirections = new JLabel("Please specify the directory containing your studies.");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, labelDirections, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, labelDirections, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(labelDirections);
		
		textField = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.WEST, textField, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FileChooser fc = new FileChooser();
			    fc.setFilterFolder();
			    String filePath = fc.showFileChooser();
			    textField.setText(filePath);
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, textField, 0, SpringLayout.SOUTH, button);
		sl_contentPanel.putConstraint(SpringLayout.EAST, textField, -10, SpringLayout.WEST, button);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, button, -7, SpringLayout.SOUTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, button, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(button);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Exit");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				JButton okButton = new JButton("Continue");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String newDir = textField.getText();
						File newDirFile = new File(newDir);
						if (!newDir.isEmpty() && newDirFile.exists() && newDirFile.isDirectory()) {
							sc.setRootDir(newDirFile);
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		this.setVisible(true);
	}
}
