package at.wihl.thomas.serialblue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

public class Bluetooth {
	final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //UUID for serial connection
	final int REQUEST_ENABLE_BT = 4711;
	BluetoothAdapter mAdapter = null;
	BluetoothSocket mSocket = null;
	OutputStream mWrite = null;
	InputStream mRead = null;
	boolean mEnabled = false;
	Thread mReaderThread = null;
	public List<String> mReceivedLines = new ArrayList<String>();
	
	Boolean init(Activity activity) {
		mAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mAdapter == null) {
		    // Device does not support Bluetooth
		    return false;
		}
		if (!mAdapter.isEnabled()) {
			//make sure the device's bluetooth is enabled
		    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    activity.startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
		}
		return true;
	}
	
	void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT)
		{
			if (resultCode == Activity.RESULT_OK) mEnabled = true;
			else mEnabled = false;
		}
		
	}
	
	List<String> scan() {
		Set<BluetoothDevice> devices = mAdapter.getBondedDevices();
		List<String> list = new ArrayList<String>();
		for (BluetoothDevice d : devices) {
			list.add(d.getName() + " (" + d.getAddress() + ")");
		}
		return list;
	}
	
	Runnable readerTask = new Runnable() {
        @Override
        public void run() {
            try {
                System.out.println("Reading...");
                byte[] buffer = new byte[64];
                int offset = 0;
                while (true) {
                	//System.out.println("Reading from " + offset); 
                	int bytesRead = mRead.read(buffer, offset, buffer.length - offset);
                	//System.out.println("Read " + new String(buffer, offset, bytesRead) + " = " + bytesRead + " bytes");
                	offset += bytesRead;
                	for (int i = offset - bytesRead; i < offset; ++i) {
                		if (buffer[i] == '\r' || buffer[i] == '\n') {
                        	//System.out.println("Found linefeed at " + i);
                			int left = offset - i - 1;
                        	//System.out.println("Leaving " + left + " for next line");
                			String line = new String(buffer, 0, i);
                			mReceivedLines.add(line);
                        	//System.out.println("---" + line);
                			for (int j = 0; j < left; ++j) {
                				buffer[j] = buffer[i + j + 1];
                			}
                			offset = bytesRead = left;
                			i = 0;
                		}
                	}
                }
            } catch (IOException e) {
                System.err.println("Connection closed.");
            }
        }
    };
    
    void write(String text) {
    	try {
        	if (mWrite != null) mWrite.write(text.getBytes());
    	}
    	catch (IOException e) {
    		
    	}
    }
    
	
	Boolean connect(String deviceName) {
		String[] parts = deviceName.split("\\(|\\)");
		BluetoothDevice device = mAdapter.getRemoteDevice(parts[1]);
		try {
		    mSocket = device.createRfcommSocketToServiceRecord(SERIAL_UUID); 
		} catch (IOException e) {
			return false;
		}

		try {           
		    mSocket.connect(); 
		    mWrite = mSocket.getOutputStream();
		    mRead = mSocket.getInputStream();
		    mReaderThread = new Thread(readerTask);
		    mReaderThread.start();
		    mWrite.write("ID\n".getBytes());
		} catch (IOException e) {
			return false;
		}
		return true;
	}
}
