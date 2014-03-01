import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class MedicalImage{
	private File imgFile;
	private BufferedImage img;

	public MedicalImage(File f){
		imgFile = f;
		img = null;
	}
	
	public Image showImage(){
		if(img != null){
			return img;
		} else {
			try{
				img = ImageIO.read(imgFile);
			} catch(Exception e){
				img = errorImage;
			}
			return img;
		}
	}
}
