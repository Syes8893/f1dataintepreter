package me.syes;

import java.sql.SQLException;
import java.util.Scanner;

public class App {

	public static DataHandler dataHandler;
	private GuiHandler guiHandler;

	public App() throws SQLException {
		System.out.println("[F1-DATA] Initialising startup...");
		System.out.println("[F1-DATA] Loading data...");
		long current = System.currentTimeMillis();
		dataHandler = new DataHandler();
		System.out.println("[F1-DATA] Data successfully loaded! (took " + (System.currentTimeMillis()-current) + "ms)");
		System.out.println("[F1-DATA] Starting app...");
		guiHandler = new GuiHandler();
		System.out.println("[F1-DATA] App successfully started!");

		//TODO:
		//- connect to database
		//- load data
		//- open UI
	}



}
