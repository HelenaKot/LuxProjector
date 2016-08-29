package mtPack.luksfera;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public
	class LuxsferDisplayCalibration
	extends Frame{
	
	private int[][][] sqTab;
	private Color[][] coTab;
	
	
    private boolean selected = false,
    				over = false;
    private boolean shift = false;
    private int sx, sy, lx, ly;	
	
	public LuxsferDisplayCalibration(int heightCount, int widthCount){
		
        if(sqTab == null){
            //sqTab = new int[9][16][2];
        	System.out.println("sqtab not null");
            sqTab = new int[heightCount][widthCount][2];
            coTab = new Color[sqTab.length][sqTab[0].length];

            for(int i=0, si=30; i<sqTab.length; i++, si+=20){
                for(int j=0, sj=30; j<sqTab[i].length; j++, sj+=20){
                    sqTab[i][j][0] = sj+30;
                    sqTab[i][j][1] = si+30;
                }
            }
        }else{
            coTab = new Color[sqTab.length][sqTab[0].length];
            setNewColors();
        }		
        setCalibrationBord();
		
		
		setBackground(Color.pink);
		setVisible(true);
		
		addKeyListener(
			new KeyAdapter() {
				
				public void keyReleased(KeyEvent evt){
					if(!evt.isShiftDown())
						shift = false;
				}
				
				public void keyPressed(KeyEvent evt){
                    if(evt.isShiftDown())
                        shift = true;
                    					
					switch(evt.getKeyChar()){
						case 'q':
							//System.out.println(">q< pressed");
							LuxsferDisplayCalibration.this.setVisible(false);
							LuxsferDisplayCalibration.this.dispose();
							break;
							
						case 'f':
							//System.out.println(">f< pressed");
							System.out.println(
									LuxsferDisplayCalibration.this.getGraphicsConfiguration().getDevice().getIDstring()
							);
							LuxsferDisplayCalibration.this.getGraphicsConfiguration().getDevice().setFullScreenWindow(LuxsferDisplayCalibration.this);
							break;
							
						case 's':
							String sstr = LuxsferDisplayCalibration.this.getGraphicsConfiguration().getDevice().getIDstring();
							sstr = sstr.replace(' ', '_');
							File sf = new File(sstr);
							try {
								FileOutputStream fos = new FileOutputStream(sf);
								ObjectOutputStream oos = new ObjectOutputStream(fos);
                                oos.writeObject(sqTab);
                                fos.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
							
						case 'l':
							String lstr = LuxsferDisplayCalibration.this.getGraphicsConfiguration().getDevice().getIDstring();
							lstr = lstr.replace(' ', '_');
							File lf = new File(lstr);
							try {
					            FileInputStream fis = new FileInputStream(lf);
					            ObjectInputStream ois = new ObjectInputStream(fis);
					            sqTab = (int[][][]) ois.readObject();
					            ois.close();
					            LuxsferDisplayCalibration.this.repaint();
							} catch(FileNotFoundException ex){
								System.out.println("nie ma takiego pliku");
							} catch (Exception e) {
								e.printStackTrace();
							}
							break;
					}	
				}
			}
		);
		
        addMouseListener(
                new MouseAdapter(){
                    public void mousePressed(MouseEvent e) {
                        int margin = 5,
                            mx = e.getX(),
                            my = e.getY();

                        for(int i=0; i<sqTab.length; i++){
                            for(int j=0; j<sqTab[i].length; j++){
                                if(
                                        (sqTab[i][j][0] - margin < mx && mx < sqTab[i][j][0] + margin ) &&
                                                (sqTab[i][j][1] - margin < my && my < sqTab[i][j][1] + margin )
                                        ){
                                    sx = j;
                                    sy = i;
                                    selected = true;
                                }
                            }
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                        if(selected){
                            selected = false;
                            repaint();
                        }
                    }

                }
        );

        addMouseMotionListener(
                new MouseMotionAdapter(){
                	
                    public void mouseDragged(MouseEvent e) {
                        if(selected && !shift){
                            sqTab[sy][sx][0] = e.getX();
                            sqTab[sy][sx][1] = e.getY();
                            repaint();
                        }else{
                            if(shift){
                                for(int i = 0; i < sqTab[0].length; i++){
                                    sqTab[sy][i][1] = e.getY();
                                }
                                for(int i = 0; i < sqTab.length; i++){
                                    sqTab[i][sx][0] = e.getX();
                                }
                                repaint();
                            }
                        }
                    }
                    
                    public void mouseMoved(MouseEvent e){
                    	if(!selected){
                    		int margin = 5,
                                mx = e.getX(),
                                my = e.getY();

                            for(int i=0; i<sqTab.length; i++){
                                for(int j=0; j<sqTab[i].length; j++){
                                    if(
                                        (sqTab[i][j][0] - margin < mx && mx < sqTab[i][j][0] + margin ) &&
                                        (sqTab[i][j][1] - margin < my && my < sqTab[i][j][1] + margin )
                                    ){
                                        lx = j;
                                        ly = i;
                                        over = true;
                                        repaint();
                                        return;
                                    }
                                }
                            }
							if(over){
								over = false;
								repaint();
							}
                    	}
                    }
                }
        );
	}
	
    public void paint(Graphics g){
        int[][] tab = new int[2][4];
        for(int i=0; i<sqTab.length-1; i++){
            for(int j=0; j<sqTab[i].length-1; j++){

                tab[0][0] = sqTab[i][j][0];
                tab[1][0] = sqTab[i][j][1];
                tab[0][1] = sqTab[i+1][j][0];
                tab[1][1] = sqTab[i+1][j][1];
                tab[0][2] = sqTab[i+1][j+1][0];
                tab[1][2] = sqTab[i+1][j+1][1];
                tab[0][3] = sqTab[i][j+1][0];
                tab[1][3] = sqTab[i][j+1][1];

                g.setColor(coTab[i][j]);
                g.fillPolygon(tab[0], tab[1], 4);
            }
        }
        if(over){
        	if(selected)
        		g.setColor(Color.red);
        	else
        		g.setColor(Color.green);
	        g.drawOval(sqTab[ly][lx][0]-5, sqTab[ly][lx][1]-5, 10, 10);
        }
    }
	
    public void setNewColors(){
        for(int i=0; i<sqTab.length; i++){
            for(int j=0; j<sqTab[i].length; j++){
                coTab[i][j] = new Color(
                        (int)(Math.random()*255),
                        (int)(Math.random()*255),
                        (int)(Math.random()*255)
                );
            }
        }
    }
	
    public void setCalibrationBord(){
        for(int i=0; i<coTab.length; i++){
            for(int j=0; j<coTab[i].length; j++){

                if(i%2 == 0)
                    if(j%2 == 0)
                        coTab[i][j] = Color.BLACK;
                    else
                        coTab[i][j] = Color.LIGHT_GRAY;
                else
                if(j%2 == 0)
                    coTab[i][j] = Color.LIGHT_GRAY;
                else
                    coTab[i][j] = Color.BLACK;
            }
        }
    }

	
}