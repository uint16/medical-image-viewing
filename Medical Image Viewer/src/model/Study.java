package model;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

enum ACCEPTABLE_FILE_EXT {
	JPG, JPEG, ACR
};

/**
 * Class representing a study (collection of medical images)
 * Holds a collection of images and the File representing the folder the study is in
 * 
 * @author Ethan Davidson (emd1771)
 *
 */
public class Study {
	public ArrayList<MedicalImage> images;
	public ArrayList<Study> substudies;
	private File folderPath;
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
		substudies = loadStudies(folderPath);
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
	
	/**
	 * Iterates over the files in given directory and creates a Study for each subdirectory
	 * Because this is called in the Study constructor, it recursively loads all studies below this folder
	 * 
	 * @param folder File object pointing to the folder containing the studies to load.
	 * @return ArrayList of Studies
	 */
	private ArrayList<Study> loadStudies(File folder){
		ArrayList<Study> result = new ArrayList<Study>();
		for(File f: folder.listFiles()){
			if(f.isDirectory()){
				result.add(new Study(f));
			}
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
	
	/**
	 * Uses Apache Commons FileUtils library to copy the folder containing this study to 
	 * a specified new folder
	 * @param destFolder
	 */
	public void copyTo(File destFolder){
		try {
			FileUtils.copyDirectory(folderPath, destFolder);
		} catch (IOException e) {
			System.err.println("Error copying study: " + folderPath + " to " + destFolder.toString());
		}
	}
	
	/**
	 * @return File object representing the file used to serialize this study's display state
	 */
	public File getSaveFile(){
		return new File(this.folderPath, "displayState");
	}
	
	/**
	 * Recursively searches for a study matching the given name
	 * 
	 * @param name String representation of the study to find
	 * @return Study matching the given name
	 */
	public Study findStudy(String name){
		if(this.toString().equals(name)){
			return this;
		} else {
			for(Study s: substudies){
				Study temp = s.findStudy(name);
				if(temp != null){
					return temp;
				}
			}
			return null;
		}
	}
}
