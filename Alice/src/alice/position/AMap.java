package alice.position;

import java.util.ArrayList;
import java.util.HashMap;

import alice.AGame;
import alice.graph.Graph;
import alice.graph.Node;
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
		ArrayList<APosition> visited = new ArrayList<APosition>();

		for (APosition position1 : baseLocations.keySet()) {
			for (APosition position2 : baseLocations.keySet()) {
				if (position1.equals(position2))
					continue;

				basePositions.addEdge(position1, position2,
						baseLocations.get(position1).getGroundDistance(baseLocations.get(position2)));
			}
		}
	}

	public static ArrayList<APosition> getBasePositions() {
		return basePositions.getConnections(mainBasePosition);
	}

	public static APosition getMainBasePosition() {
		return mainBasePosition;
	}

	public static APosition getNextExpandPosition() {
		if (getBasePositions().size() > 0)
			return getBasePositions().get(0);
		return null;
	}

}
