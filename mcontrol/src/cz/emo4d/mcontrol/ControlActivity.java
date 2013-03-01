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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ControlActivity extends Activity {

	private final String LOG_TAG = "ZenMobileControl";
	private int arrowImgXY[];
	private int btnAXY[];
	private int btnBXY[];
	
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

		getApplicationContext().bindService(new Intent(this, NetService.class), netServiceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onStop() {
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
		arrowImgXY = new int[2];
		arrowController.getLocationOnScreen(arrowImgXY);
		
		ImageView BtnAController = (ImageView) findViewById(R.id.imageViewBtnA);
		btnAXY = new int[2];
		BtnAController.getLocationOnScreen(btnAXY);
		
		ImageView BtnBController = (ImageView) findViewById(R.id.imageViewBtnB);
		btnBXY = new int[2];
		BtnBController.getLocationOnScreen(btnBXY);
		
		
	}

	public boolean onTouchEvent(MotionEvent event) {
		int eventX = (int) event.getX();
		int eventY = (int) event.getY();
		
		if ((event.getAction() == MotionEvent.ACTION_DOWN)
				|| (event.getAction() == MotionEvent.ACTION_MOVE)) {
			
			int xOnField = eventX - arrowImgXY[0] - 130; // 130 - picture center
			int yOnField = eventY - arrowImgXY[1] - 130;
			
			if ((xOnField < 100) && (yOnField < 100) && (xOnField > -100) && (yOnField > -100)) {
				// arrows
				//Log.i(LOG_TAG, "on arrows (x, y) = " + Integer.toString(xOnField)
				//		+ "   " + Integer.toString(yOnField));
				if (mServiceBound) {
					mService.sendControlEvent(EventTypes.MOVE, xOnField, yOnField);
				}
				
				return super.onTouchEvent(event);
			}
			
			xOnField = eventX - btnAXY[0];
			yOnField = eventY - btnAXY[1];
			
			if (((xOnField < 150) && (yOnField < 150) && (xOnField > 0) && (yOnField > 0))) {
				Log.i(LOG_TAG, "Button A");
				if (mServiceBound) {
					mService.sendControlEvent(EventTypes.PRESS_A, 0, 0);
				}
				
				
				return super.onTouchEvent(event);
			}
			
			xOnField = eventX - btnBXY[0];
			yOnField = eventY - btnBXY[1];
			
			if (((xOnField < 150) && (yOnField < 150) && (xOnField > 0) && (yOnField > 0))) {
				Log.i(LOG_TAG, "Button B");
				if (mServiceBound) {
					mService.sendControlEvent(EventTypes.PRESS_B, 0, 0);
				}
				return super.onTouchEvent(event);
			}
			
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (mServiceBound) {
				mService.sendControlEvent(EventTypes.MOVE, 0, 0);
			}
			Log.i(LOG_TAG, "End of gesture");
		}
		
		return super.onTouchEvent(event);
	}
	
	
	private ServiceConnection netServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			final ServiceBinder binder = (ServiceBinder) service;

			mService = binder.getService();
			mServiceBound = true;

			//ExecTask exe = new ExecTask();
			//exe.execute();
			Log.i(LOG_TAG,"Service bound");
			
			mService.openConnection("147.229.178.65");
			
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mServiceBound = false;
			mService = null;
		}
	};

}
