package org.foodobjects.bikerbuddy;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {
	 private GraphView view = null;
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//showLoginScreen();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (view == null) {
        	view = new GraphView(this);
        }
        view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        setContentView(view);		
	}

	private void showLoginScreen() {
		setContentView(R.layout.main);
		setUpOrientationListener();
		setUpGPSListener();

		Button startButton = (Button) findViewById(R.id.StartButton);
		startButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("BIKE", "Start button was pressed");
			}
		});

		Button stopButton = (Button) findViewById(R.id.StopButton);
		stopButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i("BIKE", "Stop button was pressed");
			}
		});

	}

	private void setUpOrientationListener() {
		SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		if (sensors.size() == 0) {
			Log.e("BIKE", "No ORIENTATION Sensor");
			// Toast.makeText(this, "No ORIENTATION Sensor",
			// Toast.LENGTH_LONG).show();
			finish();
		} else {
			sensorManager.registerListener(new SensorEventListener() {
				public void onSensorChanged(SensorEvent event) {
					// TODO Engine.setOrientation(event.values[0]);
					Log.i("BIKE", "Orientation sensor has changed: " + Arrays.asList(event.values));
				}

				public void onAccuracyChanged(Sensor sensor, int accuracy) {
					Log.i("BIKE", "Accuracy has changed: " + accuracy);
				}
			}, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	private void setUpGPSListener() {
		// Could be long running operation, run in the background
		// Probably change to AsyncTask
		// Runnable runnable = new Runnable () {
		// public void run() {
		LocationManager locationManager = (LocationManager) MainActivity.this
				.getSystemService(Context.LOCATION_SERVICE);
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				float speed = location.getSpeed();
				// TODO set location
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		// };
		// };
		// new Thread (runnable).start ();
	}
}
