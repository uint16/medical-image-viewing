import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;

enum ACCEPTABLE_FILE_EXT {JPG, JPEG};

public class Study {
	ArrayList<MedicalImage> images;
	File folderPath;
	
	public Study(File f){
		folderPath = f;
		images = loadImages(folderPath);
	}

	/**
	 * Iterates over the files in given folder and creates a MedicalImage for each file matching the acceptable extensions
	 * 
	 * @param folder File object pointing to the folder containing the images to load
	 * @return ArrayList of MedicalImages
	 */
	private ArrayList<MedicalImage> loadImages(File folder) {
		ArrayList<MedicalImage> result = new ArrayList<MedicalImage>();
		for(File f: folder.listFiles(validExtensionFilter())){
			result.add(new MedicalImage(f));
		}
		return result;
	}
	
	/**
	 * returns a FilenameFilter which only accepts files of the extension types listed in the enum ACCEPTABLE_FILE_EXT
	 * This should probably be generated once and then passed by reference
	 * 
	 * @return FilenameFilter accepting enumerated file extensions
	 */
	private FilenameFilter validExtensionFilter(){
		FilenameFilter result = new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name){
				String ext = FilenameUtils.getExtension(name);
				for(ACCEPTABLE_FILE_EXT a : ACCEPTABLE_FILE_EXT.values()){
					if(a.name().equalsIgnoreCase(ext)){
						return true;
					}
				}
				return false;
			}
		};
		return result;
	}
}
