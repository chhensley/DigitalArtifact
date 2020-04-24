/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import chensley.da.message.MessageManager;

import java.util.ArrayList;
import java.util.List;

import chensley.da.ecs.Entity;
import chensley.da.ecs.EntityManager;
import chensley.da.ecs.components.Position;
import chensley.da.message.Message.MessageId;

/**
 * Helper class for registering listeners related to creating entities
 */
public class FactoryListener {
	
	//A 2 Dimensional game map
	private static class GameMap {
		private final int width;
		private final int height;
		
		//Game map. Values are entities defined in entities.yml
		private final List<String> cells = new ArrayList<>();
		
		public GameMap(int width, int height) {
			this.width = width;
			this.height = height;
			
			//Build empty game map
			for(int i = 0; i <= width * height; i++) cells.add(null);
		}
		
		public void set(String cell, int x, int y) {
			if (x < 0 || y < 0 || x >= width || y >= height) 
				throw new IndexOutOfBoundsException();
			
			cells.set(x * width + y, cell);
		}
		
		public String get(int x, int y) {
			if (x < 0 || y < 0 || x >= width || y >= height) 
				throw new IndexOutOfBoundsException();
			
			return cells.get(x * width + y);
		}
	}

	private FactoryListener() {};
	
	//Creates entities from map
	private static void create(GameMap map, EntityManager mgr) {
		for(int x = 0; x < map.width; x++)
			for(int y = 0; y < map.height; y++) {
				String cell = map.get(x, y);
				if (cell != null) {
					Entity entity = mgr.create(cell);
					entity.setPosition(new Position(x, y));
					if (cell.equals("player")) mgr.setPlayer(entity);
				}
			}
	}
	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(MessageId.APP_START, (msg, ctxt)->{
			GameMap gameMap = new GameMap(ctxt.config().map().width(), ctxt.config().map().height());
			for(int i = 0; i < 500; i++) {
				int x = ctxt.rng().nextInt(0, 199);
				int y = ctxt.rng().nextInt(0, 199);
				gameMap.set("wall", x, y);
			}
			
			gameMap.set("player", 100, 100);
			create(gameMap, ctxt.mgr());
		});
	}
}
