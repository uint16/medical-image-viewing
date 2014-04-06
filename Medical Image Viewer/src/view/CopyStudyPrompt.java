package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

public class CopyStudyPrompt extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private String result;
	private JCheckBox subStudies;
	private Boolean subs;

	/**
	 * Create the dialog.
	 */
	public CopyStudyPrompt() {
		setTitle("Copy Study");
		setModal(true);
		setLocationByPlatform(true);
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);

		JLabel lblPleaseEnterA = new JLabel(
				"Please enter a new name for this copy of the study:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblPleaseEnterA, 10,
				SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblPleaseEnterA, 10,
				SpringLayout.WEST, contentPanel);
		contentPanel.add(lblPleaseEnterA);

		textField = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, textField, 6,
				SpringLayout.SOUTH, lblPleaseEnterA);
		sl_contentPanel.putConstraint(SpringLayout.WEST, textField, 0,
				SpringLayout.WEST, lblPleaseEnterA);
		sl_contentPanel.putConstraint(SpringLayout.EAST, textField, -10,
				SpringLayout.EAST, contentPanel);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						result = textField.getText();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						result = "";
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			{
				subStudies = new JCheckBox("Include Sub-Studies");
				subStudies.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						if(subStudies.isSelected())
							subs = true;
						else
							subs = false;
					}
				});
				buttonPane.add(subStudies);
			}
		}
	}

	public Boolean getSubs() {
		return subs;
	}

	public String showCopyStudyPrompt() {
		this.setVisible(true);
		return result;
	}
}
