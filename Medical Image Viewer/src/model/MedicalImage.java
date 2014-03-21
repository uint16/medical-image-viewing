package model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import org.apache.commons.io.FilenameUtils;

/**
 * Class representing a medical image in a study 
 * It is constructed with the filepath so it can use lazy loading
 * Also is lazy in creating the windowed version of the image
 * 
 * @author Ethan Davidson (emd1771)
 * 
 */
public class MedicalImage extends Image{
	private File imgFile;
	private BufferedImage img;
	private BufferedImage windowImg;
	private int lowCutoff, highCutoff;
	private static BufferedImage errImg;

	public MedicalImage(File f) {
		imgFile = f;
		img = null;
		windowImg = null;
		lowCutoff = 0;
		highCutoff = 255;

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
	
	public int getWidth(){
		return getImage().getWidth();
	}
	
	public int getHeight(){
		return getImage().getHeight();
	}

	/**
	 * Loads the image (if it hasn't done so already)
	 * Windows the image (if it hasn't done so already)
	 * returns the windowed image
	 * 
	 * @return BufferedImage the image represented by this proxy class
	 */
	public BufferedImage getImage() {
		if (img == null) {
			img = loadImage();
		}
		if(windowImg == null){
			windowImg = window(lowCutoff, highCutoff);
		}
		return windowImg;
	}
	
	public void setWindow(int low, int high){
		windowImg = null;
		lowCutoff = low;
		highCutoff = high;
	}
	
	private BufferedImage window(int low, int high){
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		result.setData(img.getData());
		for(int y = 0; y < result.getHeight(); y++){
			for(int x = 0; x < result.getWidth(); x++){
				int rgb = result.getRGB(x, y);
				Color c = new Color(rgb);
				Color r;
				if(c.getRed() == c.getBlue() && c.getBlue() == c.getGreen()){	//only window greyscale
					int intensity = c.getRed();
					if(intensity < low){
						r = new Color(0, 0, 0);
					} else if (intensity > high){
						r = new Color(255, 255, 255);
					} else {	//scale intensity
						intensity = (255 * (intensity-low)) / (high - low);
						r = new Color(intensity, intensity, intensity);
					}
					result.setRGB(x, y, r.getRGB());
				}
			}
		}
		return result;
	}

	/**
	 * Loads the medical image
	 * If image type is ACR, use special loading
	 * Otherwise, use java builtin image loading
	 * If the image is unable to be loaded, points to the error image
	 * 
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

	public int getRGB(int i, int j) {
		return getImage().getRGB(i, j);
	}
	
	public int getType(){
		return getImage().getType();
	}

	public Graphics2D createGraphics() {
		return getImage().createGraphics();
	}

	@Override
	public Graphics getGraphics() {
		return getImage().getGraphics();
	}

	@Override
	public int getHeight(ImageObserver observer) {
		return getImage().getHeight(observer);
	}

	@Override
	public Object getProperty(String name, ImageObserver observer) {
		return getImage().getProperty(name, observer);
	}

	@Override
	public ImageProducer getSource() {
		return getImage().getSource();
	}

	@Override
	public int getWidth(ImageObserver observer) {
		return getImage().getWidth(observer);
	}

	public Image getSubImage(int x, int y, int w, int h) {
		return getImage().getSubimage(x, y, w, h);
	}
	
	public BufferedImage getImageCopy(){
		BufferedImage i = getImage();
		BufferedImage result = new BufferedImage(i.getWidth(), i.getHeight(), i.getType());
		result.setData(i.getData());
		return result;
	}
}
