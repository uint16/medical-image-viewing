import java.awt.LayoutManager;


public interface DisplayMode {
	
	public int nextIndex(int index, Study s);
	
	public int prevIndex(int index, Study s);

	public LayoutManager getLayout();

	public int[] getIndices(int index);
}
