package chensley.da.ecs.factory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Factory for state machine definitions
 */
public class StateMachineFactory extends Factory<Map<String, String>>{

	protected StateMachineFactory(ObjectMapper mapper, Logger logger) {
		super(mapper, logger);
	}

	@Override
	protected Map<String, String> deserialize(JsonNode node) throws JsonProcessingException {
		Map<String, String> stateMachine = new HashMap<>();
		
		Iterator<Map.Entry<String,JsonNode>> iterator = node.fields();
		while(iterator.hasNext()) {
			Map.Entry<String,JsonNode> entry = iterator.next();
			stateMachine.put(entry.getKey(), entry.getValue().asText());
		}
		return stateMachine;
	}
}
