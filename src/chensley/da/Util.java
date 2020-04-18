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
	 * Returns the file name from a full url
	 * @param url
	 * 		URL
	 * @return
	 * 		Path
	 */
	public static String fileNameFromUrl(String url) {
		return url.substring(url.lastIndexOf(('/') + 1));
	}
}
