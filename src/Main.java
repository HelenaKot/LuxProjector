import java.awt.*;
import java.awt.event.*;

import javax.swing.BoxLayout;

import mtPack.gameServer.NM2016Games;
import mtPack.gameServer.NM2016_MemoryGame;
import mtPack.houghCalibration.preCalibrationMain;
import mtPack.luksfera.*;

public
	class Main
	extends Frame{
	
	public static void main(String[] args){
		new Main();
	}
	
	public Main(){
		super("Control window");
		
		addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent evt){
					System.exit(0);
				}
			}
		);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Button preCalibrationButton = new Button("preCalibration");
		preCalibrationButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					new preCalibrationMain();
				}
			}
		);
		add(preCalibrationButton);
		
		Button calibrateButton = new Button("Kalibracja");
		calibrateButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					new LuxsferDisplayCalibration( 9, 15);
				}
			}
		);
		add(calibrateButton);
		
		Button projectorButton = new Button("Projector");
		projectorButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					System.out.println("Run projector");
				}
			}
		);
		add(projectorButton);
		
		Button creatorButton = new Button("Creator");
		creatorButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					System.out.println("Run Creator");
					new mtPack.creator.Creator( 9, 15);
				}
			}
		);
		add(creatorButton);
		
		Button gameButton = new Button("Game");
		gameButton.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					System.out.println("Run game");
					//new mtPack.gameServer.NM2016Games( 8, 14, 5, 8, 2, 5).show();
                    new GameLaunech(9, 15, 8, 7, 1, 6);
				}
			}
		);
		add(gameButton);
		
		setSize( 300, 400);
		setVisible(true);
	}

}