package mtPack.luksfera;

import java.awt.Frame;
import java.util.ArrayList;

import mtPack.luksfera.event.*;
public
	class LuxEventsLayer
	extends Frame{
	
	ArrayList<LuxsferDisplayListener> listenersList;
	
	
	public LuxEventsLayer(){
		listenersList = new ArrayList();
	}
	
	public void addLuxsferDisplayListener(LuxsferDisplayListener lst){
		listenersList.add(lst);
	}
	
	public void removeLuxsferDisplayListener(LuxsferDisplayListener lst){
		this.listenersList.remove(lst);
	}
	
	public void fireLuxsferDisplayEvent(LuxsferDisplayEvent evt){
		for( LuxsferDisplayListener lst : listenersList){
			lst.luxsfereChanged(evt);
		}
	}
	
}