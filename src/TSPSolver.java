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
		/*return edge1[0].getX() == edge2[0].getX()
				|| edge1[0].getX() == edge2[1].getX()
				|| edge1[0].getY() == edge2[0].getY()
				|| edge1[0].getY() == edge2[1].getY()
				|| edge1[1].getX() == edge2[0].getX()
				|| edge1[1].getX() == edge2[1].getX()
				|| edge1[1].getY() == edge2[0].getY()
				|| edge1[1].getY() == edge2[1].getY();*/
		return edge1[0].equals(edge2[0]) || edge1[0].equals(edge2[1]) || edge1[1].equals(edge2[0]) || edge1[1].equals(edge2[1]);
	}
	
	public boolean sameSlope(Point[] edge1, Point[] edge2){
<<<<<<< HEAD
		double denom1 = edge1[1].getX() - edge1[0].getX();
		double denom2 = edge2[1].getX() - edge2[0].getX();
		if(denom1 == 0 && denom1 == denom2)
			return true;
		double slope1 = (edge1[1].getY() - edge1[0].getY())/denom1;
		double slope2 = (edge2[1].getY() - edge2[0].getY())/denom2;
=======
		double slope1 = (edge1[1].getY() - edge1[0].getY())/(edge1[1].getX() - edge1[0].getX());
		double slope2 = (edge2[1].getY() - edge2[0].getY())/(edge2[1].getX() - edge2[0].getX());
>>>>>>> 1f46a6b7983510fff6835aec61313341ed752865
		return slope1==slope2 || slope1==1/slope2;
	}

	public void swapEndPoints(Point p1, Point p2, ArrayList<Point> path) {
		Point tmp = p1; // (1, 3) t=4
		p1 = p2; // (2, 4)
		p2 = tmp;
		//Now flip the path between these edges 
		//Need to get path in order, not just screwed up set of edges
		
		//tmp = edge2[0];
		//edge2[0] = edge2[1];
		//edge2[1] = tmp;
	}
	
	public ArrayList<Point> reverseBetween(Point p1, Point p2, ArrayList<Point> path){
		ArrayList<Point> tempList = new ArrayList<Point>();
		for(int x = path.indexOf(p1)+1; x < path.indexOf(p2); x++){
			tempList.add(0, path.get(x));		
		}
		for(int x = path.indexOf(p1)+1; x < path.indexOf(p2); x++){
			path.set(x, tempList.get(0));
			tempList.remove(0);
		}
		return path;
	}
<<<<<<< HEAD
	
	private double distance(Point p1, Point p2){
		return Math.sqrt((p1.getX()-p2.getX())*(p1.getX()-p2.getX()) + (p1.getY()-p2.getY())*(p1.getY()-p2.getY()));
	}
=======
>>>>>>> 1f46a6b7983510fff6835aec61313341ed752865

	public ArrayList<Point> removeIntersections(
			ArrayList<Point> p) {

		// ArrayList<Point[]> convertedPath = stack2List(path);
		ArrayList<Point> path = new ArrayList<Point>(p);
		
		System.out.println("Starting to remove the intersections");

		//for (Point[] edge1 : convertedPath) {
			//for (Point[] edge2 : convertedPath) {
		for(int x = 0; x < path.size()-1; x++){
			Point[] edge1 = {path.get(x), path.get(x+1)};
			for(int y = 0; y < path.size()-1; y++){
				Point[] edge2 = {path.get(y), path.get(y+1)};
				boolean sharePt = sharePoint(edge1, edge2);
				boolean cross = Line2D.linesIntersect(edge1[0].getX(),
						edge1[0].getY(), edge1[1].getX(), edge1[1].getY(),
						edge2[0].getX(), edge2[0].getY(), edge2[1].getX(),
						edge2[1].getY());
				
				//System.out.println(sharePt + " " + cross);
<<<<<<< HEAD
				if (!equals(edge1, edge2) && !sharePt && !sameSlope(edge1, edge2) && cross) {
=======
				if (// !equals(edge1, edge2)
				!sharePt && cross) {
>>>>>>> 1f46a6b7983510fff6835aec61313341ed752865
					System.out.println(path.get(x) + " " + path.get(x+1) + " "
							+ path.get(y) + " " + path.get(y+1));
					//swapEndPoints(edge1[1], edge2[1], path);
					if(sameSlope(edge1, edge2)){
<<<<<<< HEAD
						//if(distance(edge1[0], edge2[0]) > distance(edge1[0], edge2[1])){
						System.out.println("Same slope");
							Point tmp = path.get(x+1);
							path.set(x+1, path.get(y+1));
							path.set(y+1, tmp);
						/*}
						else{
							Point tmp = path.get(x+1);
							path.set(x+1, path.get(y+1));
							path.set(y+1, tmp);
						}*/
=======
						
>>>>>>> 1f46a6b7983510fff6835aec61313341ed752865
					}
					else{
						Point tmp = path.get(x+1);
						path.set(x+1, path.get(y));
						path.set(y, tmp);
					}
					path = reverseBetween(path.get(x+1), path.get(y), path);
					System.out.println(path.get(x) + " " + path.get(x+1) + " "
							+ path.get(y) + " " + path.get(y+1));
					return path;
				}
			}

		}

		//System.out.println("Done removing intersections");

		return path;

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
