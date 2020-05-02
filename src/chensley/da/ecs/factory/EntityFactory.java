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
import chensley.da.ecs.components.Destructable;
import chensley.da.ecs.components.Physics;
import chensley.da.ecs.components.Vision;

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
	
	private Destructable deserializeDestructable(JsonNode node) {
		int hp = node.get("hit_points").asInt();
		int impactArmor = node.get("impact_armor") != null ? node.get("impact_armor").asInt() : 0;
		String onDestroy = node.get("on_destroy").asText();
		return new Destructable(hp, impactArmor, onDestroy);
	}
	
	private Physics deserializePhysics(JsonNode node) {
		boolean impassible = node.get("impassible") != null ? node.get("impassible").asBoolean() : false;
		boolean opaque = node.get("opaque") != null ? node.get("opaque").asBoolean() : false;
		return new Physics(impassible, opaque);
	}
	
	private Vision deserializeVision(JsonNode node) {
		return new Vision(node.get("range").asInt());
	}

	@Override
	protected Entity deserialize(JsonNode node) throws JsonProcessingException {
		String label = node.get("label").asText();
		if(label == null) return null; //Entity must have label
		
		Entity entity = new Entity(label);
		entity.setDestructable(node.get("destructable") != null ? deserializeDestructable(node.get("destructable")) : null);
		entity.setPhysics(node.get("physics") != null ? deserializePhysics(node.get("physics")) : null);
		entity.setTile(node.get("tile") != null ? tiles.get(node.get("tile").asText()) : null);
		entity.setVision(node.get("vision") != null ? deserializeVision(node.get("vision")) : null);
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
