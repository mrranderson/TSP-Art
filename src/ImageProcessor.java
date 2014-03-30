/*
Filename: ImageProcessor.java
Author: Sam Prestwood
Last Updated: 2014-03-14
Abstract: Reads in an image, resizes it, and dithers it.
 */

// imports:
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageProcessor {

	// fields:
	private BufferedImage img;

	// Class constructor, reads in image from filename
	public ImageProcessor(String filename) {
		// attempt to read in image
		try {
			img = ImageIO.read(new File(filename));
		}
		catch (IOException e) {
			System.out.println("Error! Image \"" + filename + "\" not found!");
		}
	}

	// resizes img so that each side is at most maxSide pixels long while also
	// retaining the original aspect ratio of the image.
	public BufferedImage resize(int maxSide) {
		// determine new dimensions:
		int width = maxSide, height = maxSide;
		if (img.getWidth() > img.getHeight())
			height = (int) (((double) maxSide / img.getWidth()) * img
					.getHeight());
		else if (img.getWidth() <= img.getHeight())
			width = (int) (((double) maxSide / img.getHeight()) * img
					.getWidth());

		// System.out.println("w = " + width + " h = " + height);

		// resize image with new dimensions:
		BufferedImage resized = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics tmpGraphics = resized.createGraphics();
		tmpGraphics.drawImage(img, 0, 0, width, height, null);
		tmpGraphics.dispose();

		// return resized image:
		return img = resized;
	}

	// treats img like a black and white image and dither's it according to the
	// Floyd-Steinberg dithering algorithm, as defined here:
	// https://en.wikipedia.org/wiki/Floyd%E2%80%93Steinberg_dithering
	// returns image as a matrix of integer values (one value for each pixel)
	public int[][] ditherBW() {
		// create and fill in matrix with integer magnitudes
		int[][] ditheredImg = new int[img.getHeight()][img.getWidth()];
		for (int y = 0; y < ditheredImg.length; y++)
			for (int x = 0; x < ditheredImg[y].length; x++) {
				Color pixel = new Color(img.getRGB(x, y));
				/*
				 * ditheredImg[y][x] = (int) Math.sqrt(Math.pow(pixel.getRed(),
				 * 2) + Math.pow(pixel.getGreen(), 2) +
				 * Math.pow(pixel.getBlue(), 2));
				 */
				// for some reason this provides a better monochromatic version
				// of the image than if we took the pixel magnitude
				ditheredImg[y][x] = (pixel.getRed() + pixel.getBlue() + pixel
						.getGreen()) / 3;
			}

		// perform dithering:
		for (int y = 0; y < ditheredImg.length; y++) {
			for (int x = 0; x < ditheredImg[y].length; x++) {
				int oldVal = ditheredImg[y][x];
				int newVal = 255;
				if (oldVal < 128)
					newVal = 0;
				ditheredImg[y][x] = newVal;
				int error = oldVal - newVal;
				if (x < ditheredImg[y].length - 1)
					ditheredImg[y][x + 1] += (int) ((7.0 / 16) * error);
				if (x > 1 && y < ditheredImg.length - 1)
					ditheredImg[y + 1][x - 1] += (int) ((3.0 / 16) * error);
				if (y < ditheredImg.length - 1)
					ditheredImg[y + 1][x] += (int) ((5.0 / 16) * error);
				if (x < ditheredImg[y].length - 1 && y < ditheredImg.length - 1)
					ditheredImg[y + 1][x + 1] += (int) ((1.0 / 16) * error);
			}
		}
		return ditheredImg;
	}

	// follows the same idea as ditherBW(), but does it for each color channel
	// instead of treating img like it's black and white
	public int[][][] ditherRGB() {
		return null;
	}
}
