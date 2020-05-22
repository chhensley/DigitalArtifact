/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import java.awt.event.KeyEvent;
import java.util.logging.Level;

import javax.swing.SwingUtilities;

import chensley.da.ecs.Component;
import chensley.da.ecs.Entity;
import chensley.da.ecs.EntityView;
import chensley.da.ecs.components.Tile;
import chensley.da.message.Message;
import chensley.da.message.MessageFactory;
import chensley.da.message.MessageManager;
import chensley.da.message.MessageManager.Context;
import chensley.da.ui.GUI;
import chensley.da.util.Util;
import squidpony.squidgrid.FOV;

/**
 * Helper class for registering UI related listeners
 */
public class UIListener {
	private static GUI gui;

	private UIListener() {};
	
	/**
	 * Displays about menu
	 * @return
	 * 		Message generated by menu input
	 */
	private static Message displayAbout() {
		gui.showAbout();
		
		for(;;) {
			KeyEvent e = gui.listen();
			String keyText = KeyEvent.getKeyText(e.getKeyCode());
			
			if(keyText.equals("Escape")) {
				gui.showTerm();
				return MessageFactory.awaitInput();
			} else if(keyText.equals("U")) {
				return MessageFactory.appUpdate();
			}
		}
	}

	/**
	 * Draws visible game map
	 * @param center
	 * 		Entity representing the center of the map
	 * 		This entity must have position and vision components
	 * @param ctxt
	 * 		Context object passed from consumer
	 */
	private static void drawVisible(Entity center, Context ctxt) {
		//Draw empty squares
		gui.termClear();
		Tile empty = ctxt.mgr().factory().tiles().get("empty");
		
		//Set upper and lower X bounds of visible area
		int minX = center.position().x() - ctxt.config().term().width() / 2;
		if(minX < 0) {
			minX = 0;
		} else if (minX + ctxt.config().term().width() >= ctxt.config().map().width()) {
			minX = ctxt.config().map().width() - ctxt.config().term().width();
		}
		
		int maxX = minX + ctxt.config().term().width() - 1;
		
		//Set upper and lower Y bounds of visible area
		int minY = center.position().y() - ctxt.config().term().height() / 2;
		if(minY < 0) { 
			minY = 0;
		} else if (minY + ctxt.config().term().height() >= ctxt.config().map().height()) {
			minY = ctxt.config().map().height() - ctxt.config().term().height(); 
		}
		
		int maxY = minY + ctxt.config().term().height() - 1;
		
		//Create light map for player's field of view
		double[][] opacityMap = Util.opacityMap(ctxt.config().map().width(), ctxt.config().map().height(), 
				ctxt.mgr().inRadius(center.position().x(), center.position().y(), center.vision().range())
				.with(Component.PHYSICS));
		FOV fov = new FOV(FOV.SHADOW);
		double[][] lightMap = fov.calculateFOV(opacityMap, center.position().x(), center.position().y(), center.vision().range());
		
		//Draw empty area
		for(int x = 0; x < ctxt.config().term().width(); x++)
			for(int y = 0; y < ctxt.config().term().height(); y++) {
				if (lightMap[x + minX][y + minY] > 0.0) {
					gui.termDraw(empty.icon(), empty.color(), x, y, empty.xOffset(), empty.yOffset());
				} else if (empty.fowColor() != null) {
					gui.termDraw(empty.icon(), empty.fowColor(), x, y, empty.xOffset(), empty.yOffset());
				}
			}
		
		//Draw entities
		EntityView view = ctxt.mgr().between(minX, minY, maxX, maxY).with(Component.TILE);
		double[][] impassibleMap = Util.impassibleMap(ctxt.config().map().width(), 
				ctxt.config().map().height(), view.with(Component.PHYSICS));
		for(Entity entity : view) {
			//Don't draw if this is a passible entity and an impassible entity is also in this position
			if (impassibleMap[entity.position().x()][entity.position().y()] != 0 && 
					(!entity.has(Component.PHYSICS) || !entity.physics().isImpassible())) continue;
			
			//Otherwise draw this entity
			if(lightMap[entity.position().x()][entity.position().y()] > 0.0) {
				gui.termDraw(entity.tile().icon(), entity.tile().color(), 
						entity.position().x() - minX, entity.position().y() - minY, 
						entity.tile().xOffset(), entity.tile().yOffset());
			} else if (entity.tile().fowColor() != null) {
				gui.termDraw(entity.tile().icon(), entity.tile().fowColor(), 
						entity.position().x() - minX, entity.position().y() - minY, 
						entity.tile().xOffset(), entity.tile().yOffset());
			}
		}
		
		gui.termRepaint();
	}
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(Message.APP_GAMEOVER, (msg, ctxt)->{
			ctxt.logger().log(Level.INFO, "Game Over");
			SwingUtilities.invokeLater(()->gui.setHealth(ctxt.mgr().player().destructable().hitPoints(), 0));
			SwingUtilities.invokeLater(gui::showGameOver);
			for(;;) {
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					//Do nothing
				}
			}
		});
		
		msgMgr.register(Message.APP_START, (msg, ctxt)->{
			gui = new GUI(ctxt.config(), ctxt.logger());
			SwingUtilities.invokeLater(gui::launch);
			ctxt.logger().log(Level.INFO, "booting spectre virtual assitant");
			ctxt.logger().log(Level.INFO, "initializing retinal display");
		});
		
		msgMgr.register(Message.AWAIT_INPUT, (msg, ctxt)->{
			KeyEvent e = gui.listen();
			String keyText = KeyEvent.getKeyText(e.getKeyCode());
			gui.showTerm();
			
			if(keyText.equals(ctxt.config().controls().up())) {
				ctxt.stack().publish(MessageFactory.actionMove(ctxt.mgr().player(), 0, -1));
			} else if(keyText.equals(ctxt.config().controls().down())) {
				ctxt.stack().publish(MessageFactory.actionMove(ctxt.mgr().player(), 0, 1));
			} else if(keyText.equals(ctxt.config().controls().left())) {
				ctxt.stack().publish(MessageFactory.actionMove(ctxt.mgr().player(), -1, 0));
			} else if(keyText.equals(ctxt.config().controls().right())) {
				ctxt.stack().publish(MessageFactory.actionMove(ctxt.mgr().player(), 1, 0));
			} else if(keyText.contentEquals(ctxt.config().controls().about())) {
				ctxt.stack().publish(displayAbout());
			} else {
				ctxt.stack().publish(MessageFactory.awaitInput());
			}
		});
		
		msgMgr.register(Message.TERM_REFRESH, (msg, ctxt)-> {
			Entity entity = msg.object("entity", Entity.class);
			SwingUtilities.invokeLater(()->gui.setHealth(entity.destructable().hitPoints(), entity.destructable().currentHitPoints()));
			SwingUtilities.invokeLater(()->drawVisible(entity, ctxt));
		});
		
		msgMgr.register(Message.TURN_START, (msg, ctxt) -> {
			ctxt.stack().publish(MessageFactory.awaitInput());
			ctxt.stack().publish(MessageFactory.termRefresh(ctxt.mgr().player()));
		});
	}
}
