/*
Filename: Driver.java
Author: Sam Prestwood
Last Updated: 2014-02-16
Abstract: Creates the window for the program to run in and executes the rest of
the program.
 */


// imports
import javax.swing.*;

public class Driver {
	
	// constants
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	// Creates the window
    public static void main(String[] args) {
    	JFrame window = new JFrame("TSP Art!");
    	window.setSize(WIDTH, HEIGHT);
    	window.setLocation(100, 100);
    	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	window.setContentPane(new Panel(WIDTH, HEIGHT));
    	window.setVisible(true);
    }
}