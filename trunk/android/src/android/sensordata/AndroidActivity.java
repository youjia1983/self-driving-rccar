package android.sensordata;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.TextView;


public class AndroidActivity extends Activity implements LocationListener, SensorEventListener {
	private LocationManager lm;
    private SensorManager sensorManager = null;
    JSONObject object = new JSONObject();
    private Sender sd;
    
    // Accelerometer X, Y, and Z values
    private TextView accelXValue;
    private TextView accelYValue;
    private TextView accelZValue;
    private String aXVal;
    private String aYVal;
    private String aZVal;
    
    // Orientation X, Y, and Z values
    private TextView orientXValue;
    private TextView orientYValue;
    private TextView orientZValue;
    private String oXVal;
    private String oYVal;
    private String oZVal;
    
    // GPS lat and lon values
    private TextView gpsLat;
    private TextView gpsLon;
    private String gpsLatVal;
    private String gpsLonVal;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //---use the LocationManager class to obtain GPS locations---
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
        
        //---Get a reference to a SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        // Capture accelerometer related view elements
        accelXValue = (TextView) findViewById(R.id.accel_x_value);
        accelYValue = (TextView) findViewById(R.id.accel_y_value);
        accelZValue = (TextView) findViewById(R.id.accel_z_value);
       
        // Capture orientation related view elements
        orientXValue = (TextView) findViewById(R.id.orient_x_value);
        orientYValue = (TextView) findViewById(R.id.orient_y_value);
        orientZValue = (TextView) findViewById(R.id.orient_z_value);
        
        // Capture GPS related view elements
        gpsLat = (TextView) findViewById(R.id.gps_lat_value);
        gpsLon = (TextView) findViewById(R.id.gps_lon_value);
        
        // Initialize accelerometer related view elements        
        accelXValue.setText("0.00");
        accelYValue.setText("0.00");
        accelZValue.setText("0.00");
       
        // Initialize orientation related view elements
        orientXValue.setText("0.00");
        orientYValue.setText("0.00");
        orientZValue.setText("0.00");
        
        // Initialize GPS related view elements
        gpsLat.setText("0.00");
        gpsLon.setText("0.00");
        
        // Initialize GSON structure
        try {
			object.put("Lat", "0.00");
			object.put("Lon", "0.00");
			object.put("aX", "0.00");
			object.put("aY", "0.00");
			object.put("aZ", "0.00");
			object.put("oX", "0.00");
			object.put("oY", "0.00");
			object.put("oZ", "0.00");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
        // Send data
        sd = new Sender((WifiManager) getSystemService(Context.WIFI_SERVICE));
        sd.startThread();
    }
    
    
    
    
    
