package commandFramework;

import model.DisplayState;

public class SaveCommand implements Command {
	private DisplayState dState;
	
	public SaveCommand(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	
	@Override
	public void execute() {
		dState.save();
	}

}
