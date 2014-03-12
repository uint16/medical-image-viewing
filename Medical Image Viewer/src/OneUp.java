import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.Serializable;

/**
 * Class respresenting the one-up display strategy
 * @author Ethan Davidson (emd1771)
 *
 */
public class OneUp implements DisplayMode, Serializable {


	@Override
	public int nextIndex(int index, Study s) {
		if(index < s.imgAmt()){
			return index + 1;
		} else {
			return index;
		}
	}

	@Override
	public int prevIndex(int index, Study s) {
		if(index > 0){
			return index - 1;
		} else {
			return index;
		}
	}

	@Override
	public LayoutManager getLayout() {
		return new GridLayout(1, 1);
	}

	@Override
	public int[] getIndices(int index) {
		int[] result = new int[1];
		result[0] = index;
		return result;
	}

	@Override
	public boolean hasPrev(int index, Study s) {
		return index > 0;
	}

	@Override
	public boolean hasNext(int index, Study s) {
		return index < s.imgAmt() - 1;
	}
}
