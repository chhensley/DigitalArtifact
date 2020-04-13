/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.factory;

import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory for hex RGB color strings
 */
public class ColorFactory extends Factory<Integer> {
	public ColorFactory(ObjectMapper mapper, Logger logger) {
		super(mapper, logger);
	}

	@Override
	protected Integer deserialize(JsonNode node) {
		return Integer.parseInt(node.asText().substring(2), 16);
	}

}
