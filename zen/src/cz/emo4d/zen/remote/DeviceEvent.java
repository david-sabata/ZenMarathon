package cz.emo4d.zen.remote;

public class DeviceEvent {
	private final static int CONNECT = 0;
	private final static int DISCONNECT = 1; 
	private final static int PRESS_A = 2;
	private final static int PRESS_B = 3;
	private final static int MOVE = 4;

	
	public int eventType;
	public int player;
	public float valueX;
	public float valueY;
}
