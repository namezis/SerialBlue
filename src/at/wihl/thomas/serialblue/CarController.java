package at.wihl.thomas.serialblue;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

public class CarController extends Activity {
	private ImageView mSpeed;
	private ImageView mLeds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_controller);
		mSpeed = (ImageView)findViewById(R.id.speed);
		mLeds = (ImageView)findViewById(R.id.leds);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.car_controller, menu);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

	    // get pointer index from the event object
	    int pointerIndex = event.getActionIndex();

	    // get pointer ID
	    int pointerId = event.getPointerId(pointerIndex);

	    // get masked (not specific to a pointer) action
	    int maskedAction = event.getActionMasked();

	    switch (maskedAction) {
		    case MotionEvent.ACTION_DOWN:
		    case MotionEvent.ACTION_POINTER_DOWN: {
			    break;
		    }
		    case MotionEvent.ACTION_MOVE: {
		    	break;
		    }
		    case MotionEvent.ACTION_UP:
		    case MotionEvent.ACTION_POINTER_UP:
		    case MotionEvent.ACTION_CANCEL: {
		    	break;
		    }
	    }
    	Rect r = new Rect();
    	int x = (int) event.getX(pointerIndex);
    	int y = (int) event.getY(pointerIndex);
    	if (mSpeed.getGlobalVisibleRect(r))
    	{
    		if (r.contains(x, y))
    		{
    			int speed = 100 - 250 * (y - r.top) / r.height();
    			if (speed > 100) speed = 100;
    			else if (speed < -100) speed = -100;
    			else if (speed < 20 && speed > -20) speed = 0;
    			Globals.mBluetooth.write("motor 0 " + speed + "\n");
    		}
    	}
    	if (mLeds.getGlobalVisibleRect(r))
    	{
    		if (r.contains(x, y))
    		{
    			int brightness = 112 - 125 * (y - r.top) / r.height();
    			if (brightness < 0) brightness = 0;
    			else if (brightness > 100) brightness = 100;
    			int index = 16 * (x - r.left) / r.width();
    			Globals.mBluetooth.write("led " + index + " " + brightness + "\n");
    		}
    	}

	    return true;
	} 
}
