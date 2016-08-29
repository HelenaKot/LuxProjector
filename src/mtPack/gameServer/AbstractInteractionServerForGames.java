package mtPack.gameServer;

import java.net.*;
import java.io.*;

import mtPack.luksfera.*;

public 
	abstract class AbstractInteractionServerForGames 
	implements Runnable {
	
	private ServerSocket ss;
	private Socket soc;

	public AbstractInteractionServerForGames()
		throws IOException {
		
		ss = new ServerSocket(1701);
		System.out.println("Adress: "+ss.getInetAddress());
			
		while(true){
			soc = ss.accept();
			new Thread(this).start();
		}
	}
	
	public void run(){
		Socket soc = this.soc;
		try{
			InputStream is = soc.getInputStream();
			OutputStream os = soc.getOutputStream();
			
			while(true){
				switch(is.read()){
					case 0:
						break;
					case 2:
						break;
					case 4:
						break;
					case 8:
						break;
					case 16:
						break;
				}
			}
		}catch(Exception ex){
			System.out.println("Soc: "+ex);
		}
	}
	
	public abstract void callMove(int x, int y);

}
