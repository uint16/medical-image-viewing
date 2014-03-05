
public class SetInitialStudyCommand implements Command {
	private StudyController controller;
	
	public SetInitialStudyCommand(StudyController c){
		controller = c;
	}

	@Override
	public void execute() {
		StudySelectorPrompt s = new StudySelectorPrompt(controller);
		String result = s.showStudySelector();
		if(result != null){
			controller.setInitialStudy(result);
		}
	}

}
