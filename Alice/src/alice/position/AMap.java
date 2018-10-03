package alice.position;

import java.util.ArrayList;
import java.util.HashMap;

import alice.AGame;
import alice.Alice;
import alice.graph.Graph;
import alice.graph.Node;
import alice.units.AUnit;
import alice.units.AUnitType;
import alice.units.Select;
import bwapi.TilePosition;
import bwta.*;

/**
 * Stores information about the Map
 * 
 * @author Lasse
 *
 */
public class AMap {

	// private static HashMap<String, APosition> ourBasePosition = new
	// HashMap<String, APosition>();
	private static Graph<APosition> basePositions = new Graph<APosition>();

	// Die Expansionsplätze in kürzester Reinfolge zur Mainbase
	private static HashMap<APosition, BaseLocation> baseLocations = new HashMap<APosition, BaseLocation>();
	private static APosition mainBasePosition;

	public static void inititalize() {
		addNodes();
		addEdges();

	}

	public static void update() {
		updateBasePositions();
	}

	private static void addNodes() {
		mainBasePosition = new APosition(BWTA.getStartLocation(AGame.getPlayerUs()));
		for (BaseLocation base : BWTA.getBaseLocations()) {
			if (!base.equals((BWTA.getStartLocation(AGame.getPlayerUs())))) {
				basePositions.addNode(new APosition(base));
				baseLocations.put(new APosition(base), base);
			}
		}
	}

	private static void addEdges() {

		for (APosition position1 : baseLocations.keySet()) {
			for (APosition position2 : baseLocations.keySet()) {
				if (position1.equals(position2))
					continue;

				basePositions.addEdge(position1, position2,
						baseLocations.get(position1).getGroundDistance(baseLocations.get(position2)));
			}
		}

		// Setze die freien BaseLocations ihre Bezeichnung zu
		for (APosition position : getBasePositions())
			position.setAddInfo(APosition.ADDINFO_IS_FREE_BASELOCATION);

	}

	public static ArrayList<APosition> getBasePositions() {
		return basePositions.getConnections(mainBasePosition);
	}

	public static APosition getMainBasePosition() {
		return mainBasePosition;
	}

	public static APosition getNextExpandPosition() {
		for (APosition position : getBasePositions())
			if (position.getAddInfo().equals(APosition.ADDINFO_IS_FREE_BASELOCATION))
				return position;
		return null;
	}

	private static APosition getBuildPositionCloseToR(AUnitType ut, int lastUp, int lastDown, int lastLeft,
			int lastRight, int tileX, int tileY, int count) {
		// TilePosition tilePosition = position.toTilePosition();
		if (canBuildHere(tileX, tileY, ut))
			return APosition.createFromTile(tileX, tileY);
		else if (count == 0)
			return null;

		for (int i = 0; i < lastUp + 2; i++) {
			tileY--;
			if (canBuildHere(tileX, tileY, ut))
				return APosition.createFromTile(tileX, tileY);
		}

		for (int i = 0; i < lastLeft + 2; i++) {
			tileX--;
			if (canBuildHere(tileX, tileY, ut))
				return APosition.createFromTile(tileX, tileY);
		}

		for (int i = 0; i < lastDown + 2; i++) {
			tileY++;
			if (canBuildHere(tileX, tileY, ut))
				return APosition.createFromTile(tileX, tileY);
		}

		for (int i = 0; i < lastRight + 2; i++) {
			tileY--;
			if (canBuildHere(tileX, tileY, ut))
				return APosition.createFromTile(tileX, tileY);
		}

		return getBuildPositionCloseToR(ut, lastUp + 2, lastDown + 2, lastLeft + 2, lastRight + 2, tileX, tileY,
				--count);
	}

	public static APosition getBuildPositionCloseTo(AUnitType ut, APosition position) {
		if (canBuildHere(position, ut))
			return position;

		int startTileX = position.getTileX();
		int startTileY = position.getTileY();
		int maxCount = 25;

		return getBuildPositionCloseToR(ut, 0, 0, 0, 1, startTileX + 1, startTileY - 1, maxCount);

		/*
		 * APosition buildPosition = new APosition(
		 * Alice.getBwapi().getBuildLocation(ut.getUnitType(),
		 * position.toTilePosition(), 8, true).toPosition());
		 * 
		 * while (notPossible && maxCount != 0) { double minDistance =
		 * Integer.MAX_VALUE; for (AUnit mineralField :
		 * Select.ourMineralFields().values()) { double distanceMineralfield =
		 * buildPosition.getDistance(mineralField.getPosition()); if
		 * (distanceMineralfield < minDistance) minDistance = distanceMineralfield;
		 * 
		 * } if (minDistance < 200) { buildPosition = new APosition(Alice.getBwapi()
		 * .getBuildLocation(ut.getUnitType(), position.toTilePosition(), 8,
		 * true).toPosition()); maxCount--; } else notPossible = false; }
		 * 
		 * return buildPosition;
		 */

	}

	public static boolean hasCreep(APosition position) {
		return Alice.getBwapi().hasCreep(position.getTileX(), position.getTileY());
	}

	public static boolean hasCreep(int tileX, int tileY) {
		return Alice.getBwapi().hasCreep(tileX, tileY);
	}

	public static boolean isCloseToOurMinerals(APosition position) {
		for (AUnit mineralField : Select.ourMineralFields().values())
			if (mineralField.getDistance(position) < 250)
				return true;
		return false;
	}

	public static boolean canBuildHere(APosition position, AUnitType ut) {
		return Alice.getBwapi().canBuildHere(position.toTilePosition(), ut.getUnitType()) && hasCreep(position)
				&& !isCloseToOurMinerals(position);
	}

	public static boolean canBuildHere(int tileX, int tileY, AUnitType ut) {
		return canBuildHere(APosition.createFromTile(tileX, tileY), ut);
	}

	/**
	 * Prüft, ob die BasePosition noch bebaut werden kann oder wieder frei ist
	 */
	private static void updateBasePositions() {
		nextBase: for (APosition basePosition : getBasePositions()) {
			for (AUnit building : Select.ourBuildings().values())
				if (building.getPosition().isCloseTo(basePosition, 100)) {
					basePosition.setAddInfo(APosition.ADDINFO_IS_NO_FREE_BASELOCATION);
					continue nextBase;
				}

			for (AUnit enemyBuilding : Select.getEnemyBuildings().values())
				if (enemyBuilding.getPosition().isCloseTo(basePosition, 100)) {
					basePosition.setAddInfo(APosition.ADDINFO_IS_NO_FREE_BASELOCATION);
					continue nextBase;
				}

			basePosition.setAddInfo(APosition.ADDINFO_IS_FREE_BASELOCATION);

		}
	}

}
