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

public class ReconstructionStrategy implements DisplayStrategy {
	private Point reconstructionPoint;
	private transient ImagePanel studyPanel;
	
	public ReconstructionStrategy(){
		reconstructionPoint = new Point(0, 0);
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
		//The line is 3px wide so that it always shows, even when image scaling causes
		//	the line of pixels at reconstructionIndex to not be shown
		cg.setColor(Color.RED);
		cg.fillRect(0, reconstructionPoint.y-1, copy.getWidth(), 3);
		cg.setColor(Color.GREEN);
		cg.fillRect(reconstructionPoint.x-1, 0, 3, copy.getHeight());
		studyPanel = new ImagePanel(copy);
		result.add(studyPanel);
		
		//TODO: combine these loops to optimize performance
		//create and add sagittal reconstruction
		BufferedImage sagittalRecon = new BufferedImage(studyImg.getHeight(), s.imgAmt(), studyImg.getType());
		for(int i = 0; i < s.imgAmt(); i++){
			MedicalImage tmpImg = s.getImage(i);
			//draw the column as a row
			for(int j = 0; j < tmpImg.getHeight(); j++){
				int rgb = tmpImg.getRGB(reconstructionPoint.x, j);
				try{
					sagittalRecon.setRGB(tmpImg.getHeight()-j-1, s.imgAmt()-i-1, rgb);
				} catch(ArrayIndexOutOfBoundsException e){
					//Since we don't explicitly check that the subImgs all fit within the reconstruction,
					//the setRGB may sometimes throw this exception. all it means is that a few pixels
					//are outside the image and won't be displayed.
					//This may occur when the images in the study aren't all the same size
					System.err.printf("Warning: pixel out of bounds: (%d, %d)\n", tmpImg.getHeight()-j-1, s.imgAmt()-i-1);
				}
			}
		}
		//add index line to reconstruction
		Graphics2D rg = sagittalRecon.createGraphics();
		rg.setColor(Color.GREEN);
		rg.fillRect(0, s.imgAmt()-index-1, sagittalRecon.getWidth(), 3);
		result.add(new ImagePanel(sagittalRecon));
		
		//create and add coronal reconstruction
		BufferedImage coronalRecon = new BufferedImage(studyImg.getWidth(), s.imgAmt(), studyImg.getType());
		Graphics2D crg = coronalRecon.createGraphics();
		for(int i = 0; i < s.imgAmt(); i++){
			MedicalImage tmpImg = s.getImage(i);
			for(int j = 0; j < tmpImg.getWidth(); j++){
				int rgb = tmpImg.getRGB(j, reconstructionPoint.y);
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
		reconstructionPoint = p;
	}

	@Override
	public ImagePanel getStudyPanel() {
		return studyPanel;
	}

}
