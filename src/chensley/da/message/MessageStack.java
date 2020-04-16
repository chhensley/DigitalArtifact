/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message;

import java.util.ArrayDeque;
import java.util.Deque;

import chensley.da.message.Message.MessageId;

/**
 * Game message stack
 * Parent of MessageManager
 */
public class MessageStack {
	private final Deque<Message> stack = new ArrayDeque<>();
	
	public void publish(Message msg) { stack.push(msg); }
	public boolean isEmpty() { return stack.isEmpty(); }
	
	protected Message pop() { return stack.pop(); }
}
