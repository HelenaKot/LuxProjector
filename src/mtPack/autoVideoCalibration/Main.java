package mtPack.autoVideoCalibration;

import java.awt.image.*;
import java.io.*;

import houghtransformation.*;

import javax.imageio.ImageIO;

public
	class Main{
	
	public Main(){

	}
	
	public static void main(String[] args){
		BufferedImage defaultImageBuf = null;
        
        try {
        	
           defaultImageBuf = ImageIO.read(new File("/Users/tomaszew2/git/LuxsferyProject/LuxProjector/room.png"));
        } catch (IOException e) {
        	System.out.println(e);
        }
		
        HTEngine htEngine = new HTEngine();
        htEngine.setSourceImage(defaultImageBuf);	
        
		/*
		//new Main();
		
		Webcam webcam = Webcam.getDefault();
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}
		webcam.open();
		try {
			ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
}

