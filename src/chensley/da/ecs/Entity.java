/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs;

import java.util.BitSet;

import chensley.da.ecs.components.Physics;
import chensley.da.ecs.components.Position;
import chensley.da.ecs.components.Tile;
import chensley.da.ecs.components.Vision;

/**
 * Game entity
 */
public class Entity {
	
	private BitSet components = new BitSet(Component.values().length);
	private String label;
	
	public Entity(String label) {
		this.label = label;
	}
	
	public Entity(Entity entity) {
		this.label = entity.label;
		if (entity.has(Component.PHYSICS)) this.setPhysics(new Physics(entity.physics));
		if (entity.has(Component.POSITION)) this.setPosition(new Position(entity.position));
		this.setTile(entity.tile);
		if (entity.has(Component.VISION)) this.setVision(new Vision(entity.vision));
	}
	
	/**
	 * Checks if this entity has the listed components
	 * @param components
	 * 		One or more components
	 * @return
	 * 		true if this entity has all listed components
	 */
	public boolean has(Component ...components) {
		for(Component component : components) {
			if(!this.components.get(component.ordinal())) return false;
		}
		return true;
	}
	
	public void setLabel(String label) { this.label = label; }
	public String label() { return label; }
	
	//Components
	private Physics physics = null;
	private Position position = null;
	private Tile tile = null;
	private Vision vision = null;
	
	public void setPhysics(Physics physics) {
		components.set(Component.PHYSICS.ordinal(), physics != null);
		this.physics = physics;
	}
	
	public Physics physics() { return physics; }
	
	public void setPosition(Position position) {
		components.set(Component.POSITION.ordinal(), position != null);
		this.position = position;
	}
	
	public Position position() { return position; }
	
	public void setTile(Tile tile) {
		components.set(Component.TILE.ordinal(), tile != null);
		this.tile = tile;
	}
	
	public Tile tile() { return tile; }
	
	public void setVision(Vision vision) {
		components.set(Component.VISION.ordinal(), vision != null);
		this.vision = vision;
	}
	
	public Vision vision() { return vision; }
}
