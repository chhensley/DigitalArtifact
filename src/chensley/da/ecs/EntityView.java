/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	 * Returns a view with the subset of entities with a position between two points, inclusive
	 * @param minX
	 * 		Left most x position
	 * @param minY
	 * 		Top most y position
	 * @param maxX
	 * 		Right most x position
	 * @param maxY
	 * 		Bottom mot y position
	 * @return
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
