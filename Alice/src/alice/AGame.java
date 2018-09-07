package alice;

import alice.units.AUnit;
import alice.units.AUnitType;
import bwapi.Player;
import bwapi.Race;

/**
 * Repräsentiert verschiedene Aspekte aus dem Spiel wie Spielzeit, Supply,
 * Spielgeschwindigkeit, Gegnerischer Spieler etc.
 * 
 * @author Lasse
 *
 */
public class AGame {

	private static double supplyTotal; // Maximale Versorgung (200)
	private static double supplyUsed;// Versorgung in Benutzung (200)

	/**
	 * Number of supply totally available.
	 */
	public static double getSupplyTotal() {
		return supplyTotal;
	}

	public static int getMinerals() {
		return getPlayerUs().minerals();
	}

	public static int getGas() {
		return getPlayerUs().gas();
	}

	/**
	 * Added das verbrauchte oder zur Verfügung gestellte Supply dem supplyUsed oder
	 * supplyTotal
	 * 
	 * @param ut
	 */
	public static void increaseSupplyUsed(AUnitType ut) {

		if (ut.supplyRequired() > 0)
			AGame.increaseSupplyUsed(ut.supplyRequired());

		if (ut.supplyProvided() > 0)
			AGame.increaseSupplyTotal(ut.supplyProvided());
	}

	public static void increaseSupplyUsed(int increase) {
		supplyUsed += increase;
	}

	public static void increaseSupplyTotal(int increase) {
		supplyTotal += increase;
	}

	/**
	 * Returns true if user plays as Terran.
	 */
	public static boolean playsAsTerran() {
		return AliceConfig.MY_RACE.equals(Race.Terran);
	}

	/**
	 * Returns true if user plays as Zerg.
	 */
	public static boolean playsAsZerg() {
		return AliceConfig.MY_RACE.equals(Race.Zerg);
	}

	public static double getSupplyUsed() {
		return supplyUsed;
	}

	public static double getSupplyFree() {
		return supplyTotal - supplyUsed;
	}

	/**
	 * Returns current player.
	 */
	public static Player getPlayerUs() {
		return Alice.getBwapi().self();
	}

	public static Player getPlayerEnemy() {
		return Alice.getBwapi().enemy();
	}

	public static Player getPlayerNeutral() {
		return Alice.getBwapi().neutral();
	}
}
