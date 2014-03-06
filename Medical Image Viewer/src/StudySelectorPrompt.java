import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;


public class StudySelectorPrompt extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private StudyController sc;

	private final JPanel contentPanel = new JPanel();
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox;
	private String selected;

	/**
	 * Popup prompt with a comboBox for selecting the study to view
	 * @param s StudyController context
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public StudySelectorPrompt(StudyController s) {
		setLocationByPlatform(true);
		setModal(true);
		sc = s;
		
		setBounds(100, 100, 450, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblSelectAStudy = new JLabel("Select a study:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblSelectAStudy, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblSelectAStudy, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblSelectAStudy);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(sc.getStudies().toArray()));
		sl_contentPanel.putConstraint(SpringLayout.NORTH, comboBox, 6, SpringLayout.SOUTH, lblSelectAStudy);
		sl_contentPanel.putConstraint(SpringLayout.WEST, comboBox, 0, SpringLayout.WEST, lblSelectAStudy);
		contentPanel.add(comboBox);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Select Study");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selected = comboBox.getSelectedItem().toString();
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
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public String showStudySelector(){
		this.setVisible(true);
		return selected;
	}
}
