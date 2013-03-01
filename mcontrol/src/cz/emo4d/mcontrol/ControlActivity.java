package cz.emo4d.mcontrol;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class ControlActivity extends Activity {

	private final String LOG_TAG = "IMG";
	private int imgXY[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_control);
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
		imgXY = new int[2];
		arrowController.getLocationOnScreen(imgXY);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if ((event.getAction() == MotionEvent.ACTION_DOWN)
				|| (event.getAction() == MotionEvent.ACTION_MOVE)) {
			//Log.i(LOG_TAG, "touch event - down");

			int eventX = (int) event.getX();
			int eventY = (int) event.getY();
			//Log.i(LOG_TAG, "event (x, y) = " + Integer.toString(eventX) + "  "
			//		+ Integer.toString(eventY));

			int xOnField = eventX - imgXY[0] - 130; // 130 - picture center
			int yOnField = eventY - imgXY[1] - 130;
			
			if (xOnField > 100) return super.onTouchEvent(event);
			if (yOnField > 100) return super.onTouchEvent(event);
			
			Log.i(LOG_TAG, "on field (x, y) = " + Integer.toString(xOnField)
					+ "   " + Integer.toString(yOnField));
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.i(LOG_TAG, "End of gesture");
		}
		
		return super.onTouchEvent(event);
	}
}
