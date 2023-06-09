package me.syes;

import me.syes.data.Lap;
import me.syes.data.Participant;
import me.syes.data.Race;
import me.syes.data.Telemetry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DataHandler {

	public ArrayList<Race> races;

	public DataHandler() throws SQLException {
		races = new ArrayList<>();
//		loadTestData();
		loadData();
	}

	private void loadTestData(){
		ArrayList<Telemetry> telemetry = new ArrayList();
		telemetry.add(new Telemetry(100, 200, 0, 0, false, new Random().nextInt(51) -50));
		for(int i = 100; i < 140000; i+=100) {
			telemetry.add(new Telemetry(i+100, 200, telemetry.get(i/100-1).getThrottle() + new Random().nextInt(3) -1, telemetry.get(i/100-1).getBrake() + new Random().nextInt(3) -1, false, new Random().nextInt(51) -50));
		}
		ArrayList<Telemetry> telemetry2 = new ArrayList();
		telemetry2.add(new Telemetry(100, 100, 100, 0, false, new Random().nextInt(51) -50));
		for(int i = 100; i < 180000; i+=100) {
			telemetry2.add(new Telemetry(i, 100, 100, telemetry2.get(i/100-1).getBrake() + new Random().nextInt(3) -1, false, new Random().nextInt(51) -50));
		}
		ArrayList<Lap> laps = new ArrayList();
		laps.add(new Lap(telemetry, 1, 100000, 30000, 30000, 40000, true, 100));
		laps.add(new Lap(telemetry2, 2, 1200, 350, 300, 550, false, 100));
		ArrayList<Lap> laps2 = new ArrayList();
		laps2.add(new Lap(telemetry2, 34, 100000, 42300, 21300, 34500, true, 100));
		ArrayList<Participant> list = new ArrayList();
		list.add(new Participant(laps, "Petre", 0, "Romanian", false, 0));
		list.add(new Participant(laps2, "Terlouw", 1, "Dutch", false, 1));
		list.add(new Participant(laps2, "Norris", 2, "British", false, 2));
		list.add(new Participant(laps2, "Vettel", 3, "German", false, 3));
		list.add(new Participant(laps2, "Verstappen", 4, "Dutch", false, 4));
		list.add(new Participant(laps2, "Verstappen", 5, "Dutch", false, 5));
		list.add(new Participant(laps2, "Verstappen", 6, "Dutch", false, 6));
		list.add(new Participant(laps2, "Verstappen", 7, "Dutch", false, 7));
		list.add(new Participant(laps2, "Verstappen", 8, "Dutch", false, 8));
		list.add(new Participant(laps2, "Verstappen", 9, "Dutch", false, 9));
		races.add(new Race(list, "Short P", "Canada", "11-09-2023",1));
		races.add(new Race(null, "R", "Silverstone", "11-09-2023", 3));
		races.add(new Race(null, "SQ", "Bahrain", "11-09-2023", 5));
		races.add(new Race(null, "R", "Spain", "03-04-2023", 2));
		races.add(new Race(null, "OSQ", "Baku", "23-05-2023",4));
		races.add(new Race(null, "OSQ", "Australia", "23-05-2023",5));
	}

	private void loadData() throws SQLException {
		Connection connection = DriverManager.getConnection(
				"jdbc:mariadb://192.168.1.205:3306/F1",
				"user", "password"
		);
		ResultSet rs = connection.createStatement().executeQuery("SELECT date, sessiontype, trackname, sessionUid FROM session;");
		while(rs.next())
			races.add(new Race(new ArrayList<>(), rs.getString(2), rs.getString(3), rs.getDate(1).toString(), rs.getLong(4)));
		ResultSet rs2 = connection.createStatement().executeQuery("SELECT sessionUid, name, teamID, ai, playerIndex FROM participants ORDER BY teamID;");
		while(rs2.next()){
			for(Race r : races){
				if(rs2.getLong(1) != r.getSessionUID())
					continue;
				r.getParticipants().add(new Participant(new ArrayList<>(), rs2.getString(2), rs2.getInt(3), "null", rs2.getBoolean(4), rs2.getInt(5)));
			}
		}
		ResultSet rs3 = connection.createStatement().executeQuery("SELECT sessionUid, playerIndex, lapnumber, laptime, s1time, s2time, lapvalid, lapstarttime FROM lapdata;");
		while(rs3.next()){
			for(Race r : races){
				if(rs3.getLong(1) != r.getSessionUID())
					continue;
				for(Participant p : r.getParticipants()){
					if(rs3.getInt(2) != p.getPlayerIndex())
						continue;
					p.getLaps().add(new Lap(new ArrayList<>(), rs3.getInt(3), rs3.getInt(4), rs3.getInt(5), rs3.getInt(6)
							, rs3.getInt(4)-rs3.getInt(5)-rs3.getInt(6), rs3.getBoolean(7), rs3.getInt(8)));
				}
			}
		}
		ResultSet rs4 = connection.createStatement().executeQuery("SELECT sessionUid, playerIndex, sessiontime, speed, throttle, brake, steer FROM telemetry ORDER BY sessiontime ASC;");
		while(rs4.next()){
			for(Race r : races){
				if(rs4.getLong(1) != r.getSessionUID())
					continue;
				for(Participant p : r.getParticipants()){
					if(rs4.getInt(2) != p.getPlayerIndex())
						continue;
					for(Lap lap : p.getLaps()){
						if(rs4.getInt(3) < lap.getLapStartSessionTime() || rs4.getInt(3) > lap.getLapEndSessionTime())
							continue;
						lap.getTelemetry().add(new Telemetry(rs4.getInt(3), rs4.getInt(4), rs4.getFloat(5)*100, rs4.getFloat(6)*100
								, false, (int) rs4.getFloat(7)*100));
					}
				}
			}
		}
		connection.close();
	}


}
