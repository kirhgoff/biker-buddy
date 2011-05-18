package org.foodobjects.bikerbuddy;

public class Quaternion {
	double x;
	double y;
	double z;
	long time;
	
	public Quaternion(double x, double y, double z, long t) {
		this.x = x;
		this.y = y;
		this.z = z;
		time = t;
	}

	public long getTime() {
		return time;
	}

	public double getX() {
		return x;
	}
	
	@Override
	public String toString() {
		return time + ", " + x + ", " + y + ", " + z;
	}
}
