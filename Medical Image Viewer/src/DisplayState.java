import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DisplayState extends Observable implements Serializable {
	int index;
	DisplayStrategy mode;
	transient Study study;
	transient boolean saved;
	transient static BufferedImage emptyImg;

	public DisplayState(Study s) {
		saved = false;
		index = 0;
		mode = new FourUp();
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
		index = mode.nextIndex(index, study);
		this.wasChanged();
		}
	}
	
	/**
	 * moves the index pointer to the prev valid index
	 */
	public void prev(){
		index = mode.prevIndex(index, study);
		this.wasChanged();
	}
	
	/**
	 * Generates a JPanel containing the images currently being viewed
	 * If it is given indices which are out of range, displays them as a default emptyImg
	 * @return JPanel containing the images currently being viewed
	 */
	public JPanel generatePanel(){
		JPanel result = new JPanel();
		result.setLayout(mode.getLayout());
		for (int i : mode.getIndices(index)) {
			if(!study.inRange(i)){
				result.add(new ImagePanel(emptyImg));
			} else {
				result.add(new ImagePanel(study.getImage(i)));
			}
		}
		return result;
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
				this.mode = ds.mode;
				
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
	public void setMode(DisplayStrategy m) {
		this.mode = m;
		this.wasChanged();
	}
	
	/**
	 * 
	 * @return the current mode of the display
	 */
	public DisplayStrategy getMode(){
		return mode;
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
		return mode.hasPrev(index, study);
	}

	/**
	 * 
	 * @return True if there is a valid index after the current index
	 */
	public boolean hasNext() {
		return mode.hasNext(index, study);
	}
}
