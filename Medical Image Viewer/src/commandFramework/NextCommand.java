package commandFramework;
import model.DisplayState;




public class NextCommand implements Command {
	private DisplayState dState;
	
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
