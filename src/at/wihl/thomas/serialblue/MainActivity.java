package at.wihl.thomas.serialblue;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Menu;
import android.widget.ImageView;


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

public class MainActivity extends Activity {

	private Bitmap mColorWheel;
	private ImageView mColorWheelView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int width = 6 * 255;
        int height = 2 * 255;
        int[] color = new int[width * height];
        for (int y = 0; y < height; ++y)
        {
        	int x = 0;
        	for (int x1 = 0; x1 < 255; ++x1)
        	{
            	int r = 255 - y, g = 255 - y, b = 255 - y;
            	r += 255;
        		g += x1;
        		if (r < 0) r = 0;
        		if (r > 255) r = 255;
        		if (g < 0) g = 0;
        		if (g > 255) g = 255;
        		if (b < 0) b = 0;
        		if (b > 255) b = 255;
        		color[x + y * width] = Color.rgb(r, g, b);
        		++x;
        	}
        	for (int x1 = 0; x1 < 255; ++x1)
        	{
            	int r = 255 - y, g = 255 - y, b = 255 - y;
        		r += 255 - x1;
        		g += 255;
        		if (r < 0) r = 0;
        		if (r > 255) r = 255;
        		if (g < 0) g = 0;
        		if (g > 255) g = 255;
        		if (b < 0) b = 0;
        		if (b > 255) b = 255;
        		color[x + y * width] = Color.rgb(r, g, b);
        		++x;
        	}
        	for (int x1 = 0; x1 < 255; ++x1)
        	{
            	int r = 255 - y, g = 255 - y, b = 255 - y;
        		g += 255;
        		b += x1;
        		if (r < 0) r = 0;
        		if (r > 255) r = 255;
        		if (g < 0) g = 0;
        		if (g > 255) g = 255;
        		if (b < 0) b = 0;
        		if (b > 255) b = 255;
        		color[x + y * width] = Color.rgb(r, g, b);
        		++x;
        	}
        	for (int x1 = 0; x1 < 255; ++x1)
        	{
            	int r = 255 - y, g = 255 - y, b = 255 - y;
        		g += 255 - x1;
        		b += 255;
        		if (r < 0) r = 0;
        		if (r > 255) r = 255;
        		if (g < 0) g = 0;
        		if (g > 255) g = 255;
        		if (b < 0) b = 0;
        		if (b > 255) b = 255;
        		color[x + y * width] = Color.rgb(r, g, b);
        		++x;
        	}
        	for (int x1 = 0; x1 < 255; ++x1)
        	{
            	int r = 255 - y, g = 255 - y, b = 255 - y;
        		b += 255;
        		r += x1;
        		if (r < 0) r = 0;
        		if (r > 255) r = 255;
        		if (g < 0) g = 0;
        		if (g > 255) g = 255;
        		if (b < 0) b = 0;
        		if (b > 255) b = 255;
        		color[x + y * width] = Color.rgb(r, g, b);
        		++x;
        	}
        	for (int x1 = 0; x1 < 255; ++x1)
        	{
            	int r = 255 - y, g = 255 - y, b = 255 - y;
        		b += 255 - x1;
        		r += 255;
        		if (r < 0) r = 0;
        		if (r > 255) r = 255;
        		if (g < 0) g = 0;
        		if (g > 255) g = 255;
        		if (b < 0) b = 0;
        		if (b > 255) b = 255;
        		color[x + y * width] = Color.rgb(r, g, b);
        		++x;
        	}
        }
        mColorWheel = Bitmap.createBitmap(color, width, height, Bitmap.Config.ARGB_8888);
        mColorWheelView = (ImageView)findViewById(R.id.colorWheel);
        mColorWheelView.setImageBitmap(mColorWheel);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
