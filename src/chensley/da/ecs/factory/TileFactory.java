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

import chensley.da.ecs.components.Tile;
import chensley.da.util.Util;

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
		final double alpha = get(node, "alpha", 1.0);
		final double fowAlpha = get(node, "fow_alpha", 0.0);
		final double xOffset = get(node, "x_offset", 0.0);
		final double yOffset = get(node, "y_offset", 0.0);
		
		if(icon == null || colorId == null) {
			logger.log(Level.SEVERE, "invalid tile definition: {0}", node);
			return null;
		}
		
		Integer color = colorFactory.get(colorId);
		
		if(color == null) {
			logger.log(Level.SEVERE, "unknown color: {0}", colorId);
			return null;
		}
		
		return new Tile(icon, Util.color(color, alpha), fowAlpha != 0?Util.color(color, fowAlpha):null, xOffset, yOffset);
	}

}
