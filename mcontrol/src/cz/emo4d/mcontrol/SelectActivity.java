package cz.emo4d.mcontrol;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import cz.emo4d.mcontrol.NetService.ServiceBinder;

public class SelectActivity extends Activity {

	private final String LOG_TAG = "ZenMobileControl";


	private boolean mServiceBound = false;
	private NetService mService = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_select);
	}

	@Override
	public void onStart() {
		super.onStart();

		getApplicationContext().bindService(new Intent(this, NetService.class),
				netServiceConnection, Context.BIND_AUTO_CREATE);

		final Button button = (Button) findViewById(R.id.button_master);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mServiceBound) {
					mService.setMode(NetService.MODE_MASTER);
					
					Intent i = new Intent(getApplicationContext(),ControlActivity.class);
					startActivity(i);
				}
			}
		});
		
		final Button button2 = (Button) findViewById(R.id.button_slave);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mServiceBound) {
					mService.setMode(NetService.MODE_SLAVE);
					
					Intent i = new Intent(getApplicationContext(),ControlActivity.class);
					startActivity(i);
				}
			}
		});

	}

	@Override
	public void onStop() {
		//if (mServiceBound) mService.unbindService(netServiceConnection);
		
		super.onStop();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	
	private ServiceConnection netServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			final ServiceBinder binder = (ServiceBinder) service;

			mService = binder.getService();
			mServiceBound = true;

			
			Log.i(LOG_TAG, "Service bound");

			//mService.runAutoDiscovery();
			//mService.openConnection();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mServiceBound = false;
			mService = null;
		}
	};

	
}
