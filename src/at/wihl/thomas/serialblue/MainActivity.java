package at.wihl.thomas.serialblue;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/* sdptool records 00:06:66:4D:62:AF
Service Name: SPP
Service RecHandle: 0x10000
Service Class ID List:
  "Serial Port" (0x1101)
Protocol Descriptor List:
  "L2CAP" (0x0100)
  "RFCOMM" (0x0003)
    Channel: 1
Language Base Attr List:
  code_ISO639: 0x656e
  encoding:    0x6a
  base_offset: 0x100
  
  
  UUID should be 00001101-0000-1000-8000-00805F9B34FB
  SerialPort UUID is 0x1101 with base UUID 00000000-0000-1000-8000-00805F9B34FB
  gives the one above.
*/

public class MainActivity extends Activity implements SensorEventListener {

	final private int GROUP_DEVICES = 2;
	private Bitmap mColorWheel;
	private TextView mAccelValue;
	private TextView mOutput;
	private ImageView mColorWheelView;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Handler mTimer = new Handler();

    private Runnable timerTask = new Runnable() {
    	private int mLastLineRead = 0;
 	   @Override
 	   public void run() {
 		   while (mLastLineRead < Globals.mBluetooth.mReceivedLines.size()) {
 			   mOutput.append("\n");
 			   mOutput.append(Globals.mBluetooth.mReceivedLines.get(mLastLineRead));
 			   ++mLastLineRead;
 		   }
 		  mTimer.postDelayed(this, 1000);
 	   }
 	};

 	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        setContentView(R.layout.activity_main);
        mAccelValue = (TextView)findViewById(R.id.accelValue);
        mOutput = (TextView)findViewById(R.id.bluetoothOutput);
        Globals.mBluetooth.init(this);
        mTimer.postDelayed(timerTask, 1000);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        String deviceName = settings.getString("deviceName", "");
        if (!deviceName.isEmpty()) {
        	Globals.mBluetooth.connect(deviceName);
        }
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Dummy");
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
    	menu.removeGroup(GROUP_DEVICES);
    	List<String> devices = Globals.mBluetooth.scan();
    	for (String s : devices) {
    		menu.add(GROUP_DEVICES, Menu.NONE, Menu.NONE, s);
    	}
    	return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getGroupId() == GROUP_DEVICES) {
    		String deviceName = item.getTitle().toString();
    		if (Globals.mBluetooth.connect(deviceName)) {
    			SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
    			SharedPreferences.Editor editor = settings.edit();
    		    editor.putString("deviceName", deviceName);
    		    editor.commit();
    		}
    	}
    	else {
            switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    	}
    	return true;
    }
    
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
    	if (mAccelValue != null) mAccelValue.setText("Angle is " + String.format("%.2f", Math.atan(event.values[1] / event.values[0])));
    }
    
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    	Globals.mBluetooth.onActivityResult(requestCode, resultCode, data);
    }
    
    public void startCarController(View view) {
    	Intent intent = new Intent(this, CarController.class);
    	startActivity(intent);
    }
    
    private void createColorWheel() {
//      int width = 6 * 255;
//      int height = 2 * 255;
//      int[] color = new int[width * height];
//      for (int y = 0; y < height; ++y)
//      {
//      	int x = 0;
//      	for (int x1 = 0; x1 < 255; ++x1)
//      	{
//          	int r = 255 - y, g = 255 - y, b = 255 - y;
//          	r += 255;
//      		g += x1;
//      		if (r < 0) r = 0;
//      		if (r > 255) r = 255;
//      		if (g < 0) g = 0;
//      		if (g > 255) g = 255;
//      		if (b < 0) b = 0;
//      		if (b > 255) b = 255;
//      		color[x + y * width] = Color.rgb(r, g, b);
//      		++x;
//      	}
//      	for (int x1 = 0; x1 < 255; ++x1)
//      	{
//          	int r = 255 - y, g = 255 - y, b = 255 - y;
//      		r += 255 - x1;
//      		g += 255;
//      		if (r < 0) r = 0;
//      		if (r > 255) r = 255;
//      		if (g < 0) g = 0;
//      		if (g > 255) g = 255;
//      		if (b < 0) b = 0;
//      		if (b > 255) b = 255;
//      		color[x + y * width] = Color.rgb(r, g, b);
//      		++x;
//      	}
//      	for (int x1 = 0; x1 < 255; ++x1)
//      	{
//          	int r = 255 - y, g = 255 - y, b = 255 - y;
//      		g += 255;
//      		b += x1;
//      		if (r < 0) r = 0;
//      		if (r > 255) r = 255;
//      		if (g < 0) g = 0;
//      		if (g > 255) g = 255;
//      		if (b < 0) b = 0;
//      		if (b > 255) b = 255;
//      		color[x + y * width] = Color.rgb(r, g, b);
//      		++x;
//      	}
//      	for (int x1 = 0; x1 < 255; ++x1)
//      	{
//          	int r = 255 - y, g = 255 - y, b = 255 - y;
//      		g += 255 - x1;
//      		b += 255;
//      		if (r < 0) r = 0;
//      		if (r > 255) r = 255;
//      		if (g < 0) g = 0;
//      		if (g > 255) g = 255;
//      		if (b < 0) b = 0;
//      		if (b > 255) b = 255;
//      		color[x + y * width] = Color.rgb(r, g, b);
//      		++x;
//      	}
//      	for (int x1 = 0; x1 < 255; ++x1)
//      	{
//          	int r = 255 - y, g = 255 - y, b = 255 - y;
//      		b += 255;
//      		r += x1;
//      		if (r < 0) r = 0;
//      		if (r > 255) r = 255;
//      		if (g < 0) g = 0;
//      		if (g > 255) g = 255;
//      		if (b < 0) b = 0;
//      		if (b > 255) b = 255;
//      		color[x + y * width] = Color.rgb(r, g, b);
//      		++x;
//      	}
//      	for (int x1 = 0; x1 < 255; ++x1)
//      	{
//          	int r = 255 - y, g = 255 - y, b = 255 - y;
//      		b += 255 - x1;
//      		r += 255;
//      		if (r < 0) r = 0;
//      		if (r > 255) r = 255;
//      		if (g < 0) g = 0;
//      		if (g > 255) g = 255;
//      		if (b < 0) b = 0;
//      		if (b > 255) b = 255;
//      		color[x + y * width] = Color.rgb(r, g, b);
//      		++x;
//      	}
//      }
//      mColorWheel = Bitmap.createBitmap(color, width, height, Bitmap.Config.ARGB_8888);
//      mColorWheelView = (ImageView)findViewById(R.id.colorWheel);
//      mColorWheelView.setImageBitmap(mColorWheel);    	
    }
}
