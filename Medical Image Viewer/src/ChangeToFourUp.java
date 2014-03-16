
public class ChangeToFourUp implements Command, Undoable{
	DisplayState dState;
	DisplayStrategy prevStrat;
	
	public ChangeToFourUp(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	
	/**
	 * Executes setMode(DisplayMode d) with a new FourUp display mode
	 */
	@Override
	public void execute() {
		prevStrat = dState.strategy;
		dState.setStrategy(new FourUp());
	}

	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
