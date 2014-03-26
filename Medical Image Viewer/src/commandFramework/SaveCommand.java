package commandFramework;




import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import model.DisplayState;


public class SaveCommand implements Command {
	private DisplayState dState;
	
	public SaveCommand(DisplayState newDisplayState){
		dState = newDisplayState;
	}
	
	@Override
	public void execute() {
		try{
			FileOutputStream fileOut = new FileOutputStream(dState.study.getSaveFile());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dState);
			out.close();
			fileOut.close();
			dState.saved = true;
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
