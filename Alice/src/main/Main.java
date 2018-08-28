package main;

import alice.Alice;
import alice.util.ProcessHelper;

public class Main {

	public static void main(String[] args) {
		
		//Beendet bestehende StarCraft Prozesse
		ProcessHelper.killStarcraftProcess();
		
		//Beendet bestehende ChaosLauncher Prozesse
	//	ProcessHelper.killChaosLauncherProcess();

		//Startet ChaosLauncher
	//	ProcessHelper.startChaosLauncherProcess();
		
		Alice alice = new Alice();
		
		alice.run();
	}

}
