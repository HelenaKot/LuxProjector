package mtPack.luksfera;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import mtPack.luksfera.event.*;

public
	class LuxsferDisplay
	extends LuxEventsLayer{
	
	// functionality
	private static LuxsferDisplay luxDisplay;	
	
	public static LuxsferDisplay make(int x, int y){
		if(luxDisplay == null)
			luxDisplay = new LuxsferDisplay( x, y);
		return luxDisplay;
	}	
	
	// logic
	private int luxsferDistortionBuffer[][][];
	public Color luxsferColorBuffer[][];
	
	private LuxsferDisplay(int x, int y){
		addKeyListener(
			new KeyAdapter() {
				public void keyPressed(KeyEvent evt){
					switch(evt.getKeyChar()){
						case 'q':
							//System.out.println(">q< pressed");
							LuxsferDisplay.this.setVisible(false);
							LuxsferDisplay.this.dispose();
							break;
							
						case 'f':
							//System.out.println(">f< pressed");
							System.out.println(
									LuxsferDisplay.this.getGraphicsConfiguration().getDevice().getIDstring()
							);
							/*
							setVisible(false);
							setUndecorated(true);
							setExtendedState(Frame.MAXIMIZED_BOTH);
							setVisible(true);
							*/
							LuxsferDisplay.this.getGraphicsConfiguration().getDevice().setFullScreenWindow(LuxsferDisplay.this);
							/*
							GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1];
							device.setFullScreenWindow(this);
							*/
							/*
							for (Window w : Window.getWindows()) {
								GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(w);
							}
							*/
							break;
							
						case 'l':
							String lstr = LuxsferDisplay.this.getGraphicsConfiguration().getDevice().getIDstring();
							lstr = lstr.replace(' ', '_');
							File lf = new File(lstr);
							System.out.println(lf);
							try {
					            FileInputStream fis = new FileInputStream(lf);
					            ObjectInputStream ois = new ObjectInputStream(fis);
					            luxsferDistortionBuffer = (int[][][]) ois.readObject();
					            ois.close();
					            LuxsferDisplay.this.repaint();
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
					}	
				}
			}
		);
		addLuxsferDisplayListener(
			new LuxsferDisplayListener(){
				public void luxsfereChanged(LuxsferDisplayEvent evt) {
					// przyszła zmiana

					LuxsferDisplay.this.repaint();
				}
			}
		);
		
		try{
			FileInputStream fis = new FileInputStream("D:/LuxProjector/src/plansza0.obj");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = ois.readObject();
			luxsferColorBuffer = (Color[][])obj;
		}catch(Exception ex){
			System.out.println(ex);
		}
		
        setSize(640, 480);
        setVisible(true);
        /*
        new Thread(){
        	public void run(){
        		
        	}
        };
        */
	}
	
    public void paint(Graphics g){
    	if(luxsferDistortionBuffer != null && luxsferColorBuffer != null){
	        int[][] tab = new int[2][4];
	        for(int i=0; i<luxsferDistortionBuffer.length-1; i++){
	            for(int j=0; j<luxsferDistortionBuffer[i].length-1; j++){
	
	                tab[0][0] = luxsferDistortionBuffer[i][j][0];
	                tab[1][0] = luxsferDistortionBuffer[i][j][1];
	                tab[0][1] = luxsferDistortionBuffer[i+1][j][0];
	                tab[1][1] = luxsferDistortionBuffer[i+1][j][1];
	                tab[0][2] = luxsferDistortionBuffer[i+1][j+1][0];
	                tab[1][2] = luxsferDistortionBuffer[i+1][j+1][1];
	                tab[0][3] = luxsferDistortionBuffer[i][j+1][0];
	                tab[1][3] = luxsferDistortionBuffer[i][j+1][1];

	                g.setColor(luxsferColorBuffer[i][j]);
	                g.fillPolygon(tab[0], tab[1], 4);
	            }
	        }
    	}
    }
}