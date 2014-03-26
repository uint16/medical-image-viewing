package model;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JPanel;

import displayStrategyFramework.CoronalReconstructionStrategy;
import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.FourUpStrategy;
import displayStrategyFramework.OneUpStrategy;
import displayStrategyFramework.SagittalReconstructionStrategy;

public class DisplayState extends Observable implements Serializable {
	private int index;
	private int highCutoff;
	private int lowCutoff;
	private ArrayList<DisplayStrategy> strategies;
	private DisplayStrategy curStrategy;
	private transient Study study;
	public transient boolean saved;

	public DisplayState(Study s) {
		saved = false;
		index = 0;
		highCutoff = 255;
		lowCutoff = 0;
		strategies = new ArrayList<DisplayStrategy>();
		strategies.add(new OneUpStrategy());
		strategies.add(new FourUpStrategy());
		strategies.add(new CoronalReconstructionStrategy());
		strategies.add(new SagittalReconstructionStrategy());
		curStrategy = strategies.get(0);
		study = s;
		load();
	}
	
	public Study getStudy() {
		return study;
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
	 * loads the display state from the text file in the study folder
	 * This should really be refactored out of this class, but that would be 
	 * more trouble than it's worth at this point
	 */
	private void load(){
		File saveFile = study.getSaveFile();
		boolean loadSuccess = true;
		
		if (saveFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(saveFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				DisplayState ds = (DisplayState) ois.readObject();
				this.index = ds.index;
				this.strategies = ds.strategies;
				this.setStrategy(ds.curStrategy);
				this.setWindow(ds.lowCutoff, ds.getHighCutoff());
				
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
		for(int i = 0; i < strategies.size(); i++){
			if(m.getClass() == strategies.get(i).getClass()){
				curStrategy = strategies.get(i);
			}
		}
		this.wasChanged();
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
