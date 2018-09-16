package alice.graph;

import java.util.ArrayList;
import java.util.List;

public interface IGraph<T> {
	void addNode( T value );
	void addEdge( T value1, T value2, double edge );
	
	void remove(T value);
	
	List<T> bfs( int startIndex );
	List<T> dfs( int startIndex );

	boolean isConnected();
	boolean hasCycles();
	boolean isTree();
	

	double getDistance(T value1, T value2);
	ArrayList<T> getConnections(T value);

}
