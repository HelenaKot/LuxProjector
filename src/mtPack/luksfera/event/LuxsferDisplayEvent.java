package mtPack.luksfera.event;

import java.io.Serializable;
import java.util.EventObject;

public
	class LuxsferDisplayEvent
	extends EventObject 
	implements Serializable{
	
	public static final int LUXSFERDISPLAYPIXELCHANGED = 1;
	public static final int LUXSFERDISPLAYIMAGECHANGED = 2;
	public static final int LUXSFERDISPLAYREFRESHREQ = 4;
	
	private int id;

	public LuxsferDisplayEvent(Object src, int id){
		super(src);
		this.id = id;
	}
}