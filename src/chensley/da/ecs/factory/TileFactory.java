/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.factory;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.paint.Color;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chensley.da.ecs.components.Tile;

@SuppressWarnings("restriction") //Incorrectly tags javafx Color
public class TileFactory extends Factory<Tile>{
	private final ColorFactory colorFactory;
	
	public TileFactory(ColorFactory colorFactory, ObjectMapper mapper, Logger logger) {
		super(mapper, logger);
		this.colorFactory = colorFactory;
	}

	@Override
	protected Tile deserialize(JsonNode node) {
		String icon = node.get("icon").asText();
		String colorId = node.get("color").asText();
		double alpha = node.get("alpha") != null ? node.get("alpha").asDouble(1.0) : 1.0;
		
		if(icon == null || colorId == null) {
			logger.log(Level.SEVERE, "invalid tile definition: {0}", node);
			return null;
		}
		
		String color = colorFactory.get(colorId);
		
		if(color == null) {
			logger.log(Level.SEVERE, "unknown color: {0}", colorId);
			return null;
		}
		
		return new Tile(icon, Color.web(color, alpha));
	}

}
