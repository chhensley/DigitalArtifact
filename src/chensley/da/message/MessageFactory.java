/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message;

import chensley.da.ecs.Entity;

/**
 * Helper class for generating messages
 */
public class MessageFactory {
	
	private MessageFactory() {};
	
	/**
	 * Creates action damage message
	 * @param src
	 * 		Entity dealing the damages
	 * @param tgt
	 * 		Entity being damaged
	 * @param impDmg
	 * 		Impact damage
	 * @return
	 * 		action_damage message
	 */
	public static Message actionDamage(Entity src, Entity tgt, int impDmg) {
		Message msg = new Message(Message.ACTION_DAMAGE);
		msg.put("src", src);
		msg.put("tgt", tgt);
		msg.put("impDmg", impDmg);
		return msg;
	}
	
	/**
	 * Creates action move message
	 * @param entity
	 * 		Entity that's moving
	 * @param dx
	 * 		Change in x positioon
	 * @param dy
	 * 		Change in y position
	 * @return
	 * 		action_move message
	 */
	public static Message actionMove(Entity entity, int dx, int dy) {
		Message msg = new Message(Message.ACTION_MOVE);
		msg.put("entity", entity);
		msg.put("dx", dx);
		msg.put("dy", dy);
		return msg;
	}
	
	/**
	 * Creates a message to process the current AI state of an entity
	 * @param entity
	 * 		Entity with an ai component
	 * @return
	 * 		message for the entity's current ai state
	 */
	public static Message aiState(Entity entity) {
		Message msg = new Message(entity.ai().msg());
		msg.put("entity", entity);
		return msg;
	}
	
	/**
	 * Creates a message destroy an entity
	 * @param id
	 * 		Message id to trigger on entity destruction
	 * @param entity
	 * 		Entity with destructable component
	 * @return
	 */
	public static Message onDestroy(String id, Entity entity) {
		Message msg = new Message(id);
		msg.put("entity", entity);
		return msg;
	}

	/**
	 * Creates a terminal refresh message
	 * @param entity
	 * 		Entity on which the terminal is centered
	 * @return
	 * 		term_refresh message
	 */
	public static Message termRefresh(Entity entity) {
		Message msg = new Message(Message.TERM_REFRESH);
		msg.put("entity", entity);
		return msg;
	}
	
	public static Message aiPreprocess() { return new Message(Message.AI_PREPROCESS); }
	public static Message appStart() { return new Message(Message.APP_START); }
	public static Message appUpdate() { return new Message(Message.APP_UPDATE); }
	public static Message awaitInput() { return new Message(Message.AWAIT_INPUT); }
	public static Message turnEnd() { return new Message(Message.TURN_END); }
	public static Message turnStart() { return new Message(Message.TURN_START); }
}
