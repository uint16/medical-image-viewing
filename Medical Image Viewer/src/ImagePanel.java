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
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, null); // see javadoc for more info on the parameters
	}
}
