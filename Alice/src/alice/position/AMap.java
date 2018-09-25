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

	/**
	 * wird vielleicht nicht gebraucht
	 * 
	 * @param ut
	 * @param position
	 * @return
	 */

	public static APosition getBuildPositionCloseTo(AUnitType ut, APosition position) {
		int startTileX = position.getTileX();
		int startTileY = position.getTileY();
		int buildingTileWidth = ut.getTileWidth();
		int buildingTileHeight = ut.getTileHeight();

		return new APosition(
				Alice.getBwapi().getBuildLocation(ut.getUnitType(), position.toTilePosition(), 8, true).toPosition());

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
