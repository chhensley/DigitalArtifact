/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Game message
 */
public class Message {
	//Message IDs
	public static final String ACTION_MOVE = "action_move";
	public static final String APP_START = "app_start";
	public static final String APP_UPDATE = "app_update";
	public static final String AWAIT_INPUT = "await_input";
	public static final String TERM_REFRESH = "term_refresh";
	public static final String TURN_END = "turn_end";
	public static final String TURN_START = "turn_start";
	
	//Exception thrown when it fails to load appropriate property from message body
	public class MessageFormatException extends Exception {
		private static final long serialVersionUID = 9211465105597620918L;
		public MessageFormatException(String msg) {
			super(msg);
		}
		
		public MessageFormatException(String msg, Exception e) {
			super(msg, e);
		}
	}
	
	private final String id;
	private final Map<String, Object> body = new HashMap<>();
	
	public Message(String id) {
		this.id = id;
	}
	
	public String id() { return id; }
	
	public void put(String key, Object value) { body.put(key, value); };
	public void put(String key, int value ) { body.put(key, new Integer(value)); }
	public Object object(String key) { return body.get(key); }
	public <T> T object(String key, Class<T> type) throws MessageFormatException {
		try {
			return type.cast(body.get(key));
		} catch (ClassCastException e) {
			throw new MessageFormatException("failed to find property " + key + " of type " + type.getCanonicalName(), e);
		}
	}
	
	public int integer(String key) throws MessageFormatException {
		try {
			return (Integer)body.get(key);
		} catch(ClassCastException | NullPointerException e) {
			throw new MessageFormatException("failed to find property " + key + " of type integer", e);
		}
	}
	
	public String string(String key) {
		Object value = body.get(key);
		return value != null ? value.toString() : null;
	}
}
