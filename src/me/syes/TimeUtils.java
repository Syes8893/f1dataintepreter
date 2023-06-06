package me.syes;

import java.time.Duration;

public class TimeUtils {

	public static String millisToFormat(int timeMillis){
		int minutes = timeMillis/60000;
		timeMillis -= minutes*60000;
		int seconds = timeMillis/1000;
		timeMillis -= seconds*1000;
		return String.format("%01d:%02d.%03d", minutes, seconds, timeMillis);
	}

}
