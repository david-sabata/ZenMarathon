package cz.emo4d.mcontrol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class NetService extends Service{

	public class ServiceBinder extends Binder {
		public NetService getService() {
			return NetService.this;
		}
	};

	private final IBinder mBinder = new ServiceBinder();
	
	private Socket socket = null;
	private final static int SERVERPORT = 5869;

	@Override
	public IBinder onBind(Intent arg0) {
				
		return mBinder;
	}

	
	public boolean openConnection(String host) {
		InetAddress serverAddr = null;
		try {
			serverAddr = InetAddress.getByName(host);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return false;
		}
		
		try {
			socket = new Socket(serverAddr, SERVERPORT);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean getSocketState() {
		if (socket == null) return false;
		else return true;
	}

}
