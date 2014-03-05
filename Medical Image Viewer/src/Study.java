import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

enum ACCEPTABLE_FILE_EXT {
	JPG, JPEG
};

/**
 * Class representing a study (collection of medical images)
 * Holds a collection of images and the File representing the folder the study is in
 * 
 * @author Ethan Davidson (emd1771)
 *
 */
public class Study {
	ArrayList<MedicalImage> images;
	File folderPath;
	private static FilenameFilter validExtFilter = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			String ext = FilenameUtils.getExtension(name);
			for (ACCEPTABLE_FILE_EXT a : ACCEPTABLE_FILE_EXT.values()) {
				if (a.name().equalsIgnoreCase(ext)) {
					return true;
				}
			}
			return false;
		}
	};

	public Study(File f) {
		folderPath = f;
		images = loadImages(folderPath);
	}

	/**
	 * Iterates over the files in given folder and creates a MedicalImage for each file matching the acceptable extensions
	 * 
	 * @param folder
	 *            File object pointing to the folder containing the images to load
	 * @return ArrayList of MedicalImages
	 */
	private ArrayList<MedicalImage> loadImages(File folder) {
		ArrayList<MedicalImage> result = new ArrayList<MedicalImage>();
		for (File f : folder.listFiles(validExtFilter)) {
			result.add(new MedicalImage(f));
		}
		return result;
	}

	public int imgAmt() {
		return images.size();
	}

	public MedicalImage getImage(int i) {
		return images.get(i);
	}

	public boolean inRange(int i) {
		return i >= 0 && i < images.size();
	}
	
	public String toString(){
		return folderPath.getName();
	}
}
