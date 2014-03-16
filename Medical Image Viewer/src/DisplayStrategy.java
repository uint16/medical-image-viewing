import javax.swing.JPanel;

/**
 * Interface representing a display mode strategy
 * @author Ethan Davidson (emd1771)
 *
 */
public interface DisplayStrategy {
	
	/**
	 * returns the next index, given the current index
	 * e.g. OneUp adds one, FourUp adds four
	 * TODO: move range checking to the display state so we don't have to pass the study
	 * @param index
	 * @param s
	 * @return int new index
	 */
	public int nextIndex(int index, Study s);
	
	/**
	 * returns the previous index, given the current one
	 * e.g. OneUp subtracts one, FourUp subtracts four
	 * TODO: move range checking to the display state so we don't have to pass the study
	 * @param index
	 * @param s
	 * @return int new index
	 */
	public int prevIndex(int index, Study s);

	/**
	 * Creates a panel displaying the study in the terms of the strategy
	 * @param index the index of the image currently being displayed
	 * @param s the study being displayed
	 * @return panel containing the images specified by the strategy 
	 * 			in the context of the current index and study
	 */
	public JPanel getPanel(int index, Study s);

	/**
	 * Given the current index, returns true if there is a valid previous index
	 */
	public boolean hasPrev(int index, Study s);

	/**
	 * Given the current index, returns true if there is a valid next index
	 */
	public boolean hasNext(int index, Study s);
}
