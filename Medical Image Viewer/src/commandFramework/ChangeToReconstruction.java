package commandFramework;

import model.DisplayState;
import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.ReconstructionStrategy;

public class ChangeToReconstruction implements UndoableCommand {
	private DisplayState dState;
	private DisplayStrategy prevStrat;
	
	public ChangeToReconstruction(DisplayState ds){
		dState = ds;
	}
	
	@Override
	public void execute() {
		prevStrat = dState.getCurStrategy();
		dState.setStrategy(new ReconstructionStrategy());
	}

	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
