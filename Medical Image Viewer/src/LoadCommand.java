
public class LoadCommand implements Command{

	DisplayState dState;
	public LoadCommand(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	@Override
	public void execute() {
		dState.load();
	}

}
