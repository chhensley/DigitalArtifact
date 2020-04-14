/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message;

import chensley.da.ecs.Entity;
import chensley.da.message.Message.MessageId;

/**
 * Helper class for generating messages
 */
public class MessageFactory {
	
	private MessageFactory() {};
	
	public static Message appStart() { return new Message(MessageId.APP_START, null); }
	public static Message termRefresh(Entity entity) { return new Message(Message.MessageId.APP_START, entity); }
}
