package model;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.FourUpStrategy;

public class DisplayState extends Observable implements Serializable {
	private int index;
	public int highCutoff;
	public int lowCutoff;
	public DisplayStrategy strategy;
	public transient Study study;
	public transient boolean saved;
	public transient static BufferedImage emptyImg;

	public DisplayState(Study s) {
		saved = false;
		index = 0;
		highCutoff = 255;
		lowCutoff = 0;
		strategy = new FourUpStrategy();
		study = s;
		if(emptyImg == null){
			try{
				emptyImg = ImageIO.read(MedicalImage.class.getResource("/img/emptyImage.jpg"));
			} catch(IOException e){
				System.err.println("Empty image unable to be read!");
			}
		}
		load();
	}
	
	/**
	 * moves the index pointer to the next valid index
	 */
	public void next(){
		if(index < study.imgAmt() - 1){
			index = strategy.nextIndex(index, study);
			this.wasChanged();
		}
	}
	
	/**
	 * moves the index pointer to the prev valid index
	 */
	public void prev(){
		index = strategy.prevIndex(index, study);
		this.wasChanged();
	}
	
	/**
	 * Generates a JPanel containing the images currently being viewed
	 * If it is given indices which are out of range, displays them as a default emptyImg
	 * @return JPanel containing the images currently being viewed
	 */
	public JPanel generatePanel(){
		return strategy.getPanel(index, study, lowCutoff, highCutoff);
	}
	
	/**
	 * loads the display state from the text file in the study folder
	 * This should really be refactored out of this class, but that would be 
	 * more trouble than it's worth at this point
	 */
	private void load(){
		File saveFile = new File(study.folderPath, "displayState");
		boolean loadSuccess = true;
		
		if (saveFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(saveFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				DisplayState ds = (DisplayState) ois.readObject();
				this.index = ds.index;
				this.setStrategy(ds.strategy);
				this.setWindow(ds.highCutoff, ds.lowCutoff);
				
				ois.close();
				fis.close();
			} catch (IOException e) {
				System.err.println("Error loading display state: " + saveFile.toString());
				loadSuccess = false;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				loadSuccess = false;
			}
		}
		if(loadSuccess){	//if we loaded successfully, we are starting in a saved state
			saved = true;
		}
	}
	
	/**
	 * notifies observers of a change, and sets saved to false
	 * simple encapsulation of these lines that are always used together
	 */
	private void wasChanged(){
		this.setChanged();
		this.notifyObservers();
		this.saved = false;
	}

	/**
	 * sets the display mode to the given mode
	 * @param m DisplayMode to use
	 */
	public void setStrategy(DisplayStrategy m) {
		this.strategy = m;
		this.wasChanged();
	}
	
	/**
	 * 
	 * @return the current mode of the display
	 */
	public DisplayStrategy getMode(){
		return strategy;
	}
	
	/**
	 * 
	 * @return index of the top right image
	 */
	public int getIndex(){
		return index;
	}

	/**
	 * 
	 * @return True if there is a valid index previous to the current index
	 */
	public boolean hasPrev() {
		return strategy.hasPrev(index, study);
	}

	/**
	 * 
	 * @return True if there is a valid index after the current index
	 */
	public boolean hasNext() {
		return strategy.hasNext(index, study);
	}

	public void setReconstructionIndex(Point p) {
		strategy.setReconstructionIndex(p);
		this.wasChanged();
	}
	
	/**
	 * ensures that the given values are within legal bounds (0 to 255) 
	 * and sets them as the windowing intensity cutoffs
	 * @param low lower windowing cutoff, must be less than high
	 * @param high upper windowing cutoff, must be greater than low
	 */
	public void setWindow(int low, int high) {
		low = bound(low, 0, 255);
		high = bound(high, 0, 255);
		lowCutoff = low;
		highCutoff = high;
		for(MedicalImage i : study.images){
			i.setWindow(low, high);
		}
		this.wasChanged();
	}

	private int bound(int in, int low, int high){
		if(in < low){
			in = low;
		} else if (in > high){
			in = high;
		}
		return in;
	}
}
