package displayStrategyFramework;

import java.awt.GridLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.MedicalImage;
import model.Study;
import view.ImagePanel;

/**
 * class representing the four-up (2 by 2) display strategy
 * @author Ethan Davidson (emd1771)
 *
 */
public class FourUpStrategy implements DisplayStrategy {
	private static int IMG_PER_PAGE = 4;
	private transient ImagePanel studyPanel;
	private transient static BufferedImage emptyImg;
	
	public FourUpStrategy(){
		if(emptyImg == null){
			try{
				emptyImg = ImageIO.read(MedicalImage.class.getResource("/img/emptyImage.jpg"));
			} catch(IOException e){
				System.err.println("Empty image unable to be read!");
			}
		}
	}

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
	public JPanel getPanel(int index, Study s, int low, int high) {
		JPanel result = new JPanel();
		result.setLayout(new GridLayout(2, 2));
		boolean setStudy = false;
		
		for (int i : getIndices(index)) {
			if(!s.inRange(i)){
				result.add(new ImagePanel(emptyImg));
			} else {
				ImagePanel newPanel = new ImagePanel(s.getImage(i));
				if(!setStudy){
					studyPanel = newPanel;
					setStudy = true;
				}
				result.add(newPanel);
			}
		}
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
	
	@Override
	public String getSaveFileName() {
		return "FourUp";
	}
}
