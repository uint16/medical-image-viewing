package controller;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

import model.DisplayState;
import model.Study;

import displayStrategyFramework.DisplayStrategy;

import view.RootDirPrompt;
import view.StudySelectorPrompt;
import view.UnsavedStatePrompt;

/**
 * Main controller class Keeps track of the root study and the
 * current state/study
 * 
 * @author Ethan Davidson (emd1771)
 * 
 */
public class StudyController extends Observable implements Observer {
	public final String NODE_NAME = "MedicalImageViewer";
	public final String INITIAL_STUDY_KEY = "INITIAL_STUDY";
	public final String ROOT_STUDY_KEY = "ROOT_STUDY";
	
	private static Preferences prefs;
	private File rootDir;
	public Study rootStudy;
	public DisplayState curState;

	public StudyController() {
		prefs = Preferences.userRoot().node(NODE_NAME);
		// If no root dir is saved, ask user where their studies are stored
		String rootDirPath = prefs.get(ROOT_STUDY_KEY, null);
		if (rootDirPath == null) {
			new RootDirPrompt(this);
		} else {
			rootDir = new File(rootDirPath);
		}
		// load the studies from the root directory
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
		Study s = rootStudy.findStudy(savedStudy);
		if(s != null){
			openStudy(s);
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
	 * load all the studies (directories) from the rootDir
	 */
	private void loadStudies() {
		rootStudy = new Study(rootDir);

		this.setChanged();
		this.notifyObservers();
	}

	public JPanel generatePanel() {
		return curState.generatePanel();
	}

	public DisplayStrategy getCurrentMode() {
		return curState.getCurStrategy();
	}

	public void setRootDir(File f) {
		prefs.put(ROOT_STUDY_KEY, f.toString());
		rootDir = f;
	}

	/**
	 * copies the current study into a new folder with the given name
	 * 
	 * @param s Name to save the study as
	 */
	public void saveStudyAs(String s) {
		File newFolder = new File(rootDir.toString() + "/" + s);
		if (!newFolder.exists()) {
			newFolder.mkdir();
		}
		if (newFolder.isDirectory()) {
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			curState.getStudy().copyTo(newFolder);
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
