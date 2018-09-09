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

	public static void inititalize() {
		System.out.println(BWTA.getBaseLocations().size());
		firstPosition();

		localizeOthers();

	}

	private static void firstPosition() {
		ourBasePosition.put("Main_Base", new APosition(AGame.getPlayerUs().getStartLocation().toPosition()));
	}

	private static void localizeOthers() {
		BaseLocation closest = null;
		ourBasePosition.
		for (BaseLocation base : BWTA.getBaseLocations()) {

			if (!ourBasePosition.containsValue(new APosition(base.getPosition())) && !base.isIsland()
					&& (closest == null || base.getGroundDistance(closest) < base.getGroundDistance(lastBase)))
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
