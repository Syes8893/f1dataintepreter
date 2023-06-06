package me.syes.data;

import java.awt.*;

import me.syes.App;
import me.syes.Panel;
import me.syes.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Lap {

	public ArrayList<Telemetry> telemetry;
	private int lapNumber;
	private int laptimeMillis;
	private int s1Millis, s2Millis, s3Millis;
	private boolean isValid;
	private float lapStartSessionTime, lapEndSessionTime;

	public Lap(ArrayList<Telemetry> telemetry, int lapNumber, int laptimeMillis, int s1Millis, int s2Millis, int s3Millis, boolean isValid, float lapStartSessionTime) {
		this.telemetry = telemetry;
		this.lapNumber = lapNumber;
		this.laptimeMillis = laptimeMillis;
		this.s1Millis = s1Millis;
		this.s2Millis = s2Millis;
		this.s3Millis = s3Millis;
		this.isValid = isValid;
		this.lapStartSessionTime = lapStartSessionTime;
		this.lapEndSessionTime = lapStartSessionTime + laptimeMillis;
	}

	public void drawTelemetry(Panel panel, Graphics g, int yOffset, Color speedColor, Color accentColor, Color throttleColor, Color brakeColor, String driverName){
		int center = panel.getHeight()/2;
//		g.setColor(Color.RED);
		Font org = g.getFont();
		Font font = new Font("Bebas Kai", Font.BOLD, 20);
		int previoussectorindex = 50;
		Color accentLight = new Color(accentColor.getRed(), accentColor.getBlue(), accentColor.getGreen(), 30);
		g.setColor(accentColor);
		g.drawString("Speed", 50- panel.guiHandler.currentScroll, center-1-250+30+150);
		g.drawString("Throttle", 50- panel.guiHandler.currentScroll, center-1-100+30+150);
		g.drawString("Brake", 50- panel.guiHandler.currentScroll, center-1+50+30+150);
		g.setColor(accentColor);
		g.fillRect(50 - panel.guiHandler.currentScroll-1, center-1 + 300 + yOffset, 2, 40);
		boolean s1done = false;
		boolean s2done = false;
		boolean s3done = false;
//		g.fillRect(25, center-1-50, 2, 100);
//		g.fillRect(25, center-1-200, 2, 100);
		for(Telemetry telemetryPacket : telemetry){
			if(telemetryPacket.getSessionTime() > lapStartSessionTime+laptimeMillis)
				break;
//			g.setColor(accentColor);
			int index = telemetry.indexOf(telemetryPacket);
			g.setColor(speedColor);
//			int maxSpeed = telemetry.stream().max(Comparator.comparing(v -> v.getSpeed())).get().getSpeed();
			if(telemetry.size() > index+1)
				g.drawLine(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4, (int) (center-1-100 - ((double) telemetryPacket.getSpeed()/300)*100)
						,50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+2)*4, (int) (center-1-100 - ((double) telemetry.get(index+1).getSpeed()/300)*100));
			g.fillRect(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4, (int) (center-1-100 - ((double) telemetryPacket.getSpeed()/300)*100), 1, 1);
			g.setColor(throttleColor);
			if(telemetry.size() > index+1)
				g.drawLine(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4, center-1+50 - (telemetryPacket.getThrottle())
				,50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+2)*4, center-1+50 - (telemetry.get(index+1).getThrottle()));
			g.fillRect(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4, center-1+50 - (telemetryPacket.getThrottle()), 1, 1);
			g.setColor(brakeColor);
			if(telemetry.size() > index+1)
				g.drawLine(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4, center-1+200 - (telemetryPacket.getBrake())
						,50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+2)*4, center-1+200 - (telemetry.get(index+1).getBrake()));
			g.fillRect(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4, center-1+200 - (telemetryPacket.getBrake()), 1, 1);
