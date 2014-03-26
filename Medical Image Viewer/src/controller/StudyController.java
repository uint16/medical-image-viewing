package controller;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

import model.DisplayState;
import model.Study;

import displayStrategyFramework.DisplayStrategy;

import view.HomeDirPrompt;
import view.StudySelectorPrompt;
import view.UnsavedStatePrompt;

/**
 * Main controller class Keeps track of the studies, the homeDir, and the
 * current state/study
 * 
 * @author Ethan Davidson (emd1771)
 * 
 */
public class StudyController extends Observable implements Observer {
	public final String NODE_NAME = "MedicalImageViewer";
	public final String INITIAL_STUDY_KEY = "INITIAL_STUDY";
	
	private static Preferences prefs;
	private File homeDir;
	private ArrayList<Study> studyList;
	public DisplayState curState;

	public StudyController() {
		studyList = new ArrayList<Study>();
		prefs = Preferences.userRoot().node(NODE_NAME);
		// If no home dir is saved, ask user where their studies are stored
		String homeDirPath = prefs.get("HOME_DIR", null);
		if (homeDirPath == null) {
			new HomeDirPrompt(this);
		} else {
			homeDir = new File(homeDirPath);
		}
		// load the studies from the home dir
		loadStudies();
		// if the user has saved which study to initially open, open that study
		String savedStudy = prefs.get(INITIAL_STUDY_KEY, null);
		if (savedStudy != null) {
			openStudy(savedStudy);
		} else { // otherwise, ask them which study to open
			StudySelectorPrompt s = new StudySelectorPrompt(this);
			String result = s.showPrompt();
			openStudy(result);
		}
		if (curState == null) { // curState is null if they don't select a study
								// or specify an initial
			System.exit(0);
		}
		
		this.setChanged();
	}

	/**
	 * Given a string representing the folder name of a study, open that study
	 * i.e. create the displayState for that study and set it to current 
	 * TODO: what do if it doesn't find the study matching the string?
	 * 
	 * @param savedStudy
	 */
	public void openStudy(String savedStudy) {
		for (Study s : studyList) {
			if (s.toString().equals(savedStudy)) {
				openStudy(s);
			}
		}
	}

	/**
	 * Given a reference to a study, open that study
	 * 
	 * @param s
	 */
	private void openStudy(Study s) {
		if (curState != null && !curState.saved) { // curState is null when
													// loading the first study
			new UnsavedStatePrompt(this);
		}
		curState = new DisplayState(s);
		curState.addObserver(this);
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * load all the studies (directories) from the homeDir
	 */
	private void loadStudies() {
		studyList.clear();
		for (File f : homeDir.listFiles()) {
			if (f.isDirectory()) {
				studyList.add(new Study(f));
			}
		}

		// if homeDir does not contain any subdirectories add current directory
		// as study
		if (studyList.size() == 0) {
			studyList.add(new Study(homeDir));
		}

		this.setChanged();
		this.notifyObservers();
	}

	public JPanel generatePanel() {
		return curState.generatePanel();
	}

	public ArrayList<Study> getStudies() {
		return studyList;
	}

	public DisplayStrategy getCurrentMode() {
		return curState.curStrategy;
	}

	public void setHomeDir(File f) {
		prefs.put("HOME_DIR", f.toString());
		homeDir = f;
	}

	/**
	 * copies the current study into a new folder with the given name
	 * 
	 * @param s Name to save the study as
	 */
	public void saveStudyAs(String s) {
		File newFolder = new File(homeDir.toString() + "/" + s);
		if (!newFolder.exists()) {
			newFolder.mkdir();
		}
		if (newFolder.isDirectory()) {
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			curState.study.copyTo(newFolder);
		} else {
			System.err.println("Error: can't copy study into a file: "
					+ newFolder.toString());
		}
		loadStudies();
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers();
	}
}
