/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import java.util.logging.Level;

import chensley.da.ecs.Entity;
import chensley.da.message.MessageManager;

/**
 * Helper class for registering entity destroyed listners
 */
public class DestroyListener {
	private DestroyListener() {};
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register("destroy_person", (msg, ctxt)->{
			Entity entity = msg.object("entity", Entity.class);
			ctxt.logger().log(Level.INFO, entity.label() + " is killed");
			entity.setLabel("dead " + entity.label());
			entity.setTile(ctxt.mgr().factory().tiles().get("splat"));
			entity.setDestructable(null);
			entity.setPhysics(null);
			//entity.setVision(null);
		});
	}
}
