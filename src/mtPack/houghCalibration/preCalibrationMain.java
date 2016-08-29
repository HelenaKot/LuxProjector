package mtPack.houghCalibration;

import houghtransformation.EHTProcessStep;
import houghtransformation.HTEngine;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.*;

import javax.imageio.ImageIO;

public
	class preCalibrationMain
	extends JFrame {
	
	public static BufferedImage staticImageLoad(){
		BufferedImage defaultImageBuf = null;
        
        try {
           defaultImageBuf = ImageIO.read(new File("/Users/tomaszew2/git/LuxsferyProject/LuxProjector/room.png"));
        } catch (IOException e) {
        	System.out.println(e);
        }
        return defaultImageBuf;
	}
	
	private BufferedImage tmpImage;
	
	private ImagePanel baseIp;
	private ImagePanel grayscaleIp;
	private ImagePanel edgeDetectionIp;
	private ImagePanel edgeTresholdIp;
	private ImagePanel houghCenterIp;
	private ImagePanel overlayedIp;
	
	private JSlider houghSpaceBrightFactorSlider;
	private JSlider edgeTresholdSlider;
	private JSlider relativeMaximumSlider;

	private HTEngine htEngine;
	
	public preCalibrationMain(){
		
		htEngine = new HTEngine();
		tmpImage = staticImageLoad();
		
		baseIp = new ImagePanel(null);
		grayscaleIp = new ImagePanel(null);
		
		edgeDetectionIp = new ImagePanel(null);
		edgeTresholdIp = new ImagePanel(null);
		
		houghCenterIp = new ImagePanel(null);
		overlayedIp = new ImagePanel(null);
		
		edgeTresholdSlider = new JSlider(0, 60);
		
		edgeTresholdSlider.addChangeListener(
			new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					System.out.println("edgeSlider val: "+edgeTresholdSlider.getValue());

				}
				
			}
		);

		edgeTresholdSlider.addMouseListener(
			new MouseAdapter(){
				public void mouseReleased(MouseEvent evt){
					System.out.print("es test ");
					htEngine.setEdgeTreshold(
							edgeTresholdSlider.getValue()
					);
					process();
					System.out.println(" - done");
				}
			}
		);
		
		relativeMaximumSlider = new JSlider( 0, 100);
		relativeMaximumSlider.addMouseListener(
			new MouseAdapter(){
				public void mouseReleased(MouseEvent evt){
					System.out.print("bs test ");
					
					htEngine.setBrightnessFactor(
							relativeMaximumSlider.getValue()
					);
					htEngine.setRelativeMaximum(relativeMaximumSlider.getValue());
					process();
					System.out.println(" - done");
				}
			}
		);		
		relativeMaximumSlider.addChangeListener(
			new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					System.out.println("edgeSlider val: "+relativeMaximumSlider.getValue());

				}
				
			}
		);
		
		houghSpaceBrightFactorSlider = new JSlider( 0, 50);
		houghSpaceBrightFactorSlider.addMouseListener(
			new MouseAdapter(){
				public void mouseReleased(MouseEvent evt){
					System.out.print("bs test ");
					
					htEngine.setBrightnessFactor(
							houghSpaceBrightFactorSlider.getValue()
					);
					htEngine.setBrightnessFactor(5.0/houghSpaceBrightFactorSlider.getValue());
					process();
					System.out.println(" - done");
				}
			}
		);		
		houghSpaceBrightFactorSlider.addChangeListener(
			new ChangeListener(){
				public void stateChanged(ChangeEvent e) {
					System.out.println("houghSpaceBright val: "+(5.0/houghSpaceBrightFactorSlider.getValue()));

				}
				
			}
		);		
		

		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
		
        this.setLayout(
        	gridbag
        );
		
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
		
        gridbag.setConstraints(baseIp, c);
		this.add(baseIp);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(grayscaleIp, c);
		this.add(grayscaleIp);
		
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(edgeDetectionIp, c);
		this.add(edgeDetectionIp);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(edgeTresholdSlider, c);
		this.add(edgeTresholdSlider);
	
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(edgeTresholdIp, c);
		this.add(edgeTresholdIp);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(relativeMaximumSlider, c);
		this.add(relativeMaximumSlider);
		
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(houghCenterIp, c);
		this.add(houghCenterIp);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(houghSpaceBrightFactorSlider, c);
		this.add(houghSpaceBrightFactorSlider);		
		
		gridbag.setConstraints(overlayedIp, c);
		this.add(overlayedIp);
		

		htEngine.setSourceImage(tmpImage);
		
		
		setSize( 640, 480);
		setVisible(true);
		
		process();
	}
	
	public void process(){
		baseIp.setImage(tmpImage);
		
		grayscaleIp.setImage(
			htEngine.getHTProcessStep(EHTProcessStep.STEP_GRAYSCALE).getImage()
		);
		
		edgeDetectionIp.setImage(
			htEngine.getHTProcessStep(EHTProcessStep.STEP_EDGE_DETECTION).getImage()
		);
		
		edgeTresholdIp.setImage(
			htEngine.getHTProcessStep(EHTProcessStep.STEP_EDGE_TRESHOLD).getImage()
		);
		
		houghCenterIp.setImage(
			htEngine.getHTProcessStep(EHTProcessStep.STEP_HOUGH_SPACE_CENTER).getImage()
		);
		
		houghCenterIp.setImage(
			htEngine.getHTProcessStep(EHTProcessStep.STEP_HOUGH_SPACE_TOP).getImage()
		);
		
		overlayedIp.setImage(
			htEngine.getHTProcessStep(EHTProcessStep.STEP_ORIGINAL_LINES_OVERLAYED).getImage()
		);
		
		
	}

}
