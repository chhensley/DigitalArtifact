/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

public class Vision {
	private final int range;
	
	public Vision(int range) {
		this.range = range;
	}
	
	public Vision(Vision vision) {
		this.range = vision.range();
	}
	
	public int range() { return range; }
}
