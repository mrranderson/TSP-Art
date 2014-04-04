/*
Filename: TSPSolver.java
Author: Sam Prestwood and Ryan Anderson
Last Updated: 2014-04-03
Abstract: Given a set of points, approximates an efficient path to visit all of 
 them
 */

// imports:
import java.util.ArrayList;
import java.util.Stack;
import java.awt.Point;
import java.awt.geom.Line2D;

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
		Point start = points.get((int) (Math.random() * (double)points.size()));
		result.push(start);

		// used to printing out progress
		int startSize = points.size();

		while (points.size() > 0) {
			points.remove(start);

			// find closest point to start
			int closestPoint = 0;
			for (int i = 1; i < points.size(); i++)
				if (start.distance(points.get(i)) < 
						start.distance(points.get(closestPoint)))
					closestPoint = i;

			// update starting point
			start = points.get(closestPoint);
			result.push(points.get(closestPoint));
			points.remove(closestPoint);

			// print out progress
			System.out.println((startSize - points.size()) + " of " + startSize
					+ " points computed");
		}
		return result;
	}

	// converts a stack of points to an arraylist of points. We probably could 
	// do without this method, but that would require refactoring of the code.
	// For now, we're keeping it in because it doesn't noticably slow the 
	// overall program down and we need to finish this program asap.
	public ArrayList<Point[]> stack2List(Stack<Point> path) {
		ArrayList<Point[]> list = new ArrayList<Point[]>();
		Point prevPoint = path.pop();
		for (Point p : path) {
			list.add(new Point[] { p, prevPoint });
			prevPoint = p;
		}
		return list;
	}

	// checks if two 'edges' are eqaul. For ease, we define an edge as an array
	// of points with a length of 2. To ensure that java doesn't do its dumb 
	// checking of equality by reference, we wrote this method.
	public boolean equals(Point[] edge1, Point[] edge2) {
		return edge1[0].equals(edge2[0]) && edge1[1].equals(edge2[1]);
	}

	// checks if edge1 and edge2 share any end-points
	public boolean sharePoint(Point[] edge1, Point[] edge2) {
		return edge1[0].equals(edge2[0]) || edge1[0].equals(edge2[1])
				|| edge1[1].equals(edge2[0]) || edge1[1].equals(edge2[1]);
	}

	// checks if edge1 and edge2 have the same slope
	public boolean sameSlope(Point[] edge1, Point[] edge2) {
		double denom1 = edge1[1].getX() - edge1[0].getX();
		double denom2 = edge2[1].getX() - edge2[0].getX();
		if (denom1 == 0 && denom1 == denom2)
			return true;
		if ((denom1 == 0 && denom1 != denom2)
				|| (denom2 == 0 && denom2 != denom1))
			return false;
		double slope1 = (edge1[1].getY() - edge1[0].getY()) / denom1;
		double slope2 = (edge2[1].getY() - edge2[0].getY()) / denom2;
		return slope1 == slope2 || slope1 == 1 / slope2;
	}

	// reverses the order of points in a contiguous region of the overall path.
	// This is used when uncrossing paths; if you uncross a path, you also have
	// to make sure the path reverses its direction
	public ArrayList<Point> reverseBetween(int start, int end, 
			ArrayList<Point> path) {
		ArrayList<Point> tempList = new ArrayList<Point>();
		for (int x = start + 1; x < end; x++) {
			tempList.add(0, path.get(x));
		}
		for (int x = start + 1; x < end; x++) {
			path.set(x, tempList.get(0));
			tempList.remove(0);
		}
		return path;
	}

	public ArrayList<Point> removeIntersections(ArrayList<Point> p) {

		// make the new path:
		ArrayList<Point> path = new ArrayList<Point>(p);
		
		// loop through every possible pair of edges:
		for (int x = 0; x < path.size() - 1; x++) {
			Point[] edge1 = { path.get(x), path.get(x + 1) };
			for (int y = x + 1; y < path.size() - 1; y++) {
				Point[] edge2 = { path.get(y), path.get(y + 1) };
				
				boolean sharePt = sharePoint(edge1, edge2);
				boolean cross = Line2D.linesIntersect(edge1[0].getX(),
						edge1[0].getY(), edge1[1].getX(), edge1[1].getY(),
						edge2[0].getX(), edge2[0].getY(), edge2[1].getX(),
						edge2[1].getY());

				// if edge1 and edge2:
				//  1. are not equal
				//	2. don't share any endpoints
				//	3. cross each other...
				if (!equals(edge1, edge2) && !sharePt && cross) {
					
					// uncomment for debugging:
					/*
					System.out.println("Uncrossing:");
					System.out.println("edge1:\t(" + edge1[0].getX() + 
							",\t" + edge1[0].getY() + ")\t->\t(" + 
							edge1[1].getX() + ",\t" + edge1[1].getY() + ")");
					System.out.println("edge2:\t(" + edge2[0].getX() + 
							",\t" + edge2[0].getY() + ")\t->\t(" + 
							edge2[1].getX() + ",\t" + edge2[1].getY() + ")");
					*/

					// if the edges have the same slope, then swap the endpoints
					if (sameSlope(edge1, edge2)) {
						System.out.println("Same slope");
						Point tmp = path.get(x + 1);
						path.set(x + 1, path.get(y + 1));
						path.set(y + 1, tmp);
					}
					
					// otherwise, swap the endpoint of the first edge with the 
					// start point of the second edge
					else {
						Point tmp = path.get(x + 1);
						path.set(x + 1, path.get(y));
						path.set(y, tmp);
					}
					
					// now reverse the points between the edges that were just
					// uncrossed:
					path = reverseBetween(x + 1, y, path);
										
					// return the completed path:
					return path;
				}
			}

		}

		// an extra return statement for when you complete checking the entire 
		// path:
		return path;

	}
}