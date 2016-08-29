package mtPack.autoVideoCalibration;

import java.awt.*;
import java.awt.event.*;

public 
	class AutoCalibrationWindow extends Frame {

	public AutoCalibrationWindow(){
		
		setSize( 640, 480);
		setVisible(true);
		
		addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent evt){
					System.exit(0);
				}
			}
		);
	}
	
	public void paint(Graphics g){
		
	}
}
