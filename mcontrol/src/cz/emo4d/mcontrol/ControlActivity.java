package cz.emo4d.mcontrol;

import cz.emo4d.mcontrol.NetService.ServiceBinder;
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
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class ControlActivity extends Activity {

	private final String LOG_TAG = "ZenMobileControl";
	private int arrowImgXY[];


	private boolean mServiceBound = false;
	private NetService mService = null;

	int lastX;
	int lastY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_control);
	}

	@Override
	public void onStart() {
		super.onStart();

		getApplicationContext().bindService(new Intent(this, NetService.class),
				netServiceConnection, Context.BIND_AUTO_CREATE);

//		final Button button = (Button) findViewById(R.id.button1);
//		button.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				if (mServiceBound) {
//					mService.sendControlEvent(EventTypes.PRESS_A, 0, 0);
//				}
//			}
//		});
//		
//		final Button button2 = (Button) findViewById(R.id.button2);
//		button2.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				if (mServiceBound) {
//					mService.sendControlEvent(EventTypes.PRESS_B, 0, 0);
//				}
//			}
//		});
		
		final Button button = (Button) findViewById(R.id.button1);
		button.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mServiceBound) {
					mService.sendControlEvent(EventTypes.PRESS_A, 0, 0);
				}
				return true;
			}
		});
		
		final Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mServiceBound) {
					mService.sendControlEvent(EventTypes.PRESS_B, 0, 0);
				}
				return true;
			}
		});
		
		

	}

	@Override
	public void onStop() {
		//if (mServiceBound) mService.close();
		
		super.onStop();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		ImageView arrowController = (ImageView) findViewById(R.id.imageView1);
		//arrowImgXY = new int[2];
		//arrowController.getLocationOnScreen(arrowImgXY);
		
		arrowController.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int action = event.getAction();
				int eventX = (int) event.getX();
				int eventY = (int) event.getY();
				
				if ((action == MotionEvent.ACTION_DOWN) ||
						(action == MotionEvent.ACTION_MOVE)) {
					
					int xOnField = eventX - 250; 
																
					int yOnField = eventY  - 250;

					if ((xOnField < 250) && (yOnField < 250)
							&& (xOnField > -250) && (yOnField > -250)) {
						// arrows
						// Log.i(LOG_TAG,
						// "on arrows (x, y) = " + Integer.toString(xOnField)
						// + "   " + Integer.toString(yOnField));
						if (mServiceBound) {
							mService.sendControlEvent(EventTypes.MOVE,
									xOnField, yOnField);
						}

						return true;
					}
					
				}
				
				if (action == MotionEvent.ACTION_UP) {
					if (mServiceBound) {
						mService.sendControlEvent(EventTypes.MOVE, 0, 0);
					}
					Log.i(LOG_TAG, "End of gesture");
				}
				
				return false;
			}
		});

	}

	private ServiceConnection netServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			final ServiceBinder binder = (ServiceBinder) service;

			mService = binder.getService();
			mServiceBound = true;

			// ExecTask exe = new ExecTask();
			// exe.execute();
			Log.i(LOG_TAG, "Service bound");

			mService.runAutoDiscovery();
			//mService.openConnection();

		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mServiceBound = false;
			mService = null;
		}
	};

}
