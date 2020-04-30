/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.util;

import java.awt.Color;

import chensley.da.ecs.Entity;
import chensley.da.ecs.EntityView;

/**
 * Static utility functions
 */
public class Util {
	
	private Util() {};
	
	/**
	 * Create an AWT color from an RGB value and separate alpha
	 * @param rgb
	 * 		RGB value as hex value 0xRRGGBB
	 * @param alpha
	 * 		Alpha value between 0 and 1
	 * @return
	 * 		Color
	 */
	public static Color color(int rgb, double alpha) {
		int alphaHex = (int)Math.round(alpha * 0xFF);
		return new Color(rgb + (alphaHex * 0x1000000), true);
	}
	
	/**
	 * Create an opaque AWT color from an RGB value
	 * @param rgb
	 * 		RGB value as hex value 0xRRGGBB
	 * @return
	 * 		Color
	 */
	public static Color color(int rgb) { return color(rgb, 1); }
	
	/**
	 * Calculates the distance between two points
	 * @param x1
	 * 		X coordinate of first point
	 * @param y1
	 * 		Y coordinate of first point
	 * @param x2
	 * 		X coordinate of second point
	 * @param y2
	 * 		Y coordinate of second point
	 * @return
	 * 		distance between the two points
	 */
	public static double distance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((double)Math.pow(x2 - x1, 2) + (double)Math.pow(y2 - y1, 2));
	}
	
	/**
	 * Returns the file name from a full url
	 * @param url
	 * 		URL
	 * @return
	 * 		Path
	 */
	public static String fileNameFromUrl(String url) {
		return url.substring((url.lastIndexOf('/') + 1));
	}
	
	/**
	 * Builds a map marking opaque objects for use by FOV calculations
	 * @param width
	 * 		Game map width
	 * @param height
	 * 		Game map height
	 * @param view
	 * 		A view containing entities with physics and position
	 * @return
	 * 		Opacity map
	 */
	public static double[][] opacityMap(int width, int height, EntityView view) {
		double[][] opacityMap = new double[width][height];
		
		for(Entity entity : view) {
			if (entity.physics().isOpaque())
				opacityMap[entity.position().x()][entity.position().y()] = 1.0;
		}
		
		return opacityMap;
	}
}
