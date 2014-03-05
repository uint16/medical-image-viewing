import java.awt.LayoutManager;

/**
 * Interface representing a display mode strategy
 * @author Ethan Davidson (emd1771)
 *
 */
public interface DisplayMode {
	
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
	 * 
	 * @return LayoutManager associated with this display mode
	 */
	public LayoutManager getLayout();

	/**
	 * Given the current index, returns the indices of the images to display
	 * @param index
	 * @return int[] array of indices representing images to display
	 */
	public int[] getIndices(int index);

	/**
	 * Given the current index, returns true if there is a valid previous index
	 */
	public boolean hasPrev(int index, Study s);

	/**
	 * Given the current index, returns true if there is a valid next index
	 */
	public boolean hasNext(int index, Study s);
}
