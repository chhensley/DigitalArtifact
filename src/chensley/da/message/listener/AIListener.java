/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import java.util.Queue;

import chensley.da.ecs.Component;
import chensley.da.ecs.Entity;
import chensley.da.ecs.EntityView;
import chensley.da.message.Message;
import chensley.da.message.MessageFactory;
import chensley.da.message.MessageManager;
import chensley.da.message.MessageStack;
import chensley.da.util.Util;
import squidpony.squidgrid.FOV;
import squidpony.squidmath.AStarSearch;
import squidpony.squidmath.AStarSearch.SearchType;
import squidpony.squidmath.Coord;

/**
 * Helper class for registering action listeners
 */
public class AIListener {
	private AIListener() {};
	
	private static double[][] opacityMap;
	private static FOV fov = new FOV(FOV.SHADOW);
	
	private static void switchState(Entity entity, String state, MessageStack stack) {
		entity.ai().setState(state);
		stack.publish(MessageFactory.aiState(entity));
	}
	
	public static void register(MessageManager msgMgr) {
		
		msgMgr.register(Message.TURN_START, (msg, ctxt)-> {
			ctxt.stack().publish(MessageFactory.aiPreprocess());
		});
		
		//Precalculates opacity map and adds individual AI action to map
		msgMgr.register(Message.AI_PREPROCESS, (msg, ctxt)->{
			opacityMap = Util.opacityMap(ctxt.config().map().width(), 
					ctxt.config().map().height(), ctxt.mgr().with(Component.PHYSICS, Component.POSITION));
			
			EntityView view = ctxt.mgr().with(Component.AI);
			for(Entity entity : view)
				ctxt.stack().publish(MessageFactory.aiState(entity));
		});
		
		msgMgr.register("state_wait", (msg, ctxt)-> {
			//If this entity sees the player attack!
			Entity entity = msg.object("entity", Entity.class);
			double[][] lightMap = fov.calculateFOV(opacityMap, entity.position().x(), entity.position().y(), 
					entity.vision().range());
			if(lightMap[ctxt.mgr().player().position().x()][ctxt.mgr().player().position().y()] != 0.0) {
				switchState(entity, "attack", ctxt.stack());
				return;
			}
		});
		
		msgMgr.register("state_attack", (msg, ctxt)-> {
			//If this entity can't see the player, wait
			Entity entity = msg.object("entity", Entity.class);
			double[][] lightMap = fov.calculateFOV(opacityMap, entity.position().x(), entity.position().y(), 
					entity.vision().range());
			
			Entity player = ctxt.mgr().player();
			if(lightMap[player.position().x()][player.position().y()] == 0.0) {
				switchState(entity, "wait", ctxt.stack());
				return;
			}
			
			//Otherwise, take the shortest path towards the player
			double[][] impassibleMap = Util.impassibleMap(opacityMap.length, opacityMap[0].length, 
					ctxt.mgr().with(Component.POSITION, Component.PHYSICS));
			AStarSearch aStar = new AStarSearch(impassibleMap, SearchType.MANHATTAN);
			Queue<Coord> path = aStar.path(entity.position().x(), entity.position().y(),
				player.position().x(), player.position().y());
			
			if(impassibleMap[path.peek().getX()][path.peek().getY()] == 0)
				ctxt.stack().publish(MessageFactory.actionMove(entity, 
					path.peek().getX() - entity.position().x(), 
					path.peek().getY() - entity.position().y()
			));
		});
	};
}
