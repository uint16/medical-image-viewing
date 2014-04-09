package commandFramework;

import model.DisplayState;
import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.OneUpStrategy;

public class ChangeToOneUp implements UndoableCommand{
	private DisplayState dState;
	private DisplayStrategy prevStrat;
	
	public ChangeToOneUp(DisplayState ds){
		dState = ds;
	}
	
	/**
	 * Executes setMode(DisplayState d) with a new OneUp display mode
	 */
	@Override
	public void execute() {
		prevStrat = dState.getCurStrategy();
		dState.setStrategy(new OneUpStrategy());
	}
	
	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
