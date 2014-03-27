package commandFramework;
import model.DisplayState;





public class PrevCommand implements Command{
	private DisplayState dState;
	
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
