/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import java.util.logging.Level;

import chensley.da.message.Message;
import chensley.da.message.MessageFactory;
import chensley.da.message.MessageManager;
import chensley.da.util.Updater;

/**
 * Helper class for registering system related listeners
 */
public class SystemListener {
	private SystemListener() {};
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(Message.APP_UPDATE, (msg, ctxt)->{
			//Updater(String downloadUrl, String runCmd, String jarPath, Logger logger)
			Updater updater = new Updater(
				ctxt.config().update().downloadUrl(),
				ctxt.config().update().cmd(),
				ctxt.config().update().jar(),
				ctxt.logger()
			);
			
			ctxt.stack().publish(MessageFactory.awaitInput());
			ctxt.logger().log(Level.INFO, "updating spectre digital assitant");
			updater.update();
		});
	}
}
