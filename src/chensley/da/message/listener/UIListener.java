/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import javax.swing.SwingUtilities;

import chensley.da.message.MessageManager;
import chensley.da.message.MessageManager.Context;
import chensley.da.ecs.Component;
import chensley.da.ecs.Entity;
import chensley.da.ecs.components.Position;
import chensley.da.ecs.components.Tile;
import chensley.da.message.Message.MessageId;
import chensley.da.ui.GUI;

/**
 * Helper class for registering UI related listeners
 */
public class UIListener {
	private static GUI gui;

	private UIListener() {};
	
	/**
	 * Draws visible game map
	 * @param center
	 * 		Entity representing the center of the map
	 * @param ctxt
	 * 		Context object passed from consumer
	 */
	private static void drawVisible(Entity center, Context ctxt) {
		//Draw empty squares
		gui.termClear();
		Tile empty = ctxt.mgr().factory().tiles().get("empty");
		for(int x = 0; x < ctxt.config().term().width(); x++)
			for(int y = 0; y < ctxt.config().term().height(); y++) {
				gui.termDraw(empty.icon(), empty.color(), x, y);
			}
		
		//Set Upper and lower X bounds of visible area
		int minX = center.position().x() - ctxt.config().term().width() / 2;
		if(minX < 0) {
			minX = 0;
		} else if (minX + ctxt.config().term().width() >= ctxt.config().map().width()) {
			minX = ctxt.config().map().width() - ctxt.config().term().width();
		}
		
		int maxX = minX + ctxt.config().term().width();
		
		//Set uppar and lower Y bounds of visible area
		int minY = center.position().y() - ctxt.config().term().height() / 2;
		if(minY < 0) { 
			minY = 0;
		} else if (minY + ctxt.config().term().height() >= ctxt.config().map().height()) {
			minY = ctxt.config().map().height() - ctxt.config().term().height(); 
		}
		int maxY = minY + ctxt.config().term().height();
		
		
		//Draw entities
		for(Entity entity : ctxt.mgr().between(minX, minY, maxX, maxY).with(Component.TILE)) {
			gui.termDraw(entity.tile().icon(), entity.tile().color(), entity.position().x() - minX, entity.position().y() - minY);
		}
	}
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(MessageId.APP_START, (msg, ctxt)->{
			gui = new GUI(ctxt.config(), ctxt.logger());
			SwingUtilities.invokeLater(gui::launch);
			msgMgr.publish(MessageId.TERM_REFRESH, ctxt.mgr().player());
		});
		
		msgMgr.register(MessageId.TERM_REFRESH, (msg, ctxt)->{
			SwingUtilities.invokeLater(()->{
				drawVisible((Entity) msg, ctxt);
			});
		});
	}
}
