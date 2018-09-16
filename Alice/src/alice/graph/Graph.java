package alice.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Graph<T> implements IGraph<T> {
	protected ArrayList<Node<T>> nodes = new ArrayList<Node<T>>();
	protected ArrayList<Node<T>> dfs = new ArrayList<Node<T>>();
	protected ArrayList<Node<T>> bfs = new ArrayList<Node<T>>();
	protected Deque<Node<T>> tempBfs = new ArrayDeque<Node<T>>();

	@Override
	public void addNode(T value) {
		nodes.add(new Node<T>(value));
		setOutdated();
	}

	@Override
	public List<T> bfs(int startIndex) {
		if (startIndex >= 0 && startIndex < nodes.size()) {
			bfs.clear();
			bfs.add(nodes.get(startIndex));
			breadthFirstR(nodes.get(startIndex));
			ArrayList<T> list = new ArrayList<T>();
			for (Node<T> bfsNode : bfs)
				list.add(bfsNode.getValue());
			return list;
		}
		return null;
	}

	private void breadthFirstR(Node<T> node) {
		// bfs.add(node);
		// tempBfs.add(node);
		for (Node<T> connection : node.getNeighborNodes().keySet())
			if (!bfs.contains(connection) && !tempBfs.contains(connection)) {
				tempBfs.addLast(connection);
			}

		// Führt eine BreitenSuche bei den Verbindungen durch, die noch nicht in der
		// Liste sind (um Zyklen zu vermeiden)
		if (!tempBfs.isEmpty()) {
			bfs.add(tempBfs.getFirst());
			breadthFirstR(tempBfs.pollFirst());
		}

		/*
		 * for (Node<T> tempNode : tempBfs) { for (Node<T> connection :
		 * tempNode.getConnections()) if (!bfs.contains(connection))
		 * breadthFirstR(connection); }
		 */
	}

	@Override
	public List<T> dfs(int startIndex) {
		if (startIndex >= 0 && startIndex < nodes.size()) {
			dfs.clear();
			deepFirstSearch(nodes.get(startIndex));
			ArrayList<T> list = new ArrayList<T>();
			for (Node<T> dsfNode : dfs)
				list.add(dsfNode.getValue());
			return list;
		}
		return null;
	}

	private void deepFirstSearch(Node<T> node) {

		dfs.add(node);
		for (Node<T> connection : node.getNeighborNodes().keySet()) {
			if (!dfs.contains(connection))
				deepFirstSearch(connection);
		}

	}

	private ArrayList<Node<T>> visited = new ArrayList<Node<T>>();

	/**
	 * Stellt eine Kante mit einem Wert zwischen den Knoten mit den entsprechenden
	 * Werten her.
	 */
	@Override
	public void addEdge(T value1, T value2, double edge) {
		if (value1 == null || value2 == null || edge <= 0 || !nodes.contains(getNode(value1))
				|| !nodes.contains(getNode(value2))) {
			throw new ArithmeticException("Fehler in Paramtern addEdge()");
		}

		getNode(value1).addConnection(getNode(value2), edge);
		setOutdated();

	}

	private void setOutdated() {
		for (Node<T> node : nodes) {
			node.setOutdated(true);
		}
	}

	@Override
	public boolean isConnected() {
		for (Node<T> node : nodes) {
			bfs(nodes.indexOf(node));
			if (bfs.size() == nodes.size())
				return true;
		}
		return false;
	}

	@Override
	public boolean hasCycles() {
		if (!nodes.isEmpty()) {
			boolean result = false;
			// Macht eine Tiefensuche bei jedem Knoten und gibt boolean zurück ob ein Zyklus
			// da ist
			for (Node<T> node : nodes) {
				visited.clear();
				result = hasCyclesR(node, null);
				if (result)
					return result;
			}
		}
		return false;
	}

	private boolean hasCyclesR(Node<T> node, Node<T> previous) {
		if (visited.contains(node))
			return true;
		visited.add(node);
		boolean result = false;
		for (Node<T> connection : node.getNeighborNodes().keySet()) {
			if (!connection.equals(previous))
				result = hasCyclesR(connection, node);
			if (result)
				return result;
		}
		// Löscht den letzten Punkt, wenn keine Kante mehr da ist. Damit ist visited
		// immer ein Weg und sobald ein Knoten doppelt vorkommt ist ein Zyklus vorhanden
		visited.remove(visited.size() - 1);
		return false;
	}

	@Override
	public boolean isTree() {
		return isConnected() && !hasCycles();
	}

	/**
	 * Gibt die Länge der Kante zwischen zwei Knote zurück
	 */

	protected double getEdgeValue(T value1, T value2) {
		if (value1 == null || value2 == null)
			return -1;

		return getNode(value1).getEdgeValue(getNode(value2));

	}

	protected Node<T> getNode(T value) {
		if (value == null)
			return null;

		for (Node<T> node : nodes) {
			if (node.getValue().equals(value)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * entfernt einen Knoten aus dem Graphen
	 */
	@Override
	public void remove(T value) {
		Node<T> toRemove = getNode(value);
		nodes.remove(toRemove);
		for (Node<T> node : nodes) {
			node.removeConnection(toRemove);
		}
	}

	/**
	 * Gibt die kürzeste Entfernung zwischen zwei Knoten zurück.
	 */

	@Override
	public double getDistance(T value1, T value2) {
		if (value1 == null || value2 == null)
			return -1;
		Node<T> node = getNode(value1);
		if (node == null)
			return -1;

		return node.getDistance((getNode(value2)));
	}

	/**
	 * Gibt die Verbindungen des Knoten in sortierter, aufsteigender Reinfolge
	 * zurück.
	 */

	@Override
	public ArrayList<T> getConnections(T value) {
		Node<T> node = getNode(value);
		if (node == null)
			return null;
		
		return node.getConnections();
	}
	public ArrayList<T> getNodes() {
		
		ArrayList<T> list = new ArrayList<T>();
		
		for(Node<T> node : nodes) {
			list.add(node.getValue());
		}
		
		return list;
	}


}
