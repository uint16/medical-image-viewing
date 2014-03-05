import java.io.File;

public class OpenCommand implements Command {

	private StudyController controller;
	File filePath;

	public OpenCommand(StudyController s) {
		controller = s;
	}

	@Override
	public void execute() {
		new StudySelectorPrompt(controller);
	}

}
