import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.JPanel;


public class StudyController {
	static Preferences prefs;
	static String nodeName = "MedicalImageViewer";
	ArrayList<Study> studyList;
	DisplayState curState;
	
	public StudyController(){
		prefs = Preferences.userRoot().node(nodeName);
		String savedStudyPath = prefs.get("SAVED_STUDY_PATH", null);
		if(savedStudyPath == null){
			//ask user which study to open at runtime
		}
		Study curStudy = new Study(new File(savedStudyPath));
		curState = new DisplayState(curStudy);
	}
	
	public JPanel generatePanel(){
		return curState.generatePanel();
	}
	
	public ArrayList<Study> getStudies(){
		return studyList;
	}
	
	public DisplayMode getCurrentMode(){
		return curState.mode;
	}
}
