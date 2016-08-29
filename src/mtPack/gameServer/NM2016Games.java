package mtPack.gameServer;

import java.io.IOException;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public 
	class NM2016Games
	implements Runnable {
		
	private int[][] logicbord;
	private static mtPack.luksfera.LuxsferDisplay ld;
	
	private final static Color colorToNum[] = {
		Color.red,
		Color.green,
		Color.blue,
		Color.cyan,
		Color.magenta,
		Color.pink,
		Color.orange
	};
	
	private int offsetX, offsetY;
	private int lastX, lastY; 
	
	public NM2016Games(int screenWidth, int screenHeight, int x, int y, int offsetX, int offsetY){
		ld = mtPack.luksfera.LuxsferDisplay.make( screenWidth, screenHeight);
		ld.luxsferColorBuffer = new Color[screenWidth][screenHeight];
		
		for(int i=0; i<ld.luxsferColorBuffer.length; i++){
			for(int j=0; j<ld.luxsferColorBuffer[i].length; j++){
				ld.luxsferColorBuffer[i][j] = Color.black;
			}
		}
	
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		
		this.genereteBord1(x, y);

		new Thread(this).start();
	}
	
	public void run(){
		try{
			ServerSocket ss = new ServerSocket(1701);
			//System.out.println(ss.getInetAddress()+" - addr");
			while(true){
				try{
					Socket soc = ss.accept();
					InputStream is = soc.getInputStream();
					while(true){
						System.out.println("recived: "+(char)is.read());
					}
				}catch(Exception ex){
					System.out.println("Ex"+ex);
				}
			}
		}catch(Exception ex){
			System.out.println("Ex"+ex);
		}
	}
	
	public void show(){
		for(int i=0; i<logicbord.length; i++){
			for(int j=0; j<logicbord[i].length;j++)
				System.out.print(logicbord[i][j]+"\t");
			System.out.println();
		}
	}
	
	public void genereteBord1(int x, int y){
		ArrayList<Integer> al = new ArrayList<Integer>();
		for(int i=0; i<(x*y)/2; i++){
			int wrt = (int)(Math.random()*colorToNum.length);
			al.add(new Integer(wrt));
			al.add(new Integer(wrt));
		}
		logicbord = new int[x][y];
		int count = 0;

		while(!al.isEmpty()){
			int wrt = (int)(Math.random()*al.size());
			logicbord[count / y][count % y] = al.remove(wrt).intValue();
			count++;
		}
		paintDataBord();
	}
	
	public void genereteBord2(int x, int y){
	}
	
	public void genereteBord3(int x, int y){	
	}
	
	
	public void paintDataBord(){
		
		for(int i=0; i<logicbord.length; i++){
			for(int j=0; j<logicbord[i].length;j++)
				ld.luxsferColorBuffer[offsetX+i][offsetY+j] = colorToNum[logicbord[i][j]];
		}
		ld.fireLuxsferDisplayEvent(
			new mtPack.luksfera.event.LuxsferDisplayEvent(this, mtPack.luksfera.event.LuxsferDisplayEvent.LUXSFERDISPLAYPIXELCHANGED)
		);
	}
}
