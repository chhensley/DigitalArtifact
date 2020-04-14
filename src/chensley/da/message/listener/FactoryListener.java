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
			
			for(int i = 0; i < 500; i++) {
				int x = ctxt.rng().nextInt(0, 199);
				int y = ctxt.rng().nextInt(0, 199);
				
				if(ctxt.mgr().at(x, y).isEmpty()) {
					Entity entity = ctxt.mgr().create("wall");
					entity.setPosition(new Position(x, y));
				}
			}
		});
	}
}
