/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chensley.da.ecs.components.Position;
import chensley.da.util.Util;

/**
 * An iterable connection of entities
 */
public class EntityView implements Iterable<Entity> {
	protected final List<Entity> entities;
	
	public EntityView() { entities = new ArrayList<>(); }
	public EntityView(List<Entity> entities) { this.entities = entities; }
	
	@Override
	public Iterator<Entity> iterator() {
		return entities.iterator();
	}
	
	/**
	 * Returns true if the view is empty
	 * @return
	 * 		boolean
	 */
	public boolean isEmpty() {
		return entities.isEmpty();
	}
	
	/**
	 * Returns a view with the subset of entities at a given position
	 * @param x
	 * 		X coordinate
	 * @param y
	 * 		Y coordinate
	 * @return
	 * 		All entities at the x, y position
	 */
	public EntityView at(int x, int y) {
		List<Entity> view = new ArrayList<>();
		Position position = new Position(x, y);
		
		for(Entity entity : entities) {
			if(entity.has(Component.POSITION) && entity.position().equals(position)) view.add(entity);
		}
		
		return new EntityView(view);
	}
	
	/**
	 * Returns a view with the subset of entities with a position between two points, inclusive
	 * @param minX
	 * 		Left most x position
	 * @param minY
	 * 		Top most y position
	 * @param maxX
	 * 		Right most x position
	 * @param maxY
	 * 		Bottom most y position
	 * @return
	 * 		All entities with a position in range
	 */
	public EntityView between(int minX, int minY, int maxX, int maxY) {
		List<Entity> view = new ArrayList<>();
		
		for(Entity entity : entities) {
			if(!entity.has(Component.POSITION) || 
				entity.position().x() < minX || entity.position().x() > maxX ||
				entity.position().y() < minY || entity.position().y() > maxY) continue;
			
			view.add(entity);
		}
		
		return new EntityView(view);
	}
	
	/**
	 * Returns a view with the subset of all entities within a defined radius
	 * @param x
	 * 		X cooordinate of center point
	 * @param y
	 * 		Y coordinate of center point
	 * @param r
	 * 		Radius
	 * @return
	 * 		All entities within radius
	 */
	public EntityView inRadius(int x, int y, int r ) {
		List<Entity> view = new ArrayList<>();
		
		for(Entity entity : entities) {
			if (!entity.has(Component.POSITION)) continue;
			
			Position position = entity.position();
			if (Util.distance(x, y, position.x(), position.y()) < r) {
				view.add(entity);
			}
		}
		
		return new EntityView(view);
	}
	
	/**
	 * Returns a view with the subset of entities containing listed components
	 * @param components
	 * 		Components to check
	 * @return
	 * 		View
	 */
	public EntityView with(Component ...components) {
		List<Entity> view = new ArrayList<>();
		
		for(Entity entity : entities) {
			if (entity.has(components)) view.add(entity);
		}
		
		return new EntityView(view);
	}
}
