package me.syes;

import java.awt.*;

public class AppendixUtils {

	public static String getTeamName(int i){
		switch(i){
			case 0:
				return "Mercedes";
			case 1:
				return "Ferrari";
			case 2:
				return "Red Bull";
			case 3:
				return "Williams";
			case 4:
				return "Aston Martin";
			case 5:
				return "Alpine";
			case 6:
				return "AlphaTauri";
			case 7:
				return "Haas";
			case 8:
				return "Mclaren";
			case 9:
				return "Alfa Romeo";
		}
		return "Unknown";
	}

	public static Color getTeamColor(int i){
		switch(i){
			case 0:
				return new Color(0x00D2BE);
			case 1:
				return new Color(0xDC0000);
			case 2:
				return new Color(0x0600EF);
			case 3:
				return new Color(0x005AFF);
			case 4:
				return new Color(0x006F62);
			case 5:
				return new Color(0x0090FF);
			case 6:
				return new Color(0x2B4562);
			case 7:
				return new Color(0xFFFFFF);
			case 8:
				return new Color(0xFF8700);
			case 9:
				return new Color(0x900000);
		}
		return new Color(0x000000);
	}

}
