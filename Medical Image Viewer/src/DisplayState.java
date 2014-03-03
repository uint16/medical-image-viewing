import java.util.Observable;

import javax.swing.JFrame;


public class DisplayState extends Observable{
	int index;
	DisplayMode mode;
	Study study;
	
	public DisplayState(Study s){
		index = 0;
		mode = new OneUp();
		study = s;
	}
	
	public JFrame generateFrame(){
		JFrame result = new JFrame();
		result.setLayout(mode.getLayout());
		for(int i : mode.getIndices(index)){
			result.add(new ImagePanel(study.getImage(i)));
		}
		return result;
	}
}
