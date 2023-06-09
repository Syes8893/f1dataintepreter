package me.syes.data;

import java.util.ArrayList;
import java.util.Date;

public class Race {

	private ArrayList<Participant> participants;
	private String sessionType;
	private String trackName;
	private String date;

	private long sessionUID;

	public Race(ArrayList<Participant> participants, String sessionType, String trackName, String date, long sessionUID){
		this.participants = participants;
		this.sessionType = sessionType;
		this.trackName = trackName;
		this.date = date;
		this.sessionUID = sessionUID;
	}

	public ArrayList<Participant> getParticipants() {
		return participants;
	}

	public String getSessionType() {
		if(sessionType.equalsIgnoreCase("R"))
			return "Race";
		if(sessionType.equalsIgnoreCase("SHORT Q"))
			return "Short Qualifying";
		if(sessionType.equalsIgnoreCase("OSQ"))
			return "One-Shot Qualifying";
		if(sessionType.equalsIgnoreCase("Short p"))
			return "Short Practice";
		return sessionType;
	}

	public String getTrackName() {
		return trackName;
	}

	public String getDate() {
		return date;
	}

	public long getSessionUID() {
		return sessionUID;
	}
}
