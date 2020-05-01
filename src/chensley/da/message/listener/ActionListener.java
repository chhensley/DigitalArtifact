/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import chensley.da.ecs.Component;
import chensley.da.ecs.Entity;
import chensley.da.ecs.components.Position;
import chensley.da.message.Message;
import chensley.da.message.MessageManager;
/**
 * Helper class for registering UI related listeners
 */
public class ActionListener {
	private ActionListener() {};

	public static void register(MessageManager msgMgr) {
		msgMgr.register(Message.ACTION_MOVE, (msg, ctxt)->{
			Entity entity = msg.object("entity", Entity.class);
			int x = entity.position().x() + msg.integer("dx");
			int y = entity.position().y() + msg.integer("dy");
			
			//Prevent movement out of bounds
			if(x < 0 || x >= ctxt.config().map().width() || y < 0 || y >= ctxt.config().map().height()) return;

			
			//Prevent movement into occupied squares
			for(Entity viewEntity : ctxt.mgr().at(x, y).with(Component.PHYSICS)) {
				if(viewEntity.physics().isImpassible()) return;
			}
			
			entity.setPosition(new Position(x, y));
		});
	}
}
