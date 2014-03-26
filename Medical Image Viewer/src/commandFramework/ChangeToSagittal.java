package commandFramework;

import model.DisplayState;
import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.SagittalReconstructionStrategy;

public class ChangeToSagittal implements UndoableCommand {
	private DisplayState dState;
	private DisplayStrategy prevStrat;
	
	public ChangeToSagittal(DisplayState ds){
		dState = ds;
	}

	@Override
	public void execute() {
		prevStrat = dState.curStrategy;
		dState.setStrategy(new SagittalReconstructionStrategy());
	}
	
	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}
}
