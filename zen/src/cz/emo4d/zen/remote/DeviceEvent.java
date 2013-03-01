package cz.emo4d.zen.remote;

public class DeviceEvent {
	public final static int CONNECT = 0;
	public final static int DISCONNECT = 1; 
	public final static int PRESS_A = 2;
	public final static int PRESS_B = 3;
	public final static int MOVE = 4;

	
	public int eventType;
	public int player;
	public float valueX;
	public float valueY;
}
