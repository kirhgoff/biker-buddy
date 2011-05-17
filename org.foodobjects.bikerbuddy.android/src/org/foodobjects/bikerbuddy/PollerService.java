package org.foodobjects.bikerbuddy;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PollerService extends Service {
	public static int interval = 30000;
	private Timer timer = new Timer();

	@Override
	public void onCreate() {
		super.onCreate();
		registerTimer();
	}

	public void registerTimer() {
		if (interval <= 0) {
			throw new RuntimeException("Service interval less or equal than 0");
		}

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				//poll
			}
		}, 0, interval);
	}
	
	@Override
	public void onDestroy() {
		timer.cancel();
		timer.purge();		
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	};
}
