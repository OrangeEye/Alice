package alice.graph;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public abstract class AbstractUndirectedGraphTest {
	public abstract IGraph<Character> get();

	IGraph<Character> g;

	@Before
	public void setUp() {
		g = get();
	}

	@Test
	public void testGraph0() {
		g.addNode('a');
		g.addNode('b');
		g.addNode('c');
		g.addNode('d');
		g.addNode('e');
		g.addNode('f');

		g.addEdge('a', 'b', 1);
		g.addEdge('a', 'c', 2);
		g.addEdge('a', 'd', 3);

		g.addEdge('b', 'd', 1);
		g.addEdge('b', 'f', 6);

		g.addEdge('c', 'e', 2);

		g.addEdge('e', 'f', 2);

		assertEquals(g.getDistance('a', 'b'), 1, 0);
		assertEquals(g.getDistance('a', 'c'), 2, 0);
		assertEquals(g.getDistance('a', 'd'), 2, 0);
		assertEquals(g.getDistance('a', 'e'), 4, 0);
		assertEquals(g.getDistance('a', 'f'), 6, 0);

		/*
		 * assertEquals( g.getEdgeValue('a', 'c' ),2,0); assertEquals(
		 * g.getEdgeValue('a', 'd' ),3,0);
		 * 
		 * assertEquals( g.getEdgeValue('b', 'd' ),1,0); assertEquals(
		 * g.getEdgeValue('b', 'f' ),6,0);
		 * 
		 * assertEquals( g.getEdgeValue('c', 'e' ),2,0);
		 * 
		 * assertEquals( g.getEdgeValue('e', 'f' ),2,0);
		 */

	}

	@Test
	public void testGraph1() {
		g.addNode('a');
		g.addNode('b');
		g.addNode('c');
		g.addNode('d');

		g.addEdge('a', 'b', 1);
		g.addEdge('a', 'c', 4);

		g.addEdge('b', 'c', 2);
		g.addEdge('b', 'd', 7);

		g.addEdge('c', 'd', 3);

		g.addEdge('e', 'f', 2);

		assertEquals(g.getDistance('a', 'b'), 1, 0);
		assertEquals(g.getDistance('a', 'c'), 3, 0);
		assertEquals(g.getDistance('a', 'd'), 6, 0);

		/*
		 * assertEquals( g.getNode('a').getDistance(g.getNode('b')),1,0); assertEquals(
		 * g.getNode('a').getDistance(g.getNode('c')),3,0); assertEquals(
		 * g.getNode('a').getDistance(g.getNode('d')),6,0);
		 */
	}

	@Test
	public void testGraph2() {
		g.addNode('s');
		g.addNode('a');
		g.addNode('b');
		g.addNode('c');
		g.addNode('d');
		g.addNode('e');
		g.addNode('f');
		g.addNode('g');
		g.addNode('z');

		g.addNode('x');

		g.addEdge('s', 'a', 1);
		g.addEdge('s', 'b', 3);
		g.addEdge('s', 'g', 5);

		g.addEdge('a', 'b', 14);
		g.addEdge('a', 'c', 13);

		g.addEdge('b', 'c', 11);
		g.addEdge('b', 'g', 2);

		g.addEdge('c', 'd', 4);
		g.addEdge('c', 'e', 9);

		g.addEdge('d', 'e', 7);
		g.addEdge('d', 'f', 6);
		g.addEdge('d', 'g', 10);

		g.addEdge('e', 'z', 12);

		g.addEdge('f', 'z', 8);

		g.addEdge('x', 's', 1);
		g.addEdge('x', 'a', 1);
		g.addEdge('x', 'b', 1);
		g.addEdge('x', 'c', 1);
		g.addEdge('x', 'd', 1);
		g.addEdge('x', 'e', 1);
		g.addEdge('x', 'f', 1);
		g.addEdge('x', 'g', 1);
		g.addEdge('x', 'z', 1);

		// assertTrue(g.getDistance('s', 'a') != 1);
		assertTrue(g.getDistance('s', 'b') != 3);
		assertTrue(g.getDistance('s', 'c') != 13);
		assertTrue(g.getDistance('s', 'd') != 15);
		assertTrue(g.getDistance('s', 'e') != 21);
		assertTrue(g.getDistance('s', 'f') != 21);
		assertTrue(g.getDistance('s', 'g') != 5);
		assertTrue(g.getDistance('s', 'z') != 29);

		g.remove('x');

		assertTrue(g.getDistance('s', 'a') == 1);
		assertTrue(g.getDistance('s', 'b') == 3);
		assertEquals(g.getDistance('s', 'c'), 14, 0);
		assertTrue(g.getDistance('s', 'd') == 15);
		assertEquals(g.getDistance('s', 'e'), 22, 0);
		assertTrue(g.getDistance('s', 'f') == 21);
		assertTrue(g.getDistance('s', 'g') == 5);
		assertTrue(g.getDistance('s', 'z') == 29);

		ArrayList<Character> connections = g.getConnections('s');
		assertTrue(connections.get(0).equals('a'));
		assertTrue(connections.get(1).equals('b'));
		assertTrue(connections.get(2).equals('g'));
		assertTrue(connections.get(3).equals('c'));
		assertTrue(connections.get(4).equals('d'));
		assertTrue(connections.get(5).equals('f'));
		assertTrue(connections.get(6).equals('e'));
		assertTrue(connections.get(7).equals('z'));

	}

	/*
	 * 
	 * @Test public void testGraph1() { g.addNode( 'a' ); g.addNode( 'b' );
	 * g.addNode( 'c' ); g.addNode( 'd' );
	 * 
	 * g.addEdge( 0, 1 ); g.addEdge( 1, 2 ); g.addEdge( 1, 3 );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'a', 'b', 'c', 'd' } ), g.bfs(
	 * 0 ) ); assertEquals( Arrays.asList( new Character[] { 'a', 'b', 'c', 'd' } ),
	 * g.dfs( 0 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'c', 'b', 'a', 'd' } ), g.bfs(
	 * 2 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'd', 'b', 'a', 'c' } ), g.dfs(
	 * 3 ) );
	 * 
	 * assertTrue( g.isTree() ); assertTrue( g.isConnected() ); assertFalse(
	 * g.hasCycles() ); }
	 * 
	 * @Test public void testGraph2() { g.addNode( 'a' ); g.addNode( 'b' );
	 * g.addNode( 'c' ); g.addNode( 'd' );
	 * 
	 * g.addEdge( 0, 1 ); g.addEdge( 2, 0 ); g.addEdge( 1, 2 ); g.addEdge( 1, 3 );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'a', 'b', 'c', 'd' } ), g.bfs(
	 * 0 ) ); assertEquals( Arrays.asList( new Character[] { 'a', 'b', 'c', 'd' } ),
	 * g.dfs( 0 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'c', 'a', 'b', 'd' } ), g.bfs(
	 * 2 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'd', 'b', 'a', 'c' } ), g.dfs(
	 * 3 ) );
	 * 
	 * assertFalse( g.isTree() ); assertTrue( g.isConnected() ); assertTrue(
	 * g.hasCycles() ); }
	 * 
	 * @Test public void testGraph3() { g.addNode( 'a' ); g.addNode( 'b' );
	 * g.addNode( 'c' ); g.addNode( 'd' ); g.addNode( 'e' ); g.addNode( 'f' );
	 * 
	 * // a g.addEdge( 0, 1 ); g.addEdge( 0, 2 ); g.addEdge( 0, 3 ); g.addEdge( 0, 5
	 * );
	 * 
	 * // b g.addEdge( 1, 2 ); g.addEdge( 1, 3 ); g.addEdge( 1, 4 );
	 * 
	 * // c g.addEdge( 2, 4 ); g.addEdge( 2, 5 );
	 * 
	 * // d g.addEdge( 3, 4 ); g.addEdge( 3, 5 );
	 * 
	 * // e g.addEdge( 4, 5 );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'a', 'b', 'c', 'd', 'f', 'e' }
	 * ), g.bfs( 0 ) ); assertEquals( Arrays.asList( new Character[] { 'a', 'b',
	 * 'c', 'e', 'd', 'f' } ), g.dfs( 0 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'd', 'a', 'b', 'e', 'f', 'c' }
	 * ), g.bfs( 3 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'f', 'a', 'b', 'c', 'e', 'd' }
	 * ), g.dfs( 5 ) );
	 * 
	 * assertFalse( g.isTree() ); assertTrue( g.isConnected() ); assertTrue(
	 * g.hasCycles() ); }
	 * 
	 * @Test public void testGraph4() { g.addNode( 'A' ); g.addNode( 'B' );
	 * g.addNode( 'C' ); g.addNode( 'D' ); g.addNode( 'E' ); g.addNode( 'F' );
	 * 
	 * g.addEdge( 0, 0 ); g.addEdge( 0, 1 ); g.addEdge( 2, 3 ); g.addEdge( 2, 4 );
	 * g.addEdge( 3, 4 ); g.addEdge( 3, 5 ); g.addEdge( 4, 5 ); g.addEdge( 5, 5 );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'A', 'B' } ), g.bfs( 0 ) );
	 * assertEquals( Arrays.asList( new Character[] { 'B', 'A' } ), g.bfs( 1 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'C', 'D', 'E', 'F' } ), g.bfs(
	 * 2 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'E', 'C', 'D', 'F' } ), g.dfs(
	 * 4 ) );
	 * 
	 * assertFalse( g.isTree() ); assertFalse( g.isConnected() ); assertTrue(
	 * g.hasCycles() ); }
	 * 
	 * @Test public void testGraph5() { g.addNode( 'A' ); g.addNode( 'B' );
	 * g.addNode( 'C' ); g.addNode( 'D' ); g.addNode( 'E' );
	 * 
	 * g.addEdge( 0, 1 ); g.addEdge( 1, 2 ); g.addEdge( 2, 3 );
	 * 
	 * g.addEdge( 4, 1 ); g.addEdge( 3, 4 );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'A', 'B', 'C', 'E', 'D' } ),
	 * g.bfs( 0 ) ); assertEquals( Arrays.asList( new Character[] { 'B', 'A', 'C',
	 * 'E', 'D' } ), g.bfs( 1 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'D', 'C', 'E', 'B', 'A' } ),
	 * g.bfs( 3 ) );
	 * 
	 * assertEquals( Arrays.asList( new Character[] { 'E', 'B', 'A', 'C', 'D' } ),
	 * g.dfs( 4 ) );
	 * 
	 * assertFalse( g.isTree() ); assertTrue( g.isConnected() ); assertTrue(
	 * g.hasCycles() ); }
	 */
}
