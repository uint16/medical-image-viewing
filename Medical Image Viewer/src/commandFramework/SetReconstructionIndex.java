package commandFramework;

import java.awt.Point;

import model.DisplayState;



public class SetReconstructionIndex implements Command {
	private DisplayState state;
	private Point newIndex;
	
	public SetReconstructionIndex(DisplayState ds, Point p){
		state = ds;
		newIndex = p;
	}


	@Override
	public void execute() {
		state.setReconstructionIndex(newIndex);
	}
}
