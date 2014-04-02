/*
Filename: TSPNode.java
Author: Sam Prestwood
Last Updated: 2014-03-29
Abstract: A simple node class that is used to store the path of the TSP
 */

// imports:
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class TSPNode {
	// fields:
	private Point myPoint;
	private Set<TSPNode> edges;
	private int index;

	public TSPNode(Point p, int i) {
		myPoint = p;
		edges = new HashSet<TSPNode>();
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	// returns the set of edges for this node; useful to prevent recursive 
	// looping in addEdge()
	public Set<TSPNode> getEdges() {
		return edges;
	}
	
	// adds a bidirectional edge between this node and n. 
	public boolean addEdge(TSPNode n) {
		boolean result = n.getEdges().add(this);
		n.setIndex(index);
		return result && edges.add(n);
	}
	
	// This will return the next node in the path. The context for this method 
	// is that it's assumed that TSPNodes will be used to create the TSP path, 
	// that is, essentially making a linked list. This implies that every node
	// will have, at most, 2 edges (i.e. the previous node and the next node).
	// Therefore, you pass this method the previous node and it will return the
	// next node. You may be thinking, "why not just have a getNext and getPrev
	// method instead of all edges-with-sets nonsense?". The reasoning for this
	// was that when creating the path, it's difficult to know which edge is 
	// 'next' and which is 'previous'. Instead, once the path is created, we let
	// the TSP solver arbitrarily choose an end of the path as the start, and we
	// use this method to easily traverse down the path. This method will return
	// null if there are not two edges in the set of edges. This will hopefully
	// cause the program to halt at the end of the path.
	public TSPNode getNext(TSPNode prevNode) {
		if(edges.size() == 2)
			for(TSPNode next : edges)
				if(!next.getPoint().equals(prevNode.getPoint()))
					return next;
		return null;
	}
	
	public boolean removeEdge(TSPNode nodeToRemove){
		return edges.remove(nodeToRemove);
	}
	
	public int numEdges() {
		return edges.size();
	}
	
	public Point getPoint() {
		return myPoint;
	}
	
	public void setPoint(Point p) {
		myPoint = p;
	}
}
