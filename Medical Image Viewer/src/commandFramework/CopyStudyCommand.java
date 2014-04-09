package commandFramework;

import view.CopyStudyPrompt;
import controller.StudyController;

public class CopyStudyCommand implements Command {
	private StudyController controller;
	
	public CopyStudyCommand(StudyController c){
		controller = c;
	}

	@Override
	public void execute() {
		CopyStudyPrompt csp = new CopyStudyPrompt();
		String newName = csp.showCopyStudyPrompt();
		Boolean subs = csp.getSubs();
		
		if(newName != null){
			controller.saveStudyAs(newName, subs);
		}
	}

}
