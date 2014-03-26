package commandFramework;




import java.util.prefs.Preferences;

import controller.StudyController;


public class SetInitialStudyCommand implements UndoableCommand {
	private StudyController controller;
	private String newInitialStudy;
	private String prevInitialStudy;
	
	public SetInitialStudyCommand(StudyController c, String newInitStudy){
		controller = c;
		newInitialStudy = newInitStudy;
	}

	/**
	 * Gets the current initial study (for undoing later)
	 * Sets the initial study to the selected new study
	 */
	@Override
	public void execute() {
		Preferences prefs = Preferences.userRoot().node(controller.NODE_NAME);
		prevInitialStudy = prefs.get(controller.INITIAL_STUDY_KEY, null);
		prefs.put(controller.INITIAL_STUDY_KEY, newInitialStudy);
	}

	@Override
	public void undo() {
		Preferences prefs = Preferences.userRoot().node(controller.NODE_NAME);
		prefs.put(controller.INITIAL_STUDY_KEY, prevInitialStudy);
	}

}
