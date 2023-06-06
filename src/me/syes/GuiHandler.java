package me.syes;

import javax.swing.*;
import java.awt.*;

public class GuiHandler {

	public GUIState guiState;
	public int selectedRace, selectedParticipant, selectedLap;
	public int selectedComparisonParticipant, selectedComparisonLap;
	public int currentScroll;
/*
	HOME GUI:
	show all races (date + trackname + race type)
	*/

	/*
	RACE GUI:
	show all participants for chosen race
	 */

	/*
	PARTICIPANT GUI:
	show all laps for chosen participant
	 */

	/*
	LAPS GUI:
	show lap telemetry for chosen lap
	 */

	public GuiHandler(){
		JFrame jFrame = new JFrame();
		JPanel jPanel = new Panel(this);
		jFrame.setAutoRequestFocus(true);
//		jFrame.setSize(1280, 720);
		jFrame.setSize(1920, 1080);
		jFrame.setResizable(false);
		jPanel.setBackground(new Color(0x282828));
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("resources/app_logo.png");
		jFrame.setTitle("F1 Data");
		jFrame.setIconImage(img.getImage());
		jFrame.add(jPanel);
		jFrame.pack();
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		guiState = GUIState.HOME;
	}

	public void addCurrentScroll(int currentScroll) {
		if(this.currentScroll+currentScroll < 0){
			this.currentScroll=0;
			return;
		}
		this.currentScroll += currentScroll;
	}

}
