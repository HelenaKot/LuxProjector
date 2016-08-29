package mtPack.houghCalibration;

import java.awt.image.BufferedImage;

import java.awt.*;
import javax.swing.*;

public 
	class ImagePanel
	extends JPanel{
	
	BufferedImage img;
	
	public ImagePanel(BufferedImage img){
		this.img = img;
		this.setPreferredSize(new Dimension( 320, 240));
	}
	
	public void setImage(BufferedImage img){
		this.img = img;
		this.repaint();
	}
	
	public void paintComponent(Graphics g){
		if(img != null)
			g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
