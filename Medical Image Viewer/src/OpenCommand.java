import java.io.File;

import javax.swing.JFileChooser;

public class OpenCommand implements Command {

	private DisplayState displayState;
	File filePath;

	public OpenCommand(DisplayState ds) {
		displayState = ds;
	}

	@Override
	public void execute() {
		JFileChooser open = new JFileChooser();
		
		//FileNameExtensionFilter filter = new FileNameExtensionFilter(
		//        "JPG & JPEG Images", "JPG", "JPEG");
		
		//open.setFileFilter(filter);
		
		open.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		int returnVal = open.showOpenDialog(null);
		
		Study temp;
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		       temp = new Study(new File(open.getSelectedFile().getPath()));
		       displayState = new DisplayState(temp);
		    }

	}

}
