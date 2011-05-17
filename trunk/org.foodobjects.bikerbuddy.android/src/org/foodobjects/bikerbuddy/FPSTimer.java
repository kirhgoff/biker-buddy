package org.foodobjects.bikerbuddy;

class FPSTimer {
	private int fps;
	private double secPerFrame;
	private double secTiming;
	private long currentMillis;

	public FPSTimer(int fps) {
		this.fps = fps;
		reset();
	}

	public void reset() {
		secPerFrame = 1.0 / fps;
		currentMillis = System.currentTimeMillis();
		secTiming = 0.0;
	}

	public boolean shouldDraw() {
		long next = System.currentTimeMillis();
		long timeSpent = next - currentMillis;
		currentMillis = next;
		
		secTiming += (timeSpent / 1000.0);
		secTiming -= secPerFrame;
		if (secTiming > 0) {
			if (secTiming > secPerFrame) {
				reset();
				return true; // force redraw
			}
			return false;
		}
		try {
			Thread.sleep((long) (-secTiming * 1000.0));
		} catch (InterruptedException e) {
		}
		return true;
	}
}