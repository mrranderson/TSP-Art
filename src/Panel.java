/*
Filename: Panel.java
Author: Sam Prestwood
Last Updated: 2014-02-16
Abstract: Draws to the screen and handles the timing of everything.
 */

// imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {
	// constants
	private int FRAME_PERIOD = 25; // in milliseconds

	// fields
	private int width;
	private int height;
	private BufferedImage screen;
	private Graphics screenBuffer;
	private Timer clock;
	private int xPos = 0, yPos = 0;

	// Class constructor; instantiates all local variables
	public Panel(int w, int h) {
		width = w;
		height = h;

		// used to draw screenBuffer to the actual screen.
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// a java formality that allows us to do ``speedy'' graphics
		screenBuffer = screen.getGraphics();

		// program clock; defines and controls the framerate
		clock = new Timer(FRAME_PERIOD, new ActionsToPerform());
		clock.start();
	}

	// draws the things that were written to screenBuffer to the screen (a java
	// formality)
	public void paintComponent(Graphics g) {
		g.drawImage(screen, 0, 0, width, height, null);
	}

	// this method is fired off by clock every FRAME_PERIOD milliseconds. We do
	// most of the stuff in this method.
	private class ActionsToPerform implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("I'm printing out every " + FRAME_PERIOD
					+ "ms, or at " + (1000.0 / FRAME_PERIOD) + " fps.");
			
			// white background
			screenBuffer.setColor(Color.white);			
			screenBuffer.fillRect(0, 0, width, height);
			
			// red box:
			screenBuffer.setColor(Color.red);
			screenBuffer.fillRect(xPos, yPos, 10, 10);
			
			// update positions
			xPos++;
			yPos++;
			
			// write to buffer:
			repaint();
		}
	}
}
