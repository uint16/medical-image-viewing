package commandFramework;
import controller.StudyController;
import view.StudySelectorPrompt;





public class OpenCommand implements Command {

	private StudyController controller;

	public OpenCommand(StudyController s) {
		controller = s;
	}

	@Override
	public void execute() {
		StudySelectorPrompt s = new StudySelectorPrompt(controller);
		String result = s.showStudySelector();
		if(result != null){
			controller.openStudy(result);
		}
	}

}
