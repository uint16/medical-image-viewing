
public class NextCommand implements Command {
	
	DisplayState dState;
	
	public NextCommand(DisplayState newDisplayState){
		dState = newDisplayState;
		
	}
	/**
	 * Executes DisplayState.next()
	 */
	@Override
	public void execute() {
		dState.next();
		
	}

}
