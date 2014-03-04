
public class ChangeToOneUp implements Command{
	
	DisplayState dState;
	public ChangeToOneUp(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	@Override
	public void execute() {
		dState.ChangeToOneUp();
		
	}

}
