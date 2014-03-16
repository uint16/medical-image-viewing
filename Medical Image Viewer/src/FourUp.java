import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.Serializable;

import javax.swing.JPanel;

/**
 * class representing the four-up (2 by 2) display strategy
 * @author Ethan Davidson (emd1771)
 *
 */
public class FourUp implements DisplayStrategy, Serializable {
	private static int IMG_PER_PAGE = 4;

	@Override
	public int nextIndex(int index, Study s) {
		if(IMG_PER_PAGE*((int) Math.floor(index/IMG_PER_PAGE)) + IMG_PER_PAGE < s.imgAmt()){
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

	private LayoutManager getLayout() {
		return new GridLayout(2, 2);
	}

	private int[] getIndices(int index) {
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

	@Override
	public JPanel getPanel(int index, Study s) {
		JPanel result = new JPanel();
		result.setLayout(getLayout());
		for (int i : getIndices(index)) {
			if(!s.inRange(i)){
				result.add(new ImagePanel(DisplayState.emptyImg));
			} else {
				result.add(new ImagePanel(s.getImage(i)));
			}
		}
		return result;
	}

}
