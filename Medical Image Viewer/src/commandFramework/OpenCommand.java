package commandFramework;
import view.StudySelectorPrompt;
import controller.StudyController;





public class OpenCommand implements Command {
	private StudyController controller;

	public OpenCommand(StudyController s) {
		controller = s;
	}

	@Override
	public void execute() {
		StudySelectorPrompt s = new StudySelectorPrompt(controller);
		String result = s.showPrompt();
		if(result != null){
			controller.openStudy(result);
		}
	}

}
