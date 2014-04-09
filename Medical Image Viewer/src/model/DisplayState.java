package model;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Observable;

import javax.swing.JPanel;

import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.OneUpStrategy;

public class DisplayState extends Observable implements Serializable {
	private int index;
	private int highCutoff;
	private int lowCutoff;
	private DisplayStrategy curStrategy;
	private transient Study study;
	public transient boolean saved;

	public DisplayState(Study s) {
		//The initial state is technically a saved state, just hardcoded instead of serialized
		saved = true;
		index = 0;
		highCutoff = 255;
		lowCutoff = 0;
		curStrategy = new OneUpStrategy();
		study = s;
	}
	
	public Study getStudy() {
		return study;
	}
	
	public void setStudy(Study s) {
		this.study = s;
	}

	/**
	 * moves the index pointer to the next valid index
	 */
	public void next(){
		if(index < study.imgAmt() - 1){
			index = curStrategy.nextIndex(index, study);
			this.wasChanged();
		}
	}
	
	/**
	 * moves the index pointer to the prev valid index
	 */
	public void prev(){
		index = curStrategy.prevIndex(index, study);
		this.wasChanged();
	}
	
	/**
	 * Generates a JPanel containing the images currently being viewed
	 * If it is given indices which are out of range, displays them as a default emptyImg
	 * @return JPanel containing the images currently being viewed
	 */
	public JPanel generatePanel(){
		return curStrategy.getPanel(index, study, lowCutoff, getHighCutoff());
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
	 * if a saved instance of this mode exists, load and use that instance
	 * otherwise, use the new (given) instance
	 * 
	 * @param s new instance of the displayStrategy to use
	 */
	public void setStrategy(DisplayStrategy s) {
		//save the current strategy before dropping it
		saveCurStrategy();
		
		//attemp to load the new strategy
		File loadFile = study.getStrategySaveFile(s);
		boolean loadSuccess = false;
		if (loadFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(loadFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				curStrategy = (DisplayStrategy) ois.readObject();
				ois.close();
				fis.close();
				loadSuccess = true;
			} catch (IOException e) {
				System.err.println("Error loading display strategy: " + loadFile.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if(!loadSuccess){
			curStrategy = s;
		}
		this.wasChanged();
	}
	
	/**
	 * serializes and saves the current strategy
	 */
	private void saveCurStrategy(){
		File saveFile = study.getStrategySaveFile(curStrategy);
		try{
			FileOutputStream fileOut = new FileOutputStream(saveFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(curStrategy);
			out.close();
			fileOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
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
		return curStrategy.hasPrev(index, study);
	}

	/**
	 * 
	 * @return True if there is a valid index after the current index
	 */
	public boolean hasNext() {
		return curStrategy.hasNext(index, study);
	}

	public void setReconstructionIndex(Point p) {
		curStrategy.setReconstructionIndex(p);
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

	public int getHighCutoff() {
		return highCutoff;
	}

	public int getLowCutoff() {
		return lowCutoff;
	}

	public DisplayStrategy getCurStrategy() {
		return curStrategy;
	}
}
