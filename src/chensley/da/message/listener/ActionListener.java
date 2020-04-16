/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import chensley.da.ecs.Component;
import chensley.da.ecs.Entity;
import chensley.da.ecs.EntityView;
import chensley.da.ecs.components.Position;
import chensley.da.message.Message.MessageId;
import chensley.da.message.MessageFactory;
import chensley.da.message.MessageManager;
import chensley.da.message.messages.MoveMsg;
/**
 * Helper class for registering UI related listeners
 */
public class ActionListener {
	private ActionListener() {};
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(MessageId.ACTION_MOVE, (msg, ctxt)->{
			MoveMsg moveMsg = (MoveMsg)msg;
			int x = moveMsg.entity().position().x() + moveMsg.dx();
			int y = moveMsg.entity().position().y() + moveMsg.dy();
			
			//Prevent movement out of bounds
			if(x < 0 || x >= ctxt.config().map().width() || y < 0 || y >= ctxt.config().map().height()) return;
			
			EntityView view = ctxt.mgr().at(x, y).with(Component.PHYSICS);
			
			//Prevent movement into occupied squares
			for(Entity entity : view) {
				if(entity.physics().isImpassible()) return;
			}
			
			moveMsg.entity().setPosition(new Position(x, y));
		});
	}
}
