/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs;

import chensley.da.ecs.factory.EntityFactory;

/**
 * Manages all game entities
 */
public class EntityManager extends EntityView {
	private final EntityFactory factory;
	
	/**
	 * Constructor
	 * @param factory
	 * 		Initialized EntityFactory
	 */
	public EntityManager(EntityFactory factory) {
		super();
		this.factory = factory;
	}
	
	/**
	 * Creates a new entity
	 * @param id
	 * 		Entity id from data file
	 * @return
	 * 		New entity
	 */
	public Entity create(String id) {
		Entity entity = factory.get(id);
		if(entity != null) entities.add(entity);
		
		return entity;
	}
	
	/**
	 * Deletes an entity
	 * @param entity
	 * 		Entity to delete
	 */
	public void delete(Entity entity) { entities.remove(entity); }
}
