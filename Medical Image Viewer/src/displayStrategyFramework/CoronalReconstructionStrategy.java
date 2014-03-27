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
public class CoronalReconstructionStrategy implements DisplayStrategy, Serializable {
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
		
		//create and add reconstruction
		BufferedImage recon = new BufferedImage(studyImg.getWidth(), s.imgAmt(), studyImg.getType());
		Graphics2D rg = recon.createGraphics();
		for(int i = 0; i < s.imgAmt(); i++){
			MedicalImage tmpImg = s.getImage(i);
			rg.drawImage(tmpImg.getSubImage(0, reconstructionIndex, tmpImg.getWidth(), 1), 0, s.imgAmt()-i-1, null);
		}
		//add index line to reconstruction
		rg.setColor(Color.RED);
		rg.fillRect(0, s.imgAmt()-index-1, recon.getWidth(), 3);
		
		result.add(new ImagePanel(recon));
		
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
	public Point getReconstructionIndex() {
		return new Point(0, reconstructionIndex);
	}

	@Override
	public ImagePanel getStudyPanel() {
		return studyPanel;
	}

}
