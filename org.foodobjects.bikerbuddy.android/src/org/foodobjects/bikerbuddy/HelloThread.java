package org.foodobjects.bikerbuddy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

class HelloThread extends Thread {
	private int x = 0;
	private int y = 0;
	private int keyCode = -1;
	private SurfaceHolder surfaceHolder;
	private boolean run = false;

	public HelloThread(SurfaceHolder holder, Context context, Handler handler) {
		surfaceHolder = holder;
	}
	public void setRunning(boolean b) {
		run = b;
	}
	public void doKeyDown(int keyCode, KeyEvent msg) {
		this.keyCode = keyCode;
	}
	public void doKeyUp(int keyCode, KeyEvent msg) {
		this.keyCode = -1;
	}
	public void run() {
		int fps = 0;
		long currentMillis = System.currentTimeMillis();
		boolean shouldDraw = true;
		FPSTimer timer = new FPSTimer(60);
		while (run) {
			Canvas c = null;
			if (shouldDraw) {
				try {
					c = surfaceHolder.lockCanvas(null);
					synchronized (surfaceHolder) {
						doDraw(c);
					}
					fps++;
				} finally {
					if (c != null)
						surfaceHolder.unlockCanvasAndPost(c);
				}
			}
			shouldDraw = timer.shouldDraw();
			long now = System.currentTimeMillis();
			if (now - currentMillis > 1000) {
				Log.d("KZK", "FPS=" + (fps * 1000 / ((double)now - currentMillis)));
				fps = 0;
				currentMillis = now;
			}
		}
	}

	protected void doDraw(Canvas canvas) {
		Paint paint = new Paint();
		//paint.setAntiAlias(true);
		//canvas.drawBitmap(image, 0, 0, null);

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) y--;
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) y++;
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) x--;
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) x++;
		paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(255,0,0,255));
        canvas.drawRect(new Rect(x+0,y+0,x+40,y+40),paint);

		String str = "";
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) str = "DPAD_UP";
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) str = "DPAD_DOWN";
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) str = "DPAD_LEFT";
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) str = "DPAD_RIGHT";
		canvas.drawText("keyCode>" + keyCode + " " + str, 0, 40, paint);
	}
}