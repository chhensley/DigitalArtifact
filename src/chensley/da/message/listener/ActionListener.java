/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import java.util.logging.Level;

import chensley.da.ecs.Component;
import chensley.da.ecs.Entity;
import chensley.da.ecs.components.Position;
import chensley.da.message.Message;
import chensley.da.message.MessageFactory;
import chensley.da.message.MessageManager;
/**
 * Helper class for registering action listeners
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

			
			
			for(Entity viewEntity : ctxt.mgr().at(x, y).with(Component.PHYSICS)) {
				//If this square can be damaged, perform attack
				if(viewEntity.has(Component.DESTRUCTABLE)) {
					ctxt.logger().log(Level.INFO, entity.label() + " attacks " + viewEntity.label());
					int impDmg = ctxt.rng().nextInt(1, 3);
					ctxt.stack().publish(MessageFactory.actionDamage(entity, viewEntity, impDmg));
				}
				
				//Prevent movement into occupied squares
				if(viewEntity.physics().isImpassible()) return;
			}
			
			entity.setPosition(new Position(x, y));
		});
		
		msgMgr.register(Message.ACTION_DAMAGE, (msg, ctxt)->{
			Entity tgt  = msg.object("tgt", Entity.class);
			int impDmg = msg.integer("impDmg");
			int impArmor = tgt.destructable().impactArmor();
			int dmg = impDmg > impArmor ? impDmg - impArmor : 0;
			tgt.destructable().addDamage(dmg);
			if (tgt.destructable().currentHitPoints() < 1) {
				ctxt.stack().publish(MessageFactory.onDestroy(tgt.destructable().onDestroy(), tgt));
			}
			ctxt.logger().log(Level.INFO, tgt.label() + " takes " + dmg + " damage");
		});
	}
}