//			System.out.println(telemetryPacket.getSessionTime() + " // " + (lapStartSessionTime+s1Millis));
			if(telemetryPacket.getSessionTime()/100 == (int) ((lapStartSessionTime+s1Millis)/100) && s1Millis > 0 && !s1done){
				g.setColor(accentLight);
				g.fillRect(50 - panel.guiHandler.currentScroll+2, center-1 + 300 + yOffset,  (telemetry.indexOf(telemetryPacket)+1)*4 - 2, 40);
				g.setColor(accentColor);
				g.fillRect(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4 - 1, center-1 + 300 + yOffset, 2, 40);
				g.setFont(font);
				g.drawString(driverName + " (" + TimeUtils.millisToFormat(laptimeMillis) + ")", 50 + 10 - panel.guiHandler.currentScroll, center-1 + 300 + 28 + yOffset);
				g.drawString("S1 (" + TimeUtils.millisToFormat(s1Millis) + ")", 50 - panel.guiHandler.currentScroll + ((telemetry.indexOf(telemetryPacket)+1)*4)/2 - g.getFontMetrics(font).stringWidth("S1 (" + TimeUtils.millisToFormat(s2Millis) + ")")/2, center-1 + 300 + 28 + yOffset);
				previoussectorindex = 50 + ((telemetry.indexOf(telemetryPacket)+1)*4);
				g.setFont(org);
				g.setColor(Color.RED);
				s1done = true;
			}
			else if(telemetryPacket.getSessionTime()/100 == (int) ((lapStartSessionTime+s1Millis+s2Millis)/100) && s2Millis > 0 &&!s2done){
				g.setColor(accentLight);
				g.fillRect(-panel.guiHandler.currentScroll+2+previoussectorindex, center-1 + 300 + yOffset,  (telemetry.indexOf(telemetryPacket)+1)*4-previoussectorindex + 50 - 2, 40);
				g.setColor(accentColor);
				g.fillRect(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4 - 1, center-1 + 300 + yOffset, 2, 40);
				g.setFont(font);
				g.drawString("S2 (" + TimeUtils.millisToFormat(s2Millis) + ")", 50 - panel.guiHandler.currentScroll + (((telemetry.indexOf(telemetryPacket)+1)*4)+previoussectorindex)/2 - g.getFontMetrics(font).stringWidth("S2 (" + TimeUtils.millisToFormat(s2Millis) + ")")/2, center-1 + 300 + 28 + yOffset);
				previoussectorindex = 50 + ((telemetry.indexOf(telemetryPacket)+1)*4);
				g.setFont(org);
				g.setColor(Color.RED);
				s2done = true;
			}else if(telemetryPacket.getSessionTime()/100 == (int) ((lapStartSessionTime+s1Millis+s2Millis+s3Millis)/100) && laptimeMillis > s1Millis+s2Millis && s2Millis > 0 && !s3done){
				g.setColor(accentLight);
				g.fillRect(-panel.guiHandler.currentScroll+2+previoussectorindex, center-1 + 300 + yOffset,  (telemetry.indexOf(telemetryPacket)+1)*4-previoussectorindex + 50 - 2, 40);
				g.setColor(accentColor);
				g.fillRect(50 - panel.guiHandler.currentScroll + (telemetry.indexOf(telemetryPacket)+1)*4 - 1, center-1 + 300 + yOffset, 2, 40);
				g.setFont(font);
				g.drawString("S3 (" + TimeUtils.millisToFormat(s3Millis) + ")", 50 - panel.guiHandler.currentScroll + (((telemetry.indexOf(telemetryPacket)+1)*4)+previoussectorindex)/2 - g.getFontMetrics(font).stringWidth("S3 (" + TimeUtils.millisToFormat(s3Millis) + ")")/2, center-1 + 300 + 28 + yOffset);
				g.setFont(org);
				g.setColor(Color.RED);
				s3done = true;
			}
		}
	}

	public ArrayList<Telemetry> getTelemetry() {
		return telemetry;
	}

	public int getLapNumber() {
		return lapNumber;
	}

	public int getLaptimeMillis() {
		return laptimeMillis;
	}

	public int getS1Millis() {
		return s1Millis;
	}

	public int getS2Millis() {
		return s2Millis;
	}

	public int getS3Millis() {
		return s3Millis;
	}

	public boolean isValid() {
		return isValid;
	}

//	public String getValidity(){
//		if(isValid)
//			return "valid";
//		return "invalid";
//	}

	public float getLapStartSessionTime() {
		return lapStartSessionTime;
	}

	public float getLapEndSessionTime() {
		return lapEndSessionTime;
	}
}
