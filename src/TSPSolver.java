/*
Filename: TSPSolver.java
Author: Sam Prestwood
Last Updated: 2014-03-30
Abstract: Given a set of points, approximates an efficient path to visit all of 
 them
 */

// imports:
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;
import java.awt.Point;

public class TSPSolver {
	// fields:
	private ArrayList<Point> points;

	// Class constructor, creats a list of points to visit from the dithered
	// image matrix (for now, assume that the dithered image is only B/W)
	public TSPSolver(int[][] ditheredImgBW) {
		points = new ArrayList<Point>();
		for (int y = 0; y < ditheredImgBW.length; y++)
			for (int x = 0; x < ditheredImgBW[y].length; x++)
				if (ditheredImgBW[y][x] == 0)
					points.add(new Point(x, y));
	}
	
	// This does a simple 'greedy' approximation of the TSP. It chooses a random
	// starting point and then iteratively chooses the next closest point to 
	// create the path. Currently, we don't do anything clever--this is just a 
	// simple Theta(n^2) approach.
	public Stack<Point> createPathClosestNeighbor() {
		// create stack to hold path:
		Stack<Point> result = new Stack<Point>();
						
		// randomly choose starting point:
		Point start = points.get((int)(Math.random() * (double)points.size()));
		result.push(start);
		
		// used to printing out progress
		int startSize = points.size();
		
		while(points.size() > 0) {
			points.remove(start);
			
			// find closest point to start
			int closestPoint = 0;
			for(int i = 1; i < points.size(); i++)
				if(start.distance(points.get(i)) < 
				   start.distance(points.get(closestPoint)))
					closestPoint = i;
			
			// update starting point
			start = points.get(closestPoint);
			result.push(points.get(closestPoint));
			points.remove(closestPoint);

			// print out progress
			System.out.println( (startSize - points.size()) + " of " + 
				startSize + " points computed");
		}
		return result;
	}
}
