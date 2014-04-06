package model;
import java.io.File;
import java.io.FileFilter;
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
	public void copyWithSubStudies(File destFolder){
		try {
			FileUtils.copyDirectory(folderPath, destFolder);
		} catch (IOException e) {
			System.err.println("Error copying study: " + folderPath + " to " + destFolder.toString());
		}
	}
	
	public void copyWithoutSubStudies(File destFolder){
		try {
			FileUtils.copyDirectory(folderPath, destFolder, new FileFilter(){

				@Override
				public boolean accept(File f) {
					return !f.isDirectory();
				}
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * Doesn't include empty (organizational) studies
	 * 
	 * @param name String representation of the study to find
	 * @return Study matching the given name
	 */
	public Study findStudy(String name){
		if(this.toString().equals(name) && this.hasImages()){
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
	
	public boolean hasImages() {
		return this.images.size() > 0;
	}

	/**
	 * recursively constructs a list of all the studies contained in this root study
	 * only includes studies if they contain images
	 * doesn't include empty (organizational) studies
	 * 
	 * @return ArrayList<Study>
	 */
	public ArrayList<Study> getStudies(){
		ArrayList<Study> result = new ArrayList<Study>();
		if(this.hasImages()){
			result.add(this);
		}
		for(Study s: substudies){
			result.addAll(s.getStudies());
		}
		return result;
	}

	public File getFolderPath() {
		return folderPath;
	}
}
