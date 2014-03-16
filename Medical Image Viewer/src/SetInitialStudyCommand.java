import java.util.prefs.Preferences;


public class SetInitialStudyCommand implements Command, Undoable {
	private StudyController controller;
	private String prevInitialStudy;
	
	public SetInitialStudyCommand(StudyController c){
		controller = c;
	}

	/**
	 * Asks the user to select a new initial study. 
	 * Gets the current initial study (for undoing later)
	 * Sets the initial study to the selected new study
	 */
	@Override
	public void execute() {
		StudySelectorPrompt s = new StudySelectorPrompt(controller);
		String result = s.showStudySelector();
		if(result != null){
			Preferences prefs = Preferences.userRoot().node(controller.NODE_NAME);
			prevInitialStudy = prefs.get(controller.INITIAL_STUDY_KEY, null);
			prefs.put(controller.INITIAL_STUDY_KEY, result);
		}
	}

	@Override
	public void undo() {
		Preferences prefs = Preferences.userRoot().node(controller.NODE_NAME);
		prefs.put(controller.INITIAL_STUDY_KEY, prevInitialStudy);
	}

}
