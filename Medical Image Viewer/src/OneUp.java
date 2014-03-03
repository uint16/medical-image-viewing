import java.awt.GridLayout;
import java.awt.LayoutManager;


public class OneUp implements DisplayMode {


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
	
}
