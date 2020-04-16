/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message;

import chensley.da.ecs.Entity;
import chensley.da.message.Message.MessageId;
import chensley.da.message.messages.MoveMsg;

/**
 * Helper class for generating messages
 */
public class MessageFactory {
	
	private MessageFactory() {};
	
	public static Message actionMove(Entity entity, int dx, int dy) { return new Message(MessageId.ACTION_MOVE, new MoveMsg(entity, dx, dy)); }
	public static Message appStart() { return new Message(MessageId.APP_START, null); }
	public static Message awaitInput() { return new Message(MessageId.AWAIT_INPUT, null); }
	public static Message termRefresh(Entity entity) { return new Message(Message.MessageId.TERM_REFRESH, entity); }
	public static Message turnEnd() { return new Message(Message.MessageId.TURN_END, null); }
	public static Message turnStart() { return new Message(Message.MessageId.TURN_START, null); }
}
