/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

/**
 * Game physics
 */
public class Physics {
	
	//true if the object blocks movement
	private final boolean impassible;
	
	//true if the object blocks line of sight
	private final boolean opaque;
	
	public Physics(boolean impassible, boolean opaque) {
		this.impassible = impassible;
		this.opaque = opaque;
	}
	
	public Physics(Physics physics) {
		this.impassible = physics.impassible;
		this.opaque = physics.opaque;
	}
	
	public boolean isImpassible() { return impassible; }
	public boolean isOpaque() { return opaque; }
}
