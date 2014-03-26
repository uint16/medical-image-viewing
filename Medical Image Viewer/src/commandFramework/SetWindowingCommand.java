package commandFramework;

import model.DisplayState;

public class SetWindowingCommand implements UndoableCommand {
	private DisplayState state;
	private int highCutoff, lowCutoff, prevHigh, prevLow;
	
	public SetWindowingCommand(DisplayState ds, int low, int high){
		state = ds;
		highCutoff = high;
		lowCutoff = low;
		prevHigh = ds.getHighCutoff();
		prevLow = ds.getLowCutoff();
	}

	@Override
	public void execute() {
		state.setWindow(lowCutoff, highCutoff);
	}

	@Override
	public void undo() {
		state.setWindow(prevLow, prevHigh);
	}

}
