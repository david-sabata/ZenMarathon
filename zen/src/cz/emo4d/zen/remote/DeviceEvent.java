package cz.emo4d.zen.remote;

public class DeviceEvent {
	private final static int CONNECT = 0;
	private final static int DISCONNECT = 1; 
	private final static int PRESS_A = 2;
	private final static int PRESS_B = 3;
	private final static int MOVE_UP = 4;
	private final static int MOVE_DOWN = 5;
	private final static int MOVE_LEFT = 6;
	private final static int MOVE_RIGHT = 7;
	
	public int eventType;
	public int player;
	public float value;
}