    // Here are the LocationListener methods
    public void onLocationChanged(Location loc) {
    	if (loc != null) {
    		gpsLatVal = Float.toString((float) loc.getLatitude());
    		gpsLonVal = Float.toString((float) loc.getLongitude());
    		gpsLat.setText(gpsLatVal);
    		gpsLon.setText(gpsLonVal);
    		
    		try {
    			object.put("Lat", gpsLatVal);
    			object.put("Lon", gpsLonVal);
    			
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	}
    }

    public void onProviderDisabled(String provider) {
    	// no op
    }

    public void onProviderEnabled(String provider) {
    	// no op
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    	// no op
    }
    // end of LocationListener methods
    
    
    
    
    
    
    
    // Here are the SensorEventListener methods
    // This method will update the UI on new sensor events
    public void onSensorChanged(SensorEvent sensorEvent) {
    	synchronized (this) {
    		if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
    			aXVal = Float.toString(sensorEvent.values[0]);
    			aYVal = Float.toString(sensorEvent.values[1]);
    			aZVal = Float.toString(sensorEvent.values[2]);
    			accelXValue.setText(aXVal);
    			accelYValue.setText(aYVal);
    			accelZValue.setText(aZVal);
    			
    			try {
        			object.put("aX", aXVal);
        			object.put("aY", aYVal);
        			object.put("aZ", aZVal);
        			
        		} catch (JSONException e) {
        			e.printStackTrace();
        		}
    		}

    		if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
    			oXVal = Float.toString(sensorEvent.values[0]);
    			oYVal = Float.toString(sensorEvent.values[1]);
    			oZVal = Float.toString(sensorEvent.values[2]);
    			orientXValue.setText(oXVal);
    			orientYValue.setText(oYVal);
    			orientZValue.setText(oZVal);
    			
    			try {
        			object.put("oX", oXVal);
        			object.put("oY", oYVal);
        			object.put("oZ", oZVal);
        			
        		} catch (JSONException e) {
        			e.printStackTrace();
        		}
    		}
    	}
    }

    public void onAccuracyChanged(Sensor arg0, int arg1) {
    	// no op
    }
    // end of SensorEventListener methods
    
    
    
    
    public class Sender extends Thread {
    	private static final String TAG = "Sending";
    	private static final int PORT = 12346;
    	private static final int TIMEOUT_MS = 500;
    	private static final int BUF_SIZE = 1024;
    	private WifiManager mWifi;
    	
    	DatagramSocket socket;
    	
    	public Sender(WifiManager wifi) {
    		mWifi = wifi;
    	}
    	
    	private volatile Thread runner;

    	public synchronized void startThread(){
    		if(runner == null){
    			runner = new Thread(this);
    			runner.start();
    		}
    	}

    	public synchronized void stopThread(){
    		if(runner != null){
    			Thread moribund = runner;
    			runner = null;
    			moribund.interrupt();
    			socket.close();
    		}
    	}
    	
    	public void run() {
    		while (Thread.currentThread() == runner) {
    			try {
    				socket = new DatagramSocket(PORT); // TO FIX: don't create socket every single time
    				socket.setBroadcast(true);
    				socket.setSoTimeout(TIMEOUT_MS);
        			sendData(socket);
        			socket.close();
        			Thread.sleep(250);  // TO FIX: stopping the thread throws can't sleep exception (?)
        		} catch (IOException ioe) {
        			Log.e(TAG, "Couldn't send data", ioe);
        		} catch (InterruptedException ie) {
    				Log.e(TAG,  "Can't sleep", ie);
    			}
    		}
    	}
    	
    	private void sendData(DatagramSocket socket) throws IOException {
    		byte[] buf = new byte[BUF_SIZE];
    		buf = object.toString().getBytes();
    		InetAddress addr = InetAddress.getByName("192.168.0.255"); // TO FIX: use getBroadcastAddress below
    		//InetAddress addr = getBroadcastAddress();
    		DatagramPacket packet = new DatagramPacket(buf, buf.length, addr, PORT);
    		socket.send(packet);
    	}
    	
    	private InetAddress getBroadcastAddress() throws IOException {
    	    DhcpInfo dhcp = mWifi.getDhcpInfo();
    	    if (dhcp == null) {
    	      Log.d(TAG, "Could not get dhcp info");
    	      return null;
    	    }

    	    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
    	    byte[] quads = new byte[4];
    	    for (int k = 0; k < 4; k++)
    	      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
    	    return InetAddress.getByAddress(quads);
    	}
    	
    }
    
    
    
    

    
    
    
    @Override
    protected void onResume() {
    	super.onResume();
    	// Register this class as a listener for the accelerometer sensor
    	sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
    		SensorManager.SENSOR_DELAY_NORMAL);
    	// ...and the orientation sensor
    	sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
    		SensorManager.SENSOR_DELAY_NORMAL);
     
    	lm.requestLocationUpdates(
    		LocationManager.GPS_PROVIDER, 
    		0, 
    		0, 
    		this);
    	
        sd.startThread();
        
    }
    
	@Override
    protected void onStop() {
    	// Unregister the listener
    	sensorManager.unregisterListener((SensorEventListener) this);
    	lm.removeUpdates(this);
    	sd.stopThread();
    	
    	// Reset GPS figures
    	gpsLatVal = Float.toString((float) 0.00);
    	gpsLonVal = Float.toString((float) 0.00);
    	try {
			object.put("Lat", gpsLatVal);
			object.put("Lon", gpsLonVal);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	gpsLat.setText(gpsLatVal);
        gpsLon.setText(gpsLonVal);
    	
    	super.onStop();
    } 
    
}