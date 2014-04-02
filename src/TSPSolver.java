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
		Point start = points
				.get((int) (Math.random() * (double) points.size()));
		result.push(start);

		// used to printing out progress
		int startSize = points.size();

		while (points.size() > 0) {
			points.remove(start);

			// find closest point to start
			int closestPoint = 0;
			for (int i = 1; i < points.size(); i++)
				if (start.distance(points.get(i)) < start.distance(points
						.get(closestPoint)))
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

	public ArrayList<Point[]> stack2List(Stack<Point> path) {
		ArrayList<Point[]> list = new ArrayList<Point[]>();
		Point prevPoint = path.pop();
		for (Point p : path) {
			list.add(new Point[] { p, prevPoint });
			prevPoint = p;
		}
		return list;
	}

	public boolean equals(Point[] edge1, Point[] edge2) {
		return edge1[0].equals(edge2[0]) && edge1[1].equals(edge2[1]);
	}

	// checks colinearity of points...?
	public boolean sharePoint(Point[] edge1, Point[] edge2) {
		return edge1[0].getX() == edge2[0].getX()
				|| edge1[0].getX() == edge2[1].getX()
				|| edge1[0].getY() == edge2[0].getY()
				|| edge1[0].getY() == edge2[1].getY()
				|| edge1[1].getX() == edge2[0].getX()
				|| edge1[1].getX() == edge2[1].getX()
				|| edge1[1].getY() == edge2[0].getY()
				|| edge1[1].getY() == edge2[1].getY();
	}

	public void swapEndPoints(Point[] edge1, Point[] edge2) {
		Point tmp = edge1[1]; // (1, 3) t=4
		edge1[1] = edge2[0]; // (2, 4)
		edge2[0] = tmp;
		//tmp = edge2[0];
		//edge2[0] = edge2[1];
		//edge2[1] = tmp;
	}

	public ArrayList<Point[]> removeIntersections(
			ArrayList<Point[]> convertedPath) {

		// ArrayList<Point[]> convertedPath = stack2List(path);

		System.out.println("Starting to remove the intersections");

		for (Point[] edge1 : convertedPath) {
			for (Point[] edge2 : convertedPath) {
				boolean sharePt = sharePoint(edge1, edge2);
				boolean cross = Line2D.linesIntersect(edge1[0].getX(),
						edge1[0].getY(), edge1[1].getX(), edge1[1].getY(),
						edge2[0].getX(), edge2[0].getY(), edge2[1].getX(),
						edge2[1].getY());
				
				//System.out.println(sharePt + " " + cross);
				if (// !equals(edge1, edge2)
				!sharePt && cross) {
					System.out.println(edge1[0] + " " + edge1[1] + " "
							+ edge2[0] + " " + edge2[1]);
					swapEndPoints(edge1, edge2);
					System.out.println(edge1[0] + " " + edge1[1] + " "
							+ edge2[0] + " " + edge2[1]);

					return convertedPath;
				}
			}

		}

		System.out.println("Done removing intersections");

		return convertedPath;

		/*
		 * ArrayList<Point[]> convertedPath = stack2List(path);
		 * 
		 * System.out.println("Starting to remove the intersections");
		 * 
		 * boolean restart1 = false; do { restart1 = false; boolean restart2 =
		 * false; for (Point[] edge1 : convertedPath) { for (Point[] edge2 :
		 * convertedPath) { if (!equals(edge1, edge2) && !adjacent(edge1, edge2)
		 * && Line2D.linesIntersect(edge1[0].getX(), edge1[0].getY(),
		 * edge1[1].getX(), edge1[1].getY(), edge2[0].getX(), edge2[0].getY(),
		 * edge2[1].getX(), edge2[1].getY())) {
		 * 
		 * swapEndPoints(edge1, edge2); restart2 = true; restart1 = true; break;
		 * } } if (restart2) break; } System.out.println("restart1 = " +
		 * restart1); } while (restart1);
		 * 
		 * System.out.println("Done removing intersections");
		 * 
		 * return convertedPath;
		 */
	}
}

/*
 * boolean restart1 = true; while(restart) boolean restart2 = false; for edge1
 * in path { for edge2 in path { if (edge1 != edge2 && intersect(edge1, edge2))
 * { swapEndPoints(edge1.startPt(), edge1.endPt(), edge2.startPt(),
 * edge2.endPt()); restart2 = true; break; } } if(restart2) break; else if
 * (edge1 == LastEdgeInPath) restart = false; }
 */
