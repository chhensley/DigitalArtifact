/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.message.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import chensley.da.ecs.Entity;
import chensley.da.ecs.EntityManager;
import chensley.da.ecs.components.Position;
import chensley.da.mapgen.CityTree;
import chensley.da.mapgen.PartitionTree;
import chensley.da.mapgen.Point;
import chensley.da.message.Message;
import chensley.da.message.MessageManager;
import chensley.da.message.MessageManager.Context;

/**
 * Helper class for registering listeners related to creating entities
 */
public class FactoryListener {
	
	//A 2 Dimensional game map
	private static class GameMap {
		private final int width;
		private final int height;
		
		//Flattened game map
		//Cells are entities defined in the entity manager
		private final List<String> cells = new ArrayList<>();
		
		public GameMap(int width, int height) {
			this.width = width;
			this.height = height;
			
			//Build empty game map
			for(int i = 0; i <= width * height; i++) cells.add(null);
		}
		
		public void set(String cell, int x, int y) {
			if (x < 0 || y < 0 || x >= width || y >= height) 
				throw new IndexOutOfBoundsException(x + "," + y);
			
			cells.set(x * width + y, cell);
		}
		
		public String get(int x, int y) {
			if (x < 0 || y < 0 || x >= width || y >= height) 
				throw new IndexOutOfBoundsException(x + "," + y);
			
			return cells.get(x * width + y);
		}
	}

	private FactoryListener() {};
	
	//Returns midpoint on a line between two points
	private static Point center(Point first, Point second) {
		return new Point((first.x() + second.x())/2, (first.y() + second.y())/2);
	}
	
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
	
	//Draws a partitioned node on the map
	private static void drawNode(GameMap map, PartitionTree node) {
		//Draw vertical walls
		for(int x = node.min().x(); x <= node.max().x(); x++) {
			map.set("wall", x, node.min().y());
			map.set("wall", x, node.max().y());
		}
		
		//Draw horizontal walls
		for(int y = node.min().y(); y <= node.max().y(); y++) {
			map.set("wall", node.min().x(), y);
			map.set("wall", node.max().x(), y);
		}
	}
	
	//Carves doors into the walls of the partitioned space
	private static void carveDoors(GameMap map, PartitionTree node, Context ctxt) {
		int doors = ctxt.rng().nextInt(
			ctxt.config().mapGen().minDoors(),
			ctxt.config().mapGen().maxDoors()
		);
		
		for(int i = 0; i < doors; i++) {
			int x = 0;
			int y = 0;
			
			
			if (ctxt.rng().nextBool()) {
				//Cut a door in a vertical wall
				x = ctxt.rng().nextInt(node.min().x() + 1, node.max().x() - 1);
				y = ctxt.rng().nextBool()?node.min().y():node.max().y();
				
				if (y == 0) y = node.max().y();
				if (y == ctxt.config().map().height() - 1) y = node.min().y();
			} else {
				//Cut a door in the horizontal wall
				x = ctxt.rng().nextBool()?node.min().x():node.max().x();
				y = ctxt.rng().nextInt(node.min().y() + 1, node.max().y() - 1);
				
				if (x == 0) x = node.max().x();
				if (x == ctxt.config().map().width() -1) x = node.min().x();
			}
			
			map.set(null, x, y);
		}
	}
	
	//Calculates player start position
	public static Point startPosition(CityTree tree) {
		PartitionTree child0 = tree.children().get(0);
		PartitionTree child1 = tree.children().get(1);
		
		Point first = null;
		Point second = null;
		
		//Calculate the center point of the widest street
		if (tree.isVertical()) {
			first = new Point(child0.max().x(), child0.min().y());
			second = new Point(child1.min().x(), child1.max().y());
		} else {
			first = new Point(child0.min().x(), child0.max().y());
			second = new Point(child1.max().x(), child1.min().y());
		}
		
		return center(first, second);
	}

	
	public static void register(MessageManager msgMgr) {
		msgMgr.register(Message.APP_START, (msg, ctxt)->{
			GameMap gameMap = new GameMap(ctxt.config().map().width(), ctxt.config().map().height());
			
			CityTree tree = new CityTree(new Point(0, 0), new Point(199, 199), ctxt.rng().nextBool(), ctxt.config(), ctxt.rng());
			for(PartitionTree node : tree.flatten()) {
				drawNode(gameMap, node);
				carveDoors(gameMap, node, ctxt);
			}
			Point start = startPosition(tree);
			gameMap.set("player", start.x(), start.y());
			
			ctxt.logger().log(Level.INFO, "mapping location");
			create(gameMap, ctxt.mgr());
		});
	}
}
