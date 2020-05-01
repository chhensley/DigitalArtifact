/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import chensley.da.Config;
import chensley.da.ecs.EntityManager;
import chensley.da.message.Message.MessageFormatException;
import chensley.da.util.RandomNumberGenerator;

/**
 * Centralizes message handling
 */
public class MessageManager extends MessageStack {
	
	//Callback for message consumers
	public interface Consumer {
		/**
		 * Consumes a message
		 * A consumer changes the game state by adding/modifying/deleting entities
		 * or by pushing new messages to the message satack
		 * @param msg
		 * 		Message body
		 * @param ctxt
		 * 		Message context
		 */
		public void consume(Message msg, Context context) throws MessageFormatException;
	}
	
	/**
	 * Used to pass initialized objects to the Consumer class 
	 */
	public class Context {
		private final Config config;
		private final RandomNumberGenerator rng;
		private final Logger logger;
		private final EntityManager mgr;
		private final MessageStack stack;
		
		public Context(Config config, EntityManager mgr, MessageStack stack, RandomNumberGenerator rng, Logger logger) {
			this.config = config;
			this.rng = rng;
			this.mgr = mgr;
			this.stack = stack;
			this.logger = logger;
		}
		
		public Config config() { return config; }
		public Logger logger() { return logger; }
		public EntityManager mgr() { return mgr; }
		public RandomNumberGenerator rng() { return rng; }
		public MessageStack stack() { return stack; }
	}
	
	
	private Map<String, List<Consumer>> consumers = new HashMap<>();
	
	private final Context ctxt;
	
	public MessageManager(Config config, EntityManager mgr, RandomNumberGenerator rng, Logger logger) {
		//Initialize context
		ctxt = new Context(config, mgr, this, rng, logger);
	}
	
	/**
	 * Registers a consumer
	 * @param id
	 * 		The message id the consumer is listening for
	 * @param consumer
	 * 		Consumer callback
	 */
	public void register(String id, Consumer consumer) {
		if(!consumers.containsKey(id)) consumers.put(id, new ArrayList<>());
		consumers.get(id).add(consumer);
	}
	
	/**
	 * Consumes the next message in the stack
	 */
	public void consume() {
		Message msg = pop();
		//If there are no registered listeners do nothing
		if(!consumers.containsKey(msg.id())) return; 
		
		//Otherwise send the message to each registered consumer
		for(Consumer consumer : consumers.get(msg.id())) {
			try {
				consumer.consume(msg, ctxt);
			} catch (MessageFormatException e) {
				ctxt.logger().log(Level.SEVERE, "failed to process message " + msg.id(), e);
			}
		}
	}
}
