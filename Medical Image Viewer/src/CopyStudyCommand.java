
public class CopyStudyCommand implements Command {
	StudyController controller;
	
	public CopyStudyCommand(StudyController c){
		controller = c;
	}

	@Override
	public void execute() {
		CopyStudyPrompt csp = new CopyStudyPrompt();
		String newName = csp.showCopyStudyPrompt();
		if(newName != null){
			controller.saveStudyAs(newName);
		}
	}

}
