package me.syes;

import me.syes.data.Lap;
import me.syes.data.Participant;
import me.syes.data.Race;
import me.syes.data.Telemetry;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Panel extends JPanel {

	public GuiHandler guiHandler;

	public Panel(GuiHandler guiHandler){
		this.guiHandler = guiHandler;
		addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
//				if(guiHandler.guiState != GUIState.LAPS)
					guiHandler.addCurrentScroll(e.getWheelRotation()*15);
				repaint(0, 0, 1280, 720);
				//check what guistate is active and for said guistate check what selection is made
			}
		});

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(e.getY() < 100 && e.getX() < getWidth()/2 && (guiHandler.guiState == GUIState.LAPS || guiHandler.guiState == GUIState.COMPARISONLAPS)){
					guiHandler.guiState = GUIState.PARTICIPANT;
					guiHandler.currentScroll = 0;
					repaint();
					return;
				}else if(e.getY() < 100 && e.getX() >= getWidth()/2 && guiHandler.guiState == GUIState.LAPS){
					guiHandler.guiState = GUIState.COMPARISONRACE;
					guiHandler.currentScroll = 0;
					repaint();
					return;
				}
				int yOffset = (e.getY()-100+guiHandler.currentScroll)%55;
				if(yOffset > 40)
					return;
				if(yOffset < 0){
					switch(guiHandler.guiState){
						case RACE -> guiHandler.guiState = GUIState.HOME;
						case PARTICIPANT -> guiHandler.guiState = GUIState.RACE;
						case COMPARISONPARTICIPANT -> guiHandler.guiState = GUIState.COMPARISONRACE;
					}
					repaint();
					return;
				}
				int yIndex = (e.getY()-100+guiHandler.currentScroll-yOffset)/55;
				//yIndex is the index of the race in the arraylist in the datahandler class
				if(guiHandler.guiState == GUIState.HOME && App.dataHandler.races.size() > yIndex){
					guiHandler.selectedRace = yIndex;
					guiHandler.guiState = GUIState.RACE;
					guiHandler.currentScroll = 0;
				}
				else if(guiHandler.guiState == GUIState.RACE && App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().size() > yIndex){
					guiHandler.selectedParticipant = yIndex;
					guiHandler.guiState = GUIState.PARTICIPANT;
					guiHandler.currentScroll = 0;
				}
				else if(guiHandler.guiState == GUIState.PARTICIPANT && App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getLaps().size() > yIndex){
					guiHandler.currentScroll = 0;
					guiHandler.selectedLap = yIndex;
					guiHandler.guiState = GUIState.LAPS;
					guiHandler.currentScroll = 0;
				}
				else if(guiHandler.guiState == GUIState.COMPARISONRACE && App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().size() > yIndex){
					guiHandler.selectedComparisonParticipant = yIndex;
					guiHandler.guiState = GUIState.COMPARISONPARTICIPANT;
					guiHandler.currentScroll = 0;
				}
				else if(guiHandler.guiState == GUIState.COMPARISONPARTICIPANT && App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getLaps().size() > yIndex){
					guiHandler.selectedComparisonLap = yIndex;
					guiHandler.guiState = GUIState.COMPARISONLAPS;
					guiHandler.currentScroll = 0;
				}
				repaint();
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
			}
		});
	}

	public Dimension getPreferredSize() {
		return new Dimension(1280, 720);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		((Graphics2D)g).setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Font font = new Font("Bebas Kai", Font.BOLD, 20);
		g.setFont(font);

		if(guiHandler.guiState == GUIState.HOME){
			for(Race race: App.dataHandler.races){
				g.setColor(new Color(0x379626));
				g.fillRect(63, 103 + 55*App.dataHandler.races.indexOf(race) - guiHandler.currentScroll, 1160, 40);
				g.setColor(new Color(0x27661b));
				g.fillRect(60, 100 + 55*App.dataHandler.races.indexOf(race) - guiHandler.currentScroll, 1160, 40);
				g.setColor(new Color(0xc2c2c2));
				g.drawString("[" + race.getDate() + "] " + race.getSessionType() + ": " + race.getTrackName(), 70, 127 + 55*App.dataHandler.races.indexOf(race) - guiHandler.currentScroll);
			}
		}
		else if(guiHandler.guiState == GUIState.RACE){
			ArrayList<Participant> participants = App.dataHandler.races.get(guiHandler.selectedRace).getParticipants();
			for(Participant participant : participants){
//				g.setColor(new Color(0x32a0a8));
				g.setColor(AppendixUtils.getTeamColor(participant.getTeamId()));
				g.fillRect(63, 103 + 55*participants.indexOf(participant) - guiHandler.currentScroll, 1160, 40);
//				g.setColor(new Color(0x16494d));
				g.setColor(AppendixUtils.getTeamColor(participant.getTeamId()).darker());
				g.fillRect(60, 100 + 55*participants.indexOf(participant) - guiHandler.currentScroll, 1160, 40);
				g.setColor(new Color(0xc2c2c2));
				int logowidth = 0;
				try {
					BufferedImage bufferedImage = ImageIO.read(new File("resources/" + AppendixUtils.getTeamName(participant.getTeamId()).toLowerCase().replace(" ", "_") + "_logo.png"));
					((Graphics2D) g).drawImage(bufferedImage, 70, 120 + 55*participants.indexOf(participant) - guiHandler.currentScroll - bufferedImage.getHeight(null)/2, null);
					logowidth = bufferedImage.getWidth(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(participant.getTeamId() == 7)
					g.setColor(new Color(0xc2c2c2).darker().darker().darker().darker());
				g.drawString("[" + AppendixUtils.getTeamName(participant.getTeamId()) + "] " + participant.getName(), 70 + logowidth + 10, 127 + 55*participants.indexOf(participant) - guiHandler.currentScroll);
			}
		}
		else if(guiHandler.guiState == GUIState.PARTICIPANT){
			ArrayList<Lap> laps = App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getLaps();
			for(Lap lap : laps){
				if(lap.isValid())
					g.setColor(new Color(0xaa3bad));
				else
					g.setColor(new Color(0xba2323));
				g.fillRect(63, 103 + 55*laps.indexOf(lap) - guiHandler.currentScroll, 1160, 40);
				if(lap.isValid())
					g.setColor(new Color(0x5a1e5c));
				else
					g.setColor(new Color(0x8a1919));
				g.fillRect(60, 100 + 55*laps.indexOf(lap) - guiHandler.currentScroll, 1160, 40);
				g.setColor(new Color(0xc2c2c2));
				String validity = "";
				if(!lap.isValid())
					validity = "(Invalid)";
				g.drawString("[Lap " + lap.getLapNumber() + "] " + TimeUtils.millisToFormat(lap.getLaptimeMillis()) + " " + validity, 70, 127 + 55*laps.indexOf(lap) - guiHandler.currentScroll);
			}
		}
		else if(guiHandler.guiState == GUIState.LAPS){
			Lap lap = App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getLaps().get(guiHandler.selectedLap);
			lap.drawTelemetry(this, g, 0, Color.GRAY, Color.GREEN, Color.RED, App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getName());
			//draw telemetry data for selected lap
		}else if(guiHandler.guiState == GUIState.COMPARISONRACE){
			ArrayList<Participant> participants = App.dataHandler.races.get(guiHandler.selectedRace).getParticipants();
			for(Participant participant : participants){
//				g.setColor(new Color(0x32a0a8));
				g.setColor(AppendixUtils.getTeamColor(participant.getTeamId()));
				g.fillRect(63, 103 + 55*participants.indexOf(participant) - guiHandler.currentScroll, 1160, 40);
//				g.setColor(new Color(0x16494d));
				g.setColor(AppendixUtils.getTeamColor(participant.getTeamId()).darker());
				g.fillRect(60, 100 + 55*participants.indexOf(participant) - guiHandler.currentScroll, 1160, 40);
				g.setColor(new Color(0xc2c2c2));
				int logowidth = 0;
				try {
					BufferedImage bufferedImage = ImageIO.read(new File("resources/" + AppendixUtils.getTeamName(participant.getTeamId()).toLowerCase().replace(" ", "_") + "_logo.png"));
					((Graphics2D) g).drawImage(bufferedImage, 70, 120 + 55*participants.indexOf(participant) - guiHandler.currentScroll - bufferedImage.getHeight(null)/2, null);
					logowidth = bufferedImage.getWidth(null);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(participant.getTeamId() == 7)
					g.setColor(new Color(0xc2c2c2).darker().darker().darker().darker());
				g.drawString("[" + AppendixUtils.getTeamName(participant.getTeamId()) + "] " + participant.getName(), 70 + logowidth + 10, 127 + 55*participants.indexOf(participant) - guiHandler.currentScroll);
			}
		}
		else if(guiHandler.guiState == GUIState.COMPARISONPARTICIPANT){
			ArrayList<Lap> laps = App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getLaps();
			for(Lap lap : laps){
				if(lap.isValid())
					g.setColor(new Color(0xaa3bad));
				else
					g.setColor(new Color(0xba2323));
				g.fillRect(63, 103 + 55*laps.indexOf(lap) - guiHandler.currentScroll, 1160, 40);
				if(lap.isValid())
					g.setColor(new Color(0x5a1e5c));
				else
					g.setColor(new Color(0x8a1919));
				g.fillRect(60, 100 + 55*laps.indexOf(lap) - guiHandler.currentScroll, 1160, 40);
				g.setColor(new Color(0xc2c2c2));
				String validity = "";
				if(!lap.isValid())
					validity = "(Invalid)";
				g.drawString("[Lap " + lap.getLapNumber() + "] " + TimeUtils.millisToFormat(lap.getLaptimeMillis()) + " " + validity, 70, 127 + 55*laps.indexOf(lap) - guiHandler.currentScroll);
			}
		}else if(guiHandler.guiState == GUIState.COMPARISONLAPS){
			Lap lap = App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getLaps().get(guiHandler.selectedLap);
			Lap lap2 = App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getLaps().get(guiHandler.selectedComparisonLap);
			lap.drawTelemetry(this, g, -100, Color.PINK, Color.PINK, Color.PINK, App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getName());
			lap2.drawTelemetry(this, g, 0, Color.CYAN, Color.CYAN, Color.CYAN, App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getName());
			//draw telemetry data for selected lap
		}

		if(guiHandler.guiState != GUIState.HOME){
			if(guiHandler.guiState != GUIState.LAPS && guiHandler.guiState != GUIState.COMPARISONLAPS){
				g.setColor(new Color(0xa8a7a7));
				g.fillRect(63, 43 - guiHandler.currentScroll, 300, 40);
				g.setColor(new Color(0xc2c2c2));
				g.fillRect(60, 40 - guiHandler.currentScroll, 300, 40);
				g.setColor(new Color(0x333333));
				g.drawString("Go Back", 170, 67 - guiHandler.currentScroll);
			}else{
				g.setColor(new Color(0xa8a7a7));
					g.fillRect(63, 43, 300, 40);
				g.setColor(new Color(0xc2c2c2));
				g.fillRect(60, 40, 300, 40);
				g.setColor(new Color(0x333333));
				g.drawString("Go Back", 170, 67);
				if(guiHandler.guiState != GUIState.COMPARISONLAPS) {
					g.setColor(new Color(0xa8a7a7));
					g.fillRect(getWidth() - 360, 43, 300, 40);
					g.setColor(new Color(0xc2c2c2));
					g.fillRect(getWidth() - 363, 40, 300, 40);
					g.setColor(new Color(0x333333));
					g.drawString("Compare Lap", getWidth() - 275, 67);
				}
			}
		}

		g.setColor(new Color(0xc2c2c2));
		font = new Font("Bebas Kai", Font.BOLD, 50);
		g.setFont(font);
		if(guiHandler.guiState == GUIState.HOME)
			g.drawString("EVENTS", getWidth()/2 - g.getFontMetrics(font).stringWidth("EVENTS")/2, 70-guiHandler.currentScroll);
		else if(guiHandler.guiState == GUIState.RACE || guiHandler.guiState == GUIState.COMPARISONRACE)
			g.drawString(App.dataHandler.races.get(guiHandler.selectedRace).getTrackName(), getWidth()/2 - g.getFontMetrics(font).stringWidth(App.dataHandler.races.get(guiHandler.selectedRace).getTrackName())/2, 70-guiHandler.currentScroll);
		else if(guiHandler.guiState == GUIState.PARTICIPANT)
			g.drawString(App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getName(), getWidth()/2 - g.getFontMetrics(font).stringWidth(App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedParticipant).getName())/2, 70-guiHandler.currentScroll);
		else if(guiHandler.guiState == GUIState.COMPARISONPARTICIPANT)
			g.drawString(App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getName(), getWidth()/2 - g.getFontMetrics(font).stringWidth(App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getName())/2, 70-guiHandler.currentScroll);
		else if(guiHandler.guiState == GUIState.COMPARISONPARTICIPANT)
			g.drawString(App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getName(), getWidth()/2 - g.getFontMetrics(font).stringWidth(App.dataHandler.races.get(guiHandler.selectedRace).getParticipants().get(guiHandler.selectedComparisonParticipant).getName())/2, 70-guiHandler.currentScroll);
		else if(guiHandler.guiState == GUIState.LAPS || guiHandler.guiState == GUIState.COMPARISONLAPS)
			g.drawString("TELEMETRY DATA", getWidth()/2 - g.getFontMetrics(font).stringWidth("TELEMETRY DATA")/2, 70);

	}

}
