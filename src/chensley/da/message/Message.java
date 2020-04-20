/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message;

/**
 * Game message
 */
public class Message {
	public enum MessageId {
		ACTION_MOVE,
		APP_START,
		APP_UPDATE,
		AWAIT_INPUT,
		TERM_REFRESH,
		TURN_START,
		TURN_END
	}
	
	private final MessageId id;
	private final Object body;
	
	public Message(MessageId id, Object body) {
		this.id = id;
		this.body = body;
	}
	
	public MessageId id() { return id; }
	public Object body() { return body; }
}
