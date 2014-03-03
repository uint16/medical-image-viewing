import java.awt.GridLayout;
import java.awt.LayoutManager;


public class FourUp implements DisplayMode {

	@Override
	public int nextIndex(int index, Study s) {
		if(index+4 < s.imgAmt()){
			return index + 4;
		} else {
			return index;
		}
	}

	@Override
	public int prevIndex(int index, Study s) {
		if(index-4 > 0){
			return index - 4;
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
		int[] result = new int[4];
		result[0] = 4*((int) Math.floor(index/4))+0;
		result[1] = 4*((int) Math.floor(index/4))+1;
		result[2] = 4*((int) Math.floor(index/4))+2;
		result[3] = 4*((int) Math.floor(index/4))+3;
		return result;
	}

}
