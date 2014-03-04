
public class ChangeToFourUp implements Command{
	
	DisplayState dState;
	public ChangeToFourUp(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	/**
	 * Executes setMode(DisplayMode d) with a new FourUp display mode
	 */
	@Override
	public void execute() {
		dState.setMode(new FourUp());
		
	}

}
