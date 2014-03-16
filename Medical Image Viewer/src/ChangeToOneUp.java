
public class ChangeToOneUp implements Command, Undoable{
	DisplayState dState;
	DisplayStrategy prevStrat;
	
	public ChangeToOneUp(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	
	/**
	 * Executes setMode(DisplayState d) with a new OneUp display mode
	 */
	@Override
	public void execute() {
		prevStrat = dState.strategy;
		dState.setStrategy(new OneUp());
	}
	
	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
