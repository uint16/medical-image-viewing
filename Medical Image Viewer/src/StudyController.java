import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

/**
 * Main controller class Keeps track of the studies, the homeDir, and the
 * current state/study
 * 
 * @author Ethan Davidson (emd1771)
 * 
 */
public class StudyController extends Observable implements Observer {
	static Preferences prefs;
	static String nodeName = "MedicalImageViewer";
	File homeDir;
	ArrayList<Study> studyList;
	DisplayState curState;

	public StudyController() {
		studyList = new ArrayList<Study>();
		prefs = Preferences.userRoot().node(nodeName);
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
		String savedStudy = prefs.get("INITIAL_STUDY", null);
		if (savedStudy != null) {
			openStudy(savedStudy);
		} else { // otherwise, ask them which study to open
			StudySelectorPrompt s = new StudySelectorPrompt(this);
			String result = s.showStudySelector();
			openStudy(result);
		}
		if (curState == null) { // curState is null if they don't select a study
								// or specify an initial
			System.exit(0);
		}
	}

	/**
	 * Given a string representing the folder name of a study, open that study
	 * i.e. create the displayState for that study and set it to current TODO:
	 * what do if it doesn't find the study matching the string?
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
			new UnsavedStatePrompt(curState);
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

	public DisplayMode getCurrentMode() {
		return curState.mode;
	}

	public void setHomeDir(File f) {
		prefs.put("HOME_DIR", f.toString());
		homeDir = f;
	}

	/**
	 * Saves the given string representing a study name as the study to open
	 * when the program loads
	 * 
	 * @param s
	 */
	public void setInitialStudy(String s) {
		prefs.put("INITIAL_STUDY", s);
	}

	/**
	 * copies the current study into a new folder with the given name
	 * 
	 * @param s
	 *            Name to save the study as
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
