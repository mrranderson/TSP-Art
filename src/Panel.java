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
	private String IMAGE = "img/apple.jpg";

	// fields
	private int width;
	private int height;
	private BufferedImage screen;
	private Graphics screenBuffer;
	private Timer clock;
	private ImageProcessor imgProc;
	private int[][] ditheredImgBW;
	private int[][][] ditheredImgRGB;
	
	private BufferedImage tmpImg;

	// Class constructor; instantiates all local variables
	public Panel(int w, int h) {
		width = w;
		height = h;

		// used to draw screenBuffer to the actual screen.
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// a java formality that allows us to do ``speedy'' graphics
		screenBuffer = screen.getGraphics();
		
		// prepare our image:
		imgProc = new ImageProcessor(IMAGE);
		tmpImg = imgProc.resize(50);
		ditheredImgBW = imgProc.ditherBW();
		
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
			// draw a white background
			screenBuffer.setColor(Color.white);			
			screenBuffer.fillRect(0, 0, width, height);
			
			//screenBuffer.drawImage(tmpImg, 0, 0, null);
			
			// draw ditheredImgBW to screen
			drawDitheredImgBW(5, 15, Color.black);
			
			// write to buffer:
			repaint();
		}
	}
	
	// draws ditheredImgBW to screenBuffer as a series of circles (blobs). The
	// circles have a radius and a spacing between each of them because it is 
	// assumed that the dithered image will be of a low resolution and thus 
	// needs to be blown up to be fully seen. Circles are filled in with color.
	public void drawDitheredImgBW(int radius, int spacing, Color color) {
		int xPad = 10, yPad = 10;
		screenBuffer.setColor(color);
		for(int r = 0; r < ditheredImgBW.length; r++) {
			for(int c = 0; c < ditheredImgBW[r].length; c++) {
				if(ditheredImgBW[r][c] != 255)
					screenBuffer.fillOval(xPad + c*spacing, yPad + r*spacing, 
							radius*2, radius*2);
				
			}
		}
	}
}
