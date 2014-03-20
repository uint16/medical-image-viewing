package commandFramework;

import model.DisplayState;
import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.SagittalReconstructionStrategy;

public class ChangeToSagittal implements Command, Undoable {
	DisplayState dState;
	DisplayStrategy prevStrat;
	
	public ChangeToSagittal(DisplayState ds){
		dState = ds;
	}

	@Override
	public void execute() {
		prevStrat = dState.strategy;
		dState.setStrategy(new SagittalReconstructionStrategy());
	}
	
	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}
}
