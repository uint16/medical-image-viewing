package displayStrategyFramework;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.JPanel;

import model.MedicalImage;
import model.Study;
import view.ImagePanel;

/**
 * Class representing the coronal reconstruction strategy
 * Layout is a 2x2 grid
 * top left grid is a "oneup" view of the study
 * bottom left grid is a coronal reconstruction of the study
 * 
 * @author Ethan Davidson (emd1771)
 *
 */
public class CoronalReconstructionStrategy implements DisplayStrategy {
	private int reconstructionIndex;
	private transient ImagePanel studyPanel;
	
	public CoronalReconstructionStrategy(){
		reconstructionIndex = 0;
	}

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
	public JPanel getPanel(int index, Study s, int low, int high) {
		JPanel result = new JPanel();
		result.setLayout(new GridLayout(2, 2));
		
		//add study image with line showing reconstructionIndex
		MedicalImage studyImg = s.getImage(index);
		BufferedImage copy = studyImg.getImageCopy();
		Graphics2D cg = copy.createGraphics();
		cg.setColor(Color.RED);
		//The line is 3px wide so that it always shows, even when image scaling causes
		//	the line of pixels at reconstructionIndex to not be shown
		cg.fillRect(0, reconstructionIndex-1, copy.getWidth(), 3);
		studyPanel = new ImagePanel(copy);
		result.add(studyPanel);
		
		//create and add coronal reconstruction
		BufferedImage coronalRecon = new BufferedImage(studyImg.getWidth(), s.imgAmt(), studyImg.getType());
		Graphics2D crg = coronalRecon.createGraphics();
		for(int i = 0; i < s.imgAmt(); i++){
			MedicalImage tmpImg = s.getImage(i);
			for(int j = 0; j < tmpImg.getWidth(); j++){
				int rgb = tmpImg.getRGB(j, reconstructionIndex);
				try{
					coronalRecon.setRGB(j, coronalRecon.getHeight()-i-1, rgb);
				} catch(ArrayIndexOutOfBoundsException e){
					System.err.printf("Warning: pixel out of bounds: (%d, %d)\n", j, tmpImg.getHeight()-i-1);
				}
			}
		}
		//add index line to reconstruction
		crg.setColor(Color.RED);
		crg.fillRect(0, s.imgAmt()-index-1, coronalRecon.getWidth(), 3);
		result.add(new ImagePanel(coronalRecon));
		
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

	@Override
	public void setReconstructionIndex(Point p) {
		reconstructionIndex = p.y;
	}

	@Override
	public ImagePanel getStudyPanel() {
		return studyPanel;
	}

}
