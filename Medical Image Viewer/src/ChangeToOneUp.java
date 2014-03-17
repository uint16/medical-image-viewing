
public class ChangeToOneUp implements Command, Undoable{
	DisplayState dState;
	DisplayStrategy prevStrat;
	
	public ChangeToOneUp(DisplayState ds){
		dState = ds;
	}
	
	/**
	 * Executes setMode(DisplayState d) with a new OneUp display mode
	 */
	@Override
	public void execute() {
		prevStrat = dState.strategy;
		dState.setStrategy(new OneUpStrategy());
	}
	
	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
