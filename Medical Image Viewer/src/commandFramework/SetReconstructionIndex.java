package commandFramework;

import java.awt.Point;

import model.DisplayState;



public class SetReconstructionIndex implements Command, Undoable {
	private DisplayState state;
	private Point prevIndex;
	private Point newIndex;
	
	public SetReconstructionIndex(DisplayState ds, Point p){
		state = ds;
		newIndex = p;
	}


	@Override
	public void execute() {
		prevIndex = state.strategy.getReconstructionIndex();
		state.setReconstructionIndex(newIndex);
	}
	
	@Override
	public void undo() {
		state.setReconstructionIndex(prevIndex);
	}
}
