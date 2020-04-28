/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.util;

import java.util.BitSet;

/**
 * Two dimensional grid of boolean values
 * For FOV calculations
 */
public class BoolMap {
	private final BitSet map;
	private final int width;
	private final int height;
	
	/**
	 * Constructor
	 * @param width
	 * 		Grid with
	 * @param height
	 * 		Grid height
	 */
	public BoolMap(int width, int height) {
		map = new BitSet(width * height);
		this.width = width;
		this.height = height;
	}
	
	public Boolean get(int x, int y) {
		return x < width && y < height && map.get(x * width + y);
	}
	
	public void set(boolean bool, int x, int y) {
		if (x >= width || y >= height) throw new IndexOutOfBoundsException(x + "," + y);
		map.set(x * width + y, bool);
	}
}
