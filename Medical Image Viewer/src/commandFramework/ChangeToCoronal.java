package commandFramework;
import model.DisplayState;
import displayStrategyFramework.CoronalReconstructionStrategy;
import displayStrategyFramework.DisplayStrategy;


public class ChangeToCoronal implements UndoableCommand {
	private DisplayState dState;
	private DisplayStrategy prevStrat;
	
	public ChangeToCoronal(DisplayState ds){
		dState = ds;
	}

	@Override
	public void execute() {
		prevStrat = dState.curStrategy;
		dState.setStrategy(new CoronalReconstructionStrategy());
	}

	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
