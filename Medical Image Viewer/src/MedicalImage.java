import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class representing a medical image in a study
 * It proxies the actual image object so that it only gets loaded once
 * @author Ethan Davidson (emd1771)
 *
 */
public class MedicalImage{
	private File imgFile;
	private BufferedImage img;
	private static BufferedImage errImg;

	public MedicalImage(File f){
		imgFile = f;
		img = null;
		
		//errImg is static, so it is only loaded by the first image instantiated
		if(errImg == null){
			try{
				errImg = ImageIO.read(MedicalImage.class.getResource("/img/errorImage.jpg"));
			} catch(IOException e){
				System.err.println("Error image unable to be read!");
			}
		}
	}
	
	/**
	 * Loads the image (if it hasn't done so already) and returns it
	 * If the image is unable to be loaded, it is pointed to the error image
	 * 
	 * @return BufferedImage the image represented by this proxy class
	 */
	public Image showImage(){
		if(img == null){
			try{
				img = ImageIO.read(imgFile);
			} catch(IOException e){
				img = errImg;
			}
		}
		return img;
	}
}
