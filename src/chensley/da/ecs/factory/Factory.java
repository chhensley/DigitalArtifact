/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.factory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Generic factory class
 * Creates game objects from external file definitions
 */
public abstract class Factory <T>{
	private final ObjectMapper mapper;
	protected final Logger logger;
	
	//Controls whether cloned object or original is returned by get
	protected boolean copy = false;
	
	protected final Map<String, T> map = new HashMap<>();
	
	protected Factory(ObjectMapper mapper, Logger logger) {
		this.mapper = mapper;
		this.logger = logger;
	}
	
	/**
	 * Loads a single external data file
	 * @param paths
	 * 		One or more external file paths
	 * @return
	 * 		Root json node or null if file does not exist
	 * @throws IOException
	 */
	public void load(String ...paths) throws IOException {
		for(String path : paths) {
			File file = new File(path);
			if(file.exists()) {
				logger.log(Level.INFO, "loading file: {0}", path);
				JsonNode root = mapper.readTree(file);
				
				if(root == null) continue;
				Iterator<Entry<String, JsonNode>> fields = root.fields();
				while(fields.hasNext()) {
					Entry<String, JsonNode> field = fields.next();
					T value = deserialize(field.getValue());
					if(value == null) throw new IOException("Failed to parse: " + field.getValue());
					map.put(field.getKey(), deserialize(field.getValue()));
				}
			} else {
				logger.log(Level.WARNING, "file not found: {0}", path);
			}
		}
	}
	
	/**
	 * Retrieves instance of object
	 * @param id
	 * 		Id of object
	 * @return
	 * 		Object
	 */
	public T get(String id) {
		return copy ? copy(id) : map.get(id);
	}
	
	/**
	 * Desrializes Jackson node into type T
	 * @param node
	 * 		Jackson node
	 * @return
	 * 		Object of type T, or null if unable to parse node
	 * @throws JsonProcessingException
	 */
	protected abstract T deserialize(JsonNode node) throws JsonProcessingException;
	
	/**
	 * Makes a copy of the object T
	 * Override implementation if copy flag is set
	 * @param id
	 * 		Id of object
	 * @return
	 * 		Copied object
	 */
	protected T copy(String id) { return null; }
}
