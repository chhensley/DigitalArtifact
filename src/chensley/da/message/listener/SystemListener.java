/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import chensley.da.message.MessageManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import chensley.da.Updater;
import chensley.da.message.Message.MessageId;
/**
 * Helper class for registering system related listeners
 */
public class SystemListener {
	private SystemListener() {};
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(MessageId.APP_UPDATE, (msg, ctxt)->{
			//Updater(String downloadUrl, String runCmd, String jarPath, Logger logger)
			Updater updater = new Updater(
				ctxt.config().update().downloadUrl(),
				ctxt.config().update().cmd(),
				ctxt.config().update().jar(),
				ctxt.logger()
			);
			
			ctxt.logger().log(Level.INFO, "updating spectre digital assitant");
			updater.update();
		});
	}
}
