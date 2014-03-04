
public class ChangeToFourUp implements Command{
	
	DisplayState dState;
	public ChangeToFourUp(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	@Override
	public void execute() {
		dState.ChangeToFourUp();
		
	}

}
