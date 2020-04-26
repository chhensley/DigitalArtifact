/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.mapgen;

import chensley.da.Config;
import chensley.da.RandomNumberGenerator;

//Partition tree for generating city block
public class CityTree extends PartitionTree {
	
	private final Config config;
	private final RandomNumberGenerator rng;
	private final boolean isVertical;
	private final int depth;
	
	/**
	 * Constructor for root node
	 * @param min
	 * 		Upper left corner
	 * @param max
	 * 		Bottom right corner
	 * @param isVertical
	 * 		true if this should divide vertically
	 * 		false if this should divide horizontally
	 * @param config
	 * 		Game config object
	 * @param rng
	 * 		Seeded random number generator
	 */
	public CityTree(Point min, Point max, boolean isVertical, Config config, RandomNumberGenerator rng) {
		super(min, max, null);
		this.config = config;
		this.rng = rng;
		this.isVertical = isVertical;
		this.depth = config.mapGen().partition().depth();
		
		partition();
	}
	
	/**
	 * Constructor for child nodes
	 * @param min
	 * 		Upper left corner
	 * @param max
	 * 		Lower right corner
	 * @param parent
	 * 		Parent node
	 */
	public CityTree(Point min, Point max, CityTree parent) {
		super(min, max, parent);
		this.rng = parent.rng;
		this.config = parent.config;
		this.isVertical = !parent.isVertical;
		this.depth = parent.depth - 1;
		
		partition();
	}
	
	//Partitions space
	private void partition() {
		//If this is the deepest legal node, do not partition
		if (depth == 0) return;
		
		int minSize = isVertical ? config.mapGen().partition().minWidth() : config.mapGen().partition().minHeight();
		Point child0Max = null;
		Point child1Min = null;
		
		//Only divide the space if it is big enough to split
		if (isVertical && max.x() - min.x() > minSize * 2) {
			int width = rng.nextInt(minSize, max.x() - min.x() - minSize);
			child0Max = new Point(min.x() + width - depth, max.y());
			child1Min = new Point(min.x() + width + depth + 1, min.y());
		} else if (!isVertical && max.y() - min.y() > minSize * 2) {
			int height = rng.nextInt(minSize, max.y() - min.y() - minSize);
			child0Max = new Point(max.x(), min.y() + height - depth);
			child1Min = new Point(min.x(), min.y() + height + depth + 1);
		}
		
		//Add children if the space was divided
		if (child0Max != null && child1Min != null) {
			this.children.add(new CityTree(min, child0Max, this));
			this.children.add(new CityTree(child1Min, max, this));
		}
	}
	
	public boolean isVertical() { return isVertical; }
}
