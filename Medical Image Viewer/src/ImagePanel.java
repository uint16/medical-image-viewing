import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	private BufferedImage img;

	public ImagePanel(MedicalImage i) {
		img = i.showImage();
	}
	
	public ImagePanel(BufferedImage i){
		img = i;
	}

	@Override
	/**
	 * Overrides the paint component to paint the image across the whole panel
	 * does some maths to keep the aspect ratio the same
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int newWidth = img.getWidth();
		int newHeight = img.getHeight();
		//fit the image into the panel, keeping aspect ratio
		if(img.getWidth() > this.getWidth()){
			newWidth = this.getWidth();
			newHeight = (newWidth * img.getHeight()) / img.getWidth();
		}
		if(newHeight > this.getHeight()){
			newHeight = this.getHeight();
			newWidth = (newHeight * img.getWidth()) / img.getHeight();
		}
		g.drawImage(img, 0, 0, newWidth, newHeight, null);
	}
}
