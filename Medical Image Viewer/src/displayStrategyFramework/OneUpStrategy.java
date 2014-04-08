package displayStrategyFramework;

import java.awt.GridLayout;
import java.awt.Point;
import java.io.Serializable;

import javax.swing.JPanel;

import model.Study;
import view.ImagePanel;

/**
 * Class respresenting the one-up display strategy
 * @author Ethan Davidson (emd1771)
 *
 */
public class OneUpStrategy implements DisplayStrategy {
	private transient ImagePanel studyPanel;

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
	public boolean hasPrev(int index, Study s) {
		return index > 0;
	}

	@Override
	public boolean hasNext(int index, Study s) {
		return index < s.imgAmt() - 1;
	}

	@Override
	public JPanel getPanel(int index, Study s, int low, int high) {
		JPanel result = new JPanel();
		result.setLayout(new GridLayout(1, 1));
		
		studyPanel = new ImagePanel(s.getImage(index));
		result.add(studyPanel);

		return result;
	}

	@Override
	public void setReconstructionIndex(Point p) {
		return;
	}

	@Override
	public ImagePanel getStudyPanel() {
		return studyPanel;
	}
}
