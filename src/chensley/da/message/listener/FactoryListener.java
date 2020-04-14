/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import chensley.da.message.MessageManager;
import chensley.da.ecs.Entity;
import chensley.da.ecs.components.Position;
import chensley.da.message.Message.MessageId;

/**
 * Helper class for registering listeners related to creating entities
 */
public class FactoryListener {

	private FactoryListener() {};
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(MessageId.APP_START, (msg, ctxt)->{
			Entity player = ctxt.mgr().create("player");
			player.setPosition(new Position(100, 100));
			ctxt.mgr().setPlayer(player);
		});
	}
}
