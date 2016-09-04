package mtPack.gameServer;

import java.awt.Color;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public 
	class NM2016_MemoryGame {
	
	private int[][] logicbord;
	private mtPack.luksfera.LuxsferDisplay ld;
	

	private int offsetX, offsetY;
	
	private final static Color colorToNum[] = {
		null,
		Color.red,
		Color.green,
		Color.blue,
		Color.cyan,
		Color.magenta,
		Color.pink,
		Color.orange
	};
	
	private final static Color covered = Color.DARK_GRAY;
	NM2016Games parent;
	ServerSocket ss;
	Socket soc;
	InputStream is;
	
	private boolean connectionLost = false;
	
	public NM2016_MemoryGame(int screenWidth, int screenHeight, int x, int y, int offsetX, int offsetY){
		ld = mtPack.luksfera.LuxsferDisplay.make( screenWidth, screenHeight);
		logicbord = new int[x][y];
		
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		new Thread(){
			public void run(){
				try{
					ss = new ServerSocket(1701);					
				}catch(Exception ex){
					System.out.println("Ex"+ex);
				}
				while(true){
					try{
						System.out.println("W8 4 new conn");
						soc = ss.accept();
						System.out.println("I have a Connection");
						is = soc.getInputStream();
						runGame();
						System.out.println("Game initiated");
					}catch(Exception ex){
						System.out.println("A: "+ex);
						ex.printStackTrace();
						connectionLost = true;
					}
				}
			}
		}.start();
		paintGameBord();
	}
	
	public void initNewGame(){
		ArrayList<Integer> al = new ArrayList<Integer>();
		for(int i=0; i<(logicbord.length*logicbord[0].length)/2; i++){
			int wrt = (int)(Math.random()*(colorToNum.length-1)) + 1;
			al.add(new Integer(wrt));
			al.add(new Integer(wrt));
		}
		
		int count = 0;
	
		while(!al.isEmpty()){
			int wrt = (int)(Math.random()*al.size());
			logicbord[count / logicbord[0].length][count % logicbord[0].length] = al.remove(wrt).intValue();
			count++;
		}		
		this.count = 0;
		System.out.println("new logi");
		last = false;
	}
	
	boolean last;
	int lastX, lastY;
	int count;
	
	public void runGame(){
		if(!connectionLost){
			initNewGame();
		}else{
			connectionLost = false;
		}
		
		while(count < (logicbord[0].length+logicbord.length)){
					
			paintGameBord();
			int[] tmp = read();
			if(logicbord[tmp[0]][tmp[1]] > 0)
			if(!last){
				last = true;
				lastX = tmp[0];
				lastY = tmp[1];
				logicbord[lastX][lastY] *= -1;
				paintGameBord();
			}else{
				if(!(lastX == tmp[0] && lastY == tmp[1])){
					System.out.println(
							Math.abs(logicbord[lastX][lastY]) +" "+ Math.abs(logicbord[tmp[0]][tmp[1]]) +" "+
						(Math.abs(logicbord[lastX][lastY]) == Math.abs(logicbord[tmp[0]][tmp[1]]))
					);
					
					if(Math.abs(logicbord[lastX][lastY]) == Math.abs(logicbord[tmp[0]][tmp[1]])){
						logicbord[tmp[0]][tmp[1]] *= -1;
						count += 2;
						paintGameBord();
					}else{
						logicbord[tmp[0]][tmp[1]] *= -1;
						paintGameBord();
						try{
							Thread.sleep(1000);
						}catch(Exception ex){
							ex.printStackTrace();
						}
						logicbord[lastX][lastY] *= -1;
						logicbord[tmp[0]][tmp[1]] *= -1;
						paintGameBord();
					}
					last = false;
				}
			}
		}
	}
	
	public int[] read(){
		try{
			int wrt = 'p';//is.read();
			switch(wrt){
				case 'p':
					int y = 2;//is.read()-'0';
					int x = 2;//is.read()-'0';
					System.out.println("r: "+x+" "+y+" "+x+" "+(6-y));
					//x = 8 - x;
					y = 6 - y;
					System.out.println("r: "+x+" "+y);
					return new int[]{x,y};
				case 'l':
					break;
				case 'r':
					break;
				/*
				default:
					System.out.println((char)wrt);
					wrt = is.read();
					System.out.println((char)wrt);
					wrt = is.read();
					System.out.println((char)wrt);
					wrt = is.read();
					System.out.println((char)wrt);
					wrt = is.read();
					System.out.println((char)wrt);
					wrt = is.read();
					System.out.println((char)wrt);
					return new int[]{0,0};
				*/
			}
		}catch(Exception ex){
			System.out.println(ex);
			ex.printStackTrace();
		}
		return null;
	}
	
	public void paintGameBord(){
		for(int i=0; i<logicbord.length; i++){
			for(int j=0; j<logicbord[i].length;j++){
				System.out.print(logicbord[i][j]);
				if(logicbord[i][j] < 0)
					ld.luxsferColorBuffer[offsetX+i][offsetY+j] = colorToNum[Math.abs(logicbord[i][j])];
				else
					ld.luxsferColorBuffer[offsetX+i][offsetY+j] = covered;
			}
			System.out.println();
		}
		ld.fireLuxsferDisplayEvent(
			new mtPack.luksfera.event.LuxsferDisplayEvent(this, mtPack.luksfera.event.LuxsferDisplayEvent.LUXSFERDISPLAYPIXELCHANGED)
		);
	}	
}
