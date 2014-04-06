package controller;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

import model.DisplayState;
import model.Study;
import view.ImagePanel;
import view.RootDirPrompt;
import view.StudySelectorPrompt;
import view.UnsavedStatePrompt;

import commandFramework.Invoker;
import commandFramework.NextCommand;
import commandFramework.PrevCommand;
import commandFramework.SetReconstructionIndex;

import displayStrategyFramework.DisplayStrategy;

/**
 * Main controller class Keeps track of the root study and the
 * current state/study
 * 
 * @author Ethan Davidson (emd1771)
 * 
 */
public class StudyController extends Observable implements Observer, MouseMotionListener, MouseWheelListener {
	public final String NODE_NAME = "MedicalImageViewer";
	public final String INITIAL_STUDY_KEY = "INITIAL_STUDY";
	public final String ROOT_STUDY_KEY = "ROOT_STUDY";
	
	private static Preferences prefs;
	private File rootDir;
	private Study rootStudy;
	public DisplayState curState;
	private Invoker invoker;

	public StudyController(Invoker i) {
		invoker = i;
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
	 * If the currently open study isn't saved, ask the user if they want to save it,
	 * then load the serialized display state from the file in the study folder.
	 * If the state can't be loaded, initialize a new state
	 * 
	 * @param s
	 */
	private void openStudy(Study s) {
		if (curState != null && !curState.saved) {	// curState is null when
													// loading the first study
			new UnsavedStatePrompt(this);
		}
		File saveFile = s.getSaveFile();
		boolean loadSuccess = false;
		
		if (saveFile.exists()) {
			try {
				FileInputStream fis = new FileInputStream(saveFile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				curState = (DisplayState) ois.readObject();
				curState.setStudy(s);
				ois.close();
				fis.close();
				loadSuccess = true;
			} catch (IOException e) {
				System.err.println("Error loading display state: " + saveFile.toString());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		if(loadSuccess){	//if we loaded successfully, we are starting in a saved state
			curState.saved = true;
		} else {
			curState = new DisplayState(s);
		}
		
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
	public void saveStudyAs(String s, Boolean b) {
		File newFolder = new File(curState.getStudy().getFolderPath().getParent() + "/" + s);
		if (!newFolder.exists()) {
			newFolder.mkdir();
		}
		if (newFolder.isDirectory()) {
			if (!newFolder.exists()) {
				newFolder.mkdir();
			}
			Study temp = curState.getStudy();
			if(b){
				temp.copyWithSubStudies(newFolder);
			} else {				
				temp.copyWithoutSubStudies(newFolder);
				
			}
			
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

	public File getHomeDir() {
		return rootDir;
	}
	
	/**
	 * returns a list of all the studies inside the root study
	 * doesn't include the root study (because it isn't a real study)
	 * 
	 * @return ArrayList<Study>
	 */
	public ArrayList<Study> getStudies(){
		return rootStudy.getStudies();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		ImagePanel studyPanel = curState.getCurStrategy().getStudyPanel();
		Point clicked = e.getPoint();
		if(studyPanel.contains(clicked)){
			//get coordinates relative to the panel
			Point p = new Point(clicked.x - studyPanel.getX(), clicked.y - studyPanel.getY());
			int scaledX = (p.x*studyPanel.getImageWidth())/studyPanel.getDisplayedDimensions().width;
			int scaledY = (p.y*studyPanel.getImageHeight())/studyPanel.getDisplayedDimensions().height;
			Point scaledP = new Point(scaledX, scaledY);
			if (scaledP.x < studyPanel.getImageWidth()
					&& scaledP.y < studyPanel.getImageHeight()) {
				invoker.add(new SetReconstructionIndex(curState, scaledP));
			}
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			new NextCommand(curState).execute();
		} else {
			new PrevCommand(curState).execute();
		}
	}
}
