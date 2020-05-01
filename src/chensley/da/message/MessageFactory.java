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
	
	public static Message actionMove(Entity entity, int dx, int dy) {
		Message msg = new Message(Message.ACTION_MOVE);
		msg.put("entity", entity);
		msg.put("dx", dx);
		msg.put("dy", dy);
		return msg;
	}
	public static Message appStart() { return new Message(Message.APP_START); }
	public static Message appUpdate() { return new Message(Message.APP_UPDATE); }
	public static Message awaitInput() { return new Message(Message.AWAIT_INPUT); }
	public static Message termRefresh(Entity entity) {
		Message msg = new Message(Message.TERM_REFRESH);
		msg.put("entity", entity);
		return msg;
	}
	public static Message turnEnd() { return new Message(Message.TURN_END); }
	public static Message turnStart() { return new Message(Message.TURN_START); }
}
