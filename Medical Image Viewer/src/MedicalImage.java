import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.io.FilenameUtils;

/**
 * Class representing a medical image in a study It proxies the actual image
 * object so that it only gets loaded once
 * 
 * @author Ethan Davidson (emd1771)
 * 
 */
public class MedicalImage {
	private File imgFile;
	private BufferedImage img;
	private static BufferedImage errImg;

	public MedicalImage(File f) {
		imgFile = f;
		img = null;

		// errImg is static, so it is only loaded by the first image
		// instantiated
		if (errImg == null) {
			try {

				errImg = ImageIO.read(this.getClass().getResource("/img/errorImage.jpg"));
			} catch (IOException e) {
				System.err.println("Error image unable to be read!");
			}
		}
	}

	/**
	 * Loads the image (if it hasn't done so already) and returns it If the
	 * image is unable to be loaded, it is pointed to the error image
	 * 
	 * @return BufferedImage the image represented by this proxy class
	 */
	public BufferedImage showImage() {
		if (img == null) {
			img = loadImage();
		}
		return img;
	}

	/**
	 * Loads the medical image
	 * If image type is ACR, use special loading
	 * Otherwise, use java builtin image loading
	 * @return BufferedImage loaded from this MedicalImage's file
	 */
	private BufferedImage loadImage() {
		BufferedImage result = null;
		if (FilenameUtils.isExtension(imgFile.toString(), "acr")) {
			final int HEADER_OFFSET = 0x2000;
			FileImageInputStream imageFile = null;
			try {
				imageFile = new FileImageInputStream(imgFile);
				imageFile.seek(HEADER_OFFSET);
			} catch (FileNotFoundException e) {
				System.err.print("Error opening file: ");
				System.err.println(e.getMessage());
				result = errImg;
			} catch (IOException e) {
				System.err.print("IO error on file: ");
				System.err.println(e.getMessage());
				result = errImg;
			}

			int sliceWidth = 256;
			int sliceHeight = 256;

			result = new BufferedImage(sliceWidth, sliceHeight, BufferedImage.TYPE_USHORT_GRAY);

			for (int i = 0; i < result.getHeight(); i++) {
				for (int j = 0; j < result.getWidth(); j++) {

					int pixelHigh = 0;
					int pixelLow = 0;
					int pixel;

					try {
						pixelHigh = imageFile.read();
						pixelLow = imageFile.read();
						pixel = pixelHigh << 4 | pixelLow >> 4;

						result.setRGB(j, i, pixel << 16 | pixel << 8 | pixel);

					} catch (IOException e) {
						System.err.print("IO error readin byte: ");
						System.err.println(e.getMessage());
						result = errImg;
					}
				}
			}
		} else {
			try {
				result = ImageIO.read(imgFile);
			} catch (IOException e) {
				System.err.println("Error loading image: " + imgFile.toString());
				result = errImg;
			}
		}
		if(result == null){
			result = errImg;
		}
		return result;
	}
}
