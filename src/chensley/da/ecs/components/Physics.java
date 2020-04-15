/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

public class Physics {
	private final boolean impassible;
	private final boolean opaque;
	
	public Physics(boolean impassible, boolean opaque) {
		this.impassible = impassible;
		this.opaque = opaque;
	}
	
	public boolean isImpassible() { return impassible; }
	public boolean isOpaque() { return opaque; }
}
