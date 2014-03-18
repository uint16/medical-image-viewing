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

public class SagittalReconstructionStrategy implements DisplayStrategy, Serializable {
	private int reconstructionIndex;
	transient ImagePanel studyPanel;
	
	public SagittalReconstructionStrategy(){
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
	public JPanel getPanel(int index, Study s) {
		JPanel result = new JPanel();
		result.setLayout(new GridLayout(2, 2));
		
		//add study image with line showing reconstructionIndex
		MedicalImage studyImg = s.getImage(index);
		BufferedImage copy = studyImg.getImageCopy();
		Graphics2D cg = copy.createGraphics();
		cg.setColor(Color.RED);
		//The line is 3px wide so that it always shows, even when image scaling causes
		//	the line of pixels at reconstructionIndex to not be shown
		cg.fillRect(reconstructionIndex-1, 0, 3, copy.getHeight());
		studyPanel = new ImagePanel(copy);
		result.add(studyPanel);
		
		//create and add reconstruction
		BufferedImage recon = new BufferedImage(studyImg.getHeight(), s.imgAmt(), studyImg.getType());
		for(int i = 0; i < s.imgAmt(); i++){
			MedicalImage tmpImg = s.getImage(i);
			BufferedImage subImg = (BufferedImage) tmpImg.getSubImage(reconstructionIndex, 0, 1, tmpImg.getHeight());
			//draw the column as a row
			for(int j = 0; j < subImg.getHeight(); j++){
				int rgb = subImg.getRGB(0, j);
				try{
					recon.setRGB(subImg.getHeight()-j-1, s.imgAmt()-i-1, rgb);
				} catch(ArrayIndexOutOfBoundsException e){
					//Since we don't explicitly check that the subImgs all fit within the reconstruction,
					//the setRGB may sometimes throw this exception. all it means is that a few pixels
					//are outside the image and won't be displayed.
					//This may occur when the images in the study aren't all the same size
					System.err.println("Warning: pixel out of bounds: " + Integer.toString(subImg.getHeight()-j-1) + Integer.toString(s.imgAmt()-i-1));
				}
			}
			//rg.drawImage(subImg, i, 0, null);
		}
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
		reconstructionIndex = p.x;
	}

	@Override
	public Point getReconstructionIndex() {
		return new Point(reconstructionIndex, 0);
	}

	@Override
	public ImagePanel getStudyPanel() {
		return studyPanel;
	}

}
