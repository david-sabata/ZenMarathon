package cz.emo4d.mcontrol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NetService extends Service {

	public class ServiceBinder extends Binder {
		public NetService getService() {
			return NetService.this;
		}
	};

	private final IBinder mBinder = new ServiceBinder();

	private Socket socket = null;
	private BufferedReader in;
	private PrintWriter out;

	private String mHost = null;

	private final static int SERVERPORT = 5869;
	private final static String SERIALIZER_DELIMITER = "&&&&";

	@Override
	public IBinder onBind(Intent arg0) {

		return mBinder;
	}

	public boolean openConnection(String host) {

		mHost = host;
		OpenConnTask oc = new OpenConnTask();
		oc.execute();

		return true;
	}

	public boolean getSocketState() {
		if (socket == null)
			return false;
		else
			return true;
	}

	public void close() {
		Socket s = socket;
		socket = null;

		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean proceedIncomingData(String incoming) {
		Log.i("INCOMING:", incoming);
		return true;
	}

	private class OpenConnTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			InetAddress serverAddr = null;
			try {
				serverAddr = InetAddress.getByName(mHost);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
				return null;
			}

			// open socket
			try {
				socket = new Socket(serverAddr, SERVERPORT);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			// prepare output buffer
			try {
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// run input listener
			try {
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			// InputListener il = new InputListener();
			// il.execute();
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			super.onPostExecute(param);

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);

		}

	}

	private class InputListener extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			while (socket != null) {
				try {
					String serverMessage = in.readLine();
					if (serverMessage != null)
						proceedIncomingData(serverMessage);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			super.onPostExecute(param);

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);

		}

	}

	public boolean sendControlEvent(int type, int valueX, int valueY) {
		String serialized = new String();

		float X = (float)valueX / (float)100;
		float Y = (float)valueY / (float)100;

		serialized = Integer.toString(type) + SERIALIZER_DELIMITER
				+ Float.toString(X) + SERIALIZER_DELIMITER + Float.toString(Y);

		SendAsync exe = new SendAsync(serialized);
		exe.execute();

		return true;
	}

	private class SendAsync extends AsyncTask<Void, Void, Void> {

		private String message;

		public SendAsync(String s) {
			message = s;
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			if (socket != null) {
				out.println(message);
				out.flush();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void param) {
			super.onPostExecute(param);

		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);

		}
	}

}
