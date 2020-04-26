/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.mapgen;

import java.util.ArrayList;
import java.util.List;

//Tree used for Space Partition map generation
public class PartitionTree {
	
	protected final List<PartitionTree> children = new ArrayList<>(2);
	protected final PartitionTree parent;
	protected final Point min;	//Upper left corner of partition
	protected final Point max;	//Lower right corner of partition

	/**
	 * Constructor
	 * @param min
	 * 		Upper left corner of partition
	 * @param max
	 * 		Lower right corner of partition
	 * @param parent
	 * 		Parent node, or null if this is the root
	 */
	public PartitionTree(Point min, Point max, PartitionTree parent) {
		this.min = min;
		this.max = max;
		this.parent = parent;
	}
	
	/**
	 * Returns a list containing the deepest child nodes
	 */
	public List<PartitionTree> flatten() {
		List<PartitionTree> nodes = new ArrayList<>();
		
		if(children.isEmpty()) {
			nodes.add(this);
		} else {
			for(PartitionTree child : children) nodes.addAll(child.flatten());
		}
		return nodes;
	}
	
	public List<PartitionTree> children() { return children; }
	public Point min() { return min; } 
	public Point max() { return max; }
	public PartitionTree parent() { return parent; }
}
