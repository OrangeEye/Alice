package alice.position;

import java.util.ArrayList;
import java.util.HashMap;

import alice.AGame;
import bwta.*;

/**
 * Stores information about the Map
 * 
 * @author Lasse
 *
 */
public class AMap {

	private static HashMap<String, APosition> ourBasePosition = new HashMap<String, APosition>();
	private static BaseLocation mainBaseLocation;

	public static void inititalize() {
		firstPosition();
		localizeOthers();

	}

	
	/**
	 * Setzt die Mainbase an Position 1 und als lastBaseLocation für localizeOthers
	 */
	private static void firstPosition() {
		mainBaseLocation = BWTA.getStartLocation(AGame.getPlayerUs());
		ourBasePosition.put("Main_Base", new APosition(mainBaseLocation.getPosition()));
		
		/*
		System.out.println(ourBasePosition.get("Main_Base").toString());
		for (BaseLocation base : BWTA.getBaseLocations()) {
			System.out.println(base.getPosition());
			if (ourBasePosition.containsValue(new APosition(base.getPosition()))) {
				lastBaseLocation = base;
				break;
			}
		} */

	}

	private static void localizeOthers() {
		BaseLocation closest = null;
		for (BaseLocation base : BWTA.getBaseLocations()) {
			System.out.println("Distance main base " + base.getGroundDistance(mainBaseLocation));
			if (closest != null) {
				System.out.println("Distance closest base " + closest.getGroundDistance(mainBaseLocation));
			}
			
			if (!ourBasePosition.containsValue(new APosition(base.getPosition())) && !base.isIsland()
					&& (closest == null || mainBaseLocation.getGroundDistance(base) < mainBaseLocation.getGroundDistance(closest)))
				closest = base;

		}

		if (closest != null) {
			addBaseLocation(new APosition(closest.getPosition()));
			localizeOthers();
		}

	}

	private static void addBaseLocation(APosition position) {

		switch (ourBasePosition.size()) {
		case 1:
			ourBasePosition.put("2_Base", position);
			break;

		case 2:
			ourBasePosition.put("3_Base", position);
			break;

		case 3:
			ourBasePosition.put("4_Base", position);
			break;

		case 4:
			ourBasePosition.put("5_Base", position);
			break;

		case 5:
			ourBasePosition.put("6_Base", position);
			break;

		case 6:
			ourBasePosition.put("7_Base", position);
			break;

		}
	}

	public static HashMap<String, APosition> getOurBasePosition() {
		return ourBasePosition;
	}

}
