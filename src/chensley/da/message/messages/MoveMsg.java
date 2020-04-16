/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.messages;

import chensley.da.ecs.Entity;

/**
 * Message body for entity movement messages
 */
public class MoveMsg {
	private final Entity entity;
	private final int dx;
	private final int dy;
	
	public MoveMsg(Entity entity, int dx, int dy) {
		this.entity = entity;
		this.dx = dx;
		this.dy = dy;
	}
	
	public Entity entity() { return entity; }
	public int dx() { return dx; }
	public int dy() { return dy; }
}
