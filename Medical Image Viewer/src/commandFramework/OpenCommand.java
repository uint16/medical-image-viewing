package commandFramework;

import controller.StudyController;

public class OpenCommand implements Command {
	private StudyController controller;
	private String studyName;

	public OpenCommand(StudyController c, String s) {
		controller = c;
		studyName = s;
	}

	@Override
	public void execute() {
		controller.openStudy(studyName);
	}

}
