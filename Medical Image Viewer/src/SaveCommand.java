
public class SaveCommand implements Command {
	
	DisplayState dState;
	public SaveCommand(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	@Override
	public void execute() {
		dState.save();
		
	}

}
