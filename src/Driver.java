/*
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

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
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	
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