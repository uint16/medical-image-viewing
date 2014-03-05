import java.awt.GridLayout;
import java.awt.LayoutManager;

/**
 * class representing the four-up (2 by 2) display strategy
 * @author Ethan Davidson (emd1771)
 *
 */
public class FourUp implements DisplayMode {
	private static int IMG_PER_PAGE = 4;

	@Override
	public int nextIndex(int index, Study s) {
		if(index+IMG_PER_PAGE <= s.imgAmt()){
			return index + IMG_PER_PAGE;
		} else {
			return index;
		}
	}

	@Override
	public int prevIndex(int index, Study s) {
		if(index-IMG_PER_PAGE >= 0){
			return index - IMG_PER_PAGE;
		} else {
			return index;
		}
	}

	@Override
	public LayoutManager getLayout() {
		return new GridLayout(2, 2);
	}

	@Override
	public int[] getIndices(int index) {
		int[] result = new int[IMG_PER_PAGE];
		for(int i = 0; i < result.length; i++){
			result[i] = IMG_PER_PAGE*((int) Math.floor(index/IMG_PER_PAGE))+i;
		}
		return result;
	}

	@Override
	public boolean hasPrev(int index, Study s) {
		return prevIndex(index, s) != index;
	}

	@Override
	public boolean hasNext(int index, Study s) {
		return nextIndex(index, s) != index;
	}

}
