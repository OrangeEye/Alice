package alice;

import alice.production.AOrder;
import alice.production.orders.AZergBuildOrder;
import alice.production.orders.AZergProduction;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;
import bwapi.Game;
import bwapi.Player;
import bwapi.Race;
import bwapi.Unit;

/**
 * Repräsentiert verschiedene Aspekte aus dem Spiel wie Spielzeit, Supply,
 * Spielgeschwindigkeit, Gegnerischer Spieler etc.
 * 
 * @author Lasse
 *
 */
public class AGame {

	private static double supplyTotal = 0; // Maximale Versorgung (200)
	private static double supplyUsed = 0;// Versorgung in Benutzung (200)

	/**
	 * Number of supply totally available.
	 */

	public static double getSupplyTotal() {
		return supplyTotal;
	}

	public static int getFPS() {
		return Alice.getBwapi().getFPS();
	}

	public static double getAverageFPS() {
		return Alice.getBwapi().getAverageFPS();
	}

	public static int getMinerals() {
		return getPlayerUs().minerals();
	}

	public static int getGas() {
		return getPlayerUs().gas();
	}

	public static void updateSupply() {
		supplyUsed = 0;
		supplyTotal = 0;
		for (AUnit unit : Select.getOurUnits().values()) {
			AUnitType ut;

			if (unit.isMorphing())
				ut = unit.getBuildType();
			else
				ut = unit.getType();

			supplyUsed += ut.supplyRequired();
			if (unit.isCompleted())
				supplyTotal += ut.supplyProvided();
		}
	}

	/**
	 * Added das verbrauchte oder zur Verfügung gestellte Supply dem supplyUsed oder
	 * supplyTotal
	 * 
	 * @param ut
	 */
	/*
	 * public static void increaseSupplyUsed(AUnitType ut) {
	 * 
	 * if (ut.supplyRequired() > 0) AGame.increaseSupplyUsed(ut.supplyRequired());
	 * 
	 * if (ut.supplyProvided() > 0) AGame.increaseSupplyTotal(ut.supplyProvided());
	 * }
	 */
	/*
	 * public static void increaseSupplyUsed(int increase) { supplyUsed += increase;
	 * }
	 * 
	 * public static void increaseSupplyTotal(int increase) { supplyTotal +=
	 * increase; }
	 * 
	 * public static void decreaseSupplyUsedOrTotal(AUnitType ut) { if
	 * (ut.supplyRequired() > 0) AGame.decreaseSupplyUsed(ut.supplyRequired());
	 * 
	 * if (ut.supplyProvided() > 0) AGame.decreaseSupplyTotal(ut.supplyProvided());
	 * }
	 * 
	 * private static void decreaseSupplyUsed(int increase) { supplyUsed -=
	 * increase; }
	 * 
	 * private static void decreaseSupplyTotal(int increase) { supplyTotal -=
	 * increase; }
	 */
	public static void sendText() {
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
	 * onUnitMorph und onUnitComplete wird beides beim fertigstellen einer Zerg
	 * Einheit aufgerufen.
	 * 
	 * @param morphedUnit
	 */
	public static void handleOnUnitMorphOrComplete(AUnit morphedUnit) {
		AUnitType morphedType; // Das Gebäude oder die Einheit, die gerade gemorphed wird
		if (morphedUnit.isType(AUnitType.Zerg_Egg))
			morphedType = morphedUnit.getBuildType();
		else
			morphedType = morphedUnit.getType();

		// if (morphedType.supplyRequired() > 0)
		// increaseSupplyUsed(morphedType.supplyRequired());

		// AZergProduction.decreaseReservedRessources(morphedType);
		if (morphedUnit.isCompleted())
			AZergBuildOrder.setOrderStatus(morphedType, morphedUnit, AOrder.STATUS_FINISHED);
		else
			AZergBuildOrder.setOrderStatus(morphedType, morphedUnit, AOrder.STATUS_IN_PROCESS);

		/*
		 * if (morphedUnit.isType(AUnitType.Resource_Vespene_Geyser)) {
		 * Select.removeVespeneGeyser(morphedUnit.getPosition()); }
		 */

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
