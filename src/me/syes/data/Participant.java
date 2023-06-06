package me.syes.data;

import java.util.ArrayList;

public class Participant {

	private ArrayList<Lap> laps;
	private String name;
	private int teamId;
	private String nationality;
	private boolean isAi;

	private int playerIndex;

	public Participant(ArrayList<Lap> laps, String name, int teamId, String nationality, boolean isAi, int playerIndex) {
		this.laps = laps;
		this.name = name;
		this.teamId = teamId;
		this.nationality = nationality;
		this.isAi = isAi;
		this.playerIndex = playerIndex;
	}

	public ArrayList<Lap> getLaps() {
		return laps;
	}

	public String getName() {
		return name;
	}

	public int getTeamId() {
		return teamId;
	}

	public String getNationality() {
		return nationality;
	}

	public boolean isAi() {
		return isAi;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}
}
