import java.io.File;

public class OpenCommand implements Command {

	private StudyController controller;
	File filePath;

	public OpenCommand(StudyController s) {
		controller = s;
	}

	@Override
	public void execute() {
		StudySelectorPrompt s = new StudySelectorPrompt(controller);
		String result = s.showStudySelector();
		controller.openStudy(result);
	}

}
