import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

public class DisplayState extends Observable {
	int index;
	DisplayMode mode;
	Study study;
	static BufferedImage emptyImg;

	public DisplayState(Study s) {
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
	 * saves the display state to a text file in the study folder
	 * TODO: use something more robust than a text file (possibly preferences?)
	 */
	public void save() {
		File saveFile = new File(study.folderPath, "displayState.txt");
		try {
			FileUtils.writeStringToFile(saveFile, Integer.toString(index) + "\n");
			FileUtils.writeStringToFile(saveFile, mode.getClass().getName() + "\n");
		} catch (IOException e) {
			System.err.println("Error saving display state: " + saveFile.toString());
		}
	}
	
	/**
	 * loads the display state from the text file in the study folder
	 * TODO: use something more robust than a text file
	 */
	private void load(){
		File saveFile = new File(study.folderPath, "displayState.txt");
		
		if (saveFile.exists()) {
			try {
				List<String> data = FileUtils.readLines(saveFile);
				try{
					index = Integer.parseInt(data.get(0));
				} catch(NumberFormatException e){
					System.err.println("Error loading index: " + saveFile.toString());
					index = 0;
				}
				//dynamically load the right DisplayMode
				try {
					Class<?> m = Class.forName(data.get(1));
					Constructor<?> c = m.getConstructor();
					mode = (DisplayMode) c.newInstance();
				} catch (Exception e) {	
					System.err.println("Error loading display mode: " + saveFile.toString());
					mode = new OneUp();
				}
			} catch (IOException e) {
				System.err.println("Error loading display state: " + saveFile.toString());
			}
		}
	}
}
