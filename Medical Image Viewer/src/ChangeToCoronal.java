
public class ChangeToCoronal implements Command, Undoable {
	DisplayState dState;
	DisplayStrategy prevStrat;
	
	public ChangeToCoronal(DisplayState ds){
		dState = ds;
	}

	@Override
	public void execute() {
		prevStrat = dState.strategy;
		dState.setStrategy(new CoronalReconstructionStrategy());
	}

	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
