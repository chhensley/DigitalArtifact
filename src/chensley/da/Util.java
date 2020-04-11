/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da;

import java.awt.Color;

/**
 * Static utility functions
 */
public class Util {
	
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
		return new Color((rgb * 0x100) + alphaHex, true);
	}
	
	/**
	 * Create an opaque AWT color from an RGB value
	 * @param rgb
	 * 		RGB value as hex value 0xRRGGBB
	 * @return
	 * 		Color
	 */
	public static Color color(int rgb) { return color(rgb, 1); }
}
