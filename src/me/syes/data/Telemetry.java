package me.syes.data;

import java.awt.*;

public class Telemetry {

	private int sessionTime;
	private int speed;
	private int throttle, brake;
	private boolean drs;
	private int steer;

	public Telemetry(int sessionTime, int speed, float throttle, float brake, boolean drs, int steer) {
		this.sessionTime = sessionTime;
		this.speed = speed;
		this.throttle = (int) throttle;
		this.brake = (int) brake;
		this.drs = drs;
		this.steer = steer;
	}

	public int getSessionTime() {
		return sessionTime;
	}

	public int getSpeed() {
		return speed;
	}

	public int getThrottle() {
		return throttle;
	}

	public int getBrake() {
		return brake;
	}

	public boolean isDrs() {
		return drs;
	}

	public int getSteer() {
		return steer;
	}

}
