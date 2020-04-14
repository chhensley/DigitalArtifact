/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.factory;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chensley.da.Util;
import chensley.da.ecs.components.Tile;

public class TileFactory extends Factory<Tile>{
	private final ColorFactory colorFactory;
	
	public TileFactory(ColorFactory colorFactory, ObjectMapper mapper, Logger logger) {
		super(mapper, logger);
		this.colorFactory = colorFactory;
	}

	@Override
	protected Tile deserialize(JsonNode node) {
		final String icon = node.get("icon").asText();
		final String colorId = node.get("color").asText();
		final double alpha = node.get("alpha") != null ? node.get("alpha").asDouble(1.0) : 1.0;
		final double xOffset = node.get("x_offset") != null ? node.get("x_offset").asDouble() : 0.0;
		final double yOffset = node.get("y_offset") != null ? node.get("y_offset").asDouble() : 0.0;
		
		if(icon == null || colorId == null) {
			logger.log(Level.SEVERE, "invalid tile definition: {0}", node);
			return null;
		}
		
		Integer color = colorFactory.get(colorId);
		
		if(color == null) {
			logger.log(Level.SEVERE, "unknown color: {0}", colorId);
			return null;
		}
		
		return new Tile(icon, Util.color(color, alpha), xOffset, yOffset);
	}

}
