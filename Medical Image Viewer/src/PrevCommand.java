
public class PrevCommand implements Command{

	DisplayState dState;
	
	public PrevCommand(DisplayState newDisplayState){
		dState = newDisplayState;
		
	}
	/**
	 * Executes DisplayState.prev()
	 */
	@Override
	public void execute() {
		dState.prev();
		
	}
}
