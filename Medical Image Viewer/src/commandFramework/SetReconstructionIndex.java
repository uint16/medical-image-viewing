package commandFramework;

import java.awt.Point;

import model.DisplayState;



public class SetReconstructionIndex implements UndoableCommand {
	private DisplayState state;
	private Point prevIndex;
	private Point newIndex;
	
	public SetReconstructionIndex(DisplayState ds, Point p){
		state = ds;
		newIndex = p;
	}


	@Override
	public void execute() {
		prevIndex = state.curStrategy.getReconstructionIndex();
		state.setReconstructionIndex(newIndex);
	}
	
	@Override
	public void undo() {
		state.setReconstructionIndex(prevIndex);
	}
}
