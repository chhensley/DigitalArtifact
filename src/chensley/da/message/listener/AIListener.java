/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import java.util.logging.Level;

import chensley.da.ecs.Component;
import chensley.da.ecs.Entity;
import chensley.da.ecs.EntityView;
import chensley.da.message.Message;
import chensley.da.message.MessageFactory;
import chensley.da.message.MessageManager;

/**
 * Helper class for registering action listeners
 */
public class AIListener {
	private AIListener() {};
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(Message.TURN_START, (msg, ctxt)-> {
			EntityView view = ctxt.mgr().with(Component.AI);
			for(Entity entity : view)
				ctxt.stack().publish(MessageFactory.aiState(entity));
			ctxt.stack().publish(MessageFactory.aiPreprocess());
		});
		
		msgMgr.register(Message.AI_PREPROCESS, (msg, ctxt)->{
			//Code for preprocessing game state for use by AIs will go here
		});
	};
}
