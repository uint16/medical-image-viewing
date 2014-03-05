
public class ChangeToOneUp implements Command{
	
	DisplayState dState;
	public ChangeToOneUp(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	/**
	 * Executes setMode(DisplayMode d) with a new FourUp display mode
	 */
	@Override
	public void execute() {
		dState.setMode(new OneUp());
		
	}

}
