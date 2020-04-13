/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.factory;

import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chensley.da.ecs.Entity;

/**
 * Main factory for generating entities and components
 */
public class EntityFactory extends Factory<Entity> {
	private final ColorFactory colors;
	private final TileFactory tiles;
	
	public EntityFactory(ObjectMapper mapper, Logger logger) {
		super(mapper, logger);
		this.colors = new ColorFactory(mapper, logger);
		this.tiles = new TileFactory(colors, mapper, logger);
		this.copy = true;
	}

	@Override
	protected Entity deserialize(JsonNode node) throws JsonProcessingException {
		String label = node.get("label").asText();
		if(label == null) return null; //Entity must have label
		
		Entity entity = new Entity(label);
		entity.setTile(node.get("tile") != null ? tiles.get(node.get("tile").asText()) : null);
		
		return entity;
	}
	
	
	@Override 
	protected Entity copy(String id) {
		Entity entity = map.get(id);
		return new Entity(entity);
	}
	
	//Access underlying factories
	public ColorFactory colors() { return colors; }
	public TileFactory tiles() { return tiles; }

}
