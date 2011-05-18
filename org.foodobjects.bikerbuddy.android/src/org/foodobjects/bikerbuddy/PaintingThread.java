package org.foodobjects.bikerbuddy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

class PaintingThread extends Thread {
	private int x = 0;
	private int y = 0;
	private int keyCode = -1;
	private SurfaceHolder surfaceHolder;
	private boolean run = false;
	
	private long minTime = 0;
	private long maxTime = 5000;
	private double minX = Double.MAX_VALUE;
	private double maxX = Double.MIN_VALUE;
	private List<Quaternion> data = fillRandomQuaternions ();
	private boolean flag = false;
	
	public PaintingThread(SurfaceHolder holder, Context context, Handler handler) {
		surfaceHolder = holder;
	}
	
	private List<Quaternion> fillRandomQuaternions() {
		Log.i ("BIKE", "Calculating data");
		List<Quaternion> data = new ArrayList<Quaternion> (500);
		for (int i = 0; i < 5000; i += 100) {
			data.add (new Quaternion (((double)i)*i/10000, Math.sqrt (i), i*Math.tan(i), i));
		}
		
		for (int i = 0; i < data.size(); i++) {
			Quaternion quaternion = data.get(i);
			if (minTime > quaternion.getTime()) {
				minTime = quaternion.getTime();
			}
			if (maxTime < quaternion.getTime()) {
				maxTime = quaternion.getTime();
			}

			if (minX > quaternion.getX()) {
				minX = quaternion.getX();
			}
			if (maxX < quaternion.getX()) {
				maxX = quaternion.getX();
			}
		}
		return data;
	}
	
	public void setRunning(boolean b) {
		run = b;
	}
	public void doKeyDown(int keyCode, KeyEvent msg) {
		this.keyCode = keyCode;
	}
	public void doKeyUp(int keyCode, KeyEvent msg) {
		if (this.keyCode == keyCode) {
			this.keyCode = -1;
		}
	}
	public void run() {
		int fps = 0;
		long currentMillis = System.currentTimeMillis();
		boolean shouldDraw = true;
		FPSTimer timer = new FPSTimer(60);
		while (run) {
			Canvas canvas = null;
			if (shouldDraw) {
				try {
					canvas = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						doDraw(canvas);
					}
					fps++;
				} finally {
					if (canvas != null)
						surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
			shouldDraw = timer.shouldDraw();
			long now = System.currentTimeMillis();
			if (now - currentMillis > 1000) {
				//Log.d("BIKE", "FPS=" + (fps * 1000 / ((double)now - currentMillis)));
				fps = 0;
				currentMillis = now;
			}
		}
	}

	protected void doDraw(Canvas canvas) {
		Paint paint = new Paint();
		//paint.setAntiAlias(true);
		//canvas.drawBitmap(image, 0, 0, null);
		paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255,0,0,0));
        int width = canvas.getWidth();
		int height = canvas.getHeight();
		canvas.drawRect(new Rect(0,0,width,height),paint);
        
		double timeRatio = ((double) width/ (maxTime - minTime));
		double xRatio = ((double) height)/ (maxX - minX);
		if (!flag) {
			Log.i ("BIKE", "minTime=" + minTime + ", maxTime=" + maxTime);
			Log.i ("BIKE", "minX=" + minX + ", maxX=" + maxX);		
			Log.i ("BIKE", "width=" + width + ", height=" + height);			
			Log.i ("BIKE", "Ratio  time=" + timeRatio + ", x=" + xRatio);
		}
	
		for (Iterator<Quaternion> iterator = data.iterator(); iterator.hasNext();) {
			Quaternion quaternion = iterator.next();
			
			long time = quaternion.getTime ();
			double x = quaternion.getX ();
			long screenX = Math.round(time * timeRatio);
			long screenY = Math.round((x - minX) * xRatio);
			if (!flag) {
				Log.i ("BIKE", "Drawing: quanternion=[" + quaternion + "] screen=("+ screenX + ", "  + screenY + ")");
			}

			paint.setColor(Color.argb(255,128,96,0));
			canvas.drawCircle(screenX, screenY, 5, paint);
		}
		flag = true;
	}
}