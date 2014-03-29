package view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class WindowingValuesPrompt extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] selectedValues = new int[2];
	private JLabel lblLowCutoff, lblHighCutoff;
	private JSlider highSlider, lowSlider ;
	
	/**
	 * Create the dialog.
	 */
	public WindowingValuesPrompt() {
		setMaximumSize(new Dimension(275, 2147483647));
		setMinimumSize(new Dimension(275, 0));
		setLocationByPlatform(true);
		setModal(true);
		setBounds(100, 100, 275, 229);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);
		
		JButton btnSetWindowing = new JButton("Set Windowing");
		btnSetWindowing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnSetWindowing, -10, SpringLayout.SOUTH, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnSetWindowing, -10, SpringLayout.EAST, getContentPane());
		getContentPane().add(btnSetWindowing);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedValues = null;
				dispose();
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, btnCancel, 0, SpringLayout.SOUTH, btnSetWindowing);
		springLayout.putConstraint(SpringLayout.EAST, btnCancel, -6, SpringLayout.WEST, btnSetWindowing);
		getContentPane().add(btnCancel);
		
		lblLowCutoff = new JLabel("Low Cutoff");
		springLayout.putConstraint(SpringLayout.NORTH, lblLowCutoff, 10, SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblLowCutoff, 10, SpringLayout.WEST, getContentPane());
		getContentPane().add(lblLowCutoff);
		
		lowSlider = new JSlider();
		lowSlider.setValue(0);
		lowSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				lblLowCutoff.setText("Low Cutoff: " + lowSlider.getValue());
				if(!source.getValueIsAdjusting()){
					selectedValues[0] = source.getValue();
				}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, lowSlider, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, lowSlider, -10, SpringLayout.EAST, getContentPane());
		lowSlider.setMaximum(255);
		springLayout.putConstraint(SpringLayout.NORTH, lowSlider, 6, SpringLayout.SOUTH, lblLowCutoff);
		getContentPane().add(lowSlider);
		
		lblHighCutoff = new JLabel("High Cutoff");
		springLayout.putConstraint(SpringLayout.NORTH, lblHighCutoff, 6, SpringLayout.SOUTH, lowSlider);
		springLayout.putConstraint(SpringLayout.WEST, lblHighCutoff, 0, SpringLayout.WEST, lblLowCutoff);
		getContentPane().add(lblHighCutoff);
		
		highSlider = new JSlider();
		highSlider.setValue(255);
		highSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				lblHighCutoff.setText("High Cutoff: " + highSlider.getValue());
				if(!source.getValueIsAdjusting()){
					selectedValues[1] = source.getValue();					
				}
			}
		});
		springLayout.putConstraint(SpringLayout.WEST, highSlider, 10, SpringLayout.WEST, getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, highSlider, -10, SpringLayout.EAST, getContentPane());
		highSlider.setMaximum(255);
		springLayout.putConstraint(SpringLayout.NORTH, highSlider, 6, SpringLayout.SOUTH, lblHighCutoff);
		getContentPane().add(highSlider);
	}
	
	/**
	 * displays the (modal) prompt to the user, and returns their input to the caller
	 * 
	 * @return an array of length 2 
	 * 	where the first element is the low cutoff 
	 * 	and the second element is the high cutoff
	 */
	public int[] showPrompt(){
		this.setVisible(true);
		return selectedValues;
	}
}
