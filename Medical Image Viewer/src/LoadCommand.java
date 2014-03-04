
public class LoadCommand implements Command{

	DisplayState dState;
	public LoadCommand(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	/**
	 * Exxecutes DisplayState.load()
	 * 
	 */
	@Override
	public void execute() {
		//dState.load();
	}

}
