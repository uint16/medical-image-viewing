package commandFramework;
import model.DisplayState;
import displayStrategyFramework.DisplayStrategy;
import displayStrategyFramework.FourUpStrategy;


public class ChangeToFourUp implements Command, Undoable{
	DisplayState dState;
	DisplayStrategy prevStrat;
	
	public ChangeToFourUp(DisplayState ds){
		dState = ds;
	}
	
	/**
	 * Executes setMode(DisplayMode d) with a new FourUp display mode
	 */
	@Override
	public void execute() {
		prevStrat = dState.curStrategy;
		dState.setStrategy(new FourUpStrategy());
	}

	@Override
	public void undo() {
		dState.setStrategy(prevStrat);
	}

}
