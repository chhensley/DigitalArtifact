/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.util;

import java.util.Random;

/**
 * Exposes RNG functions to message consumers
 */
public class RandomNumberGenerator {
	private final Random random;
	
	public RandomNumberGenerator() {
		random = new Random();
	}
	
	public RandomNumberGenerator(long seed) {
		random = new Random(seed);
	}
	
	public boolean nextBool() {
		return random.nextBoolean();
	}
	
	/**
	 * Returns a random integer in range
	 * @param min
	 * 		Minimum value, inclusive
	 * @param max
	 * 		Maximum value, inclusive
	 * @return
	 * 		Random integer
	 */
	public int nextInt(int min, int max) {
		return random.nextInt(max -  min + 1) + min;
	}
}
