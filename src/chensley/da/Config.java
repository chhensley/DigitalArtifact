/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Game configuration loaded from external file
 */
public class Config {
	//Game map settings
	public class Map {
		private final int height;
		private final int width;
		
		Map(int width, int height) {
			this.width = width;
			this.height = height;
		}
		
		public int height() { return height; }
		public int width()  { return width; }
	}
	
	//Simulated main terminal settings
	public class Terminal {
		private final int fontSize;
		private final int height;
		private final int width;
		
		Terminal(int width, int height, int fontSize) {
			this.width = width;
			this.height = height;
			this.fontSize = fontSize;
		}
		
		public int fontSize() { return fontSize; }
		public int height() { return height; }
		public int width() { return width; }
	}
	
	private final String title;
	private final Map map;
	private final Terminal term;
	
	/**
	 * Constructor
	 * Loads configuration data from external file
	 * @param path
	 * 		Path to configuration file
	 * @param mapper
	 * 		Object mapper
	 * @param logger
	 * 		Logger
	 * @throws IOException
	 */
	Config(String path, ObjectMapper mapper, Logger logger) throws IOException {
		logger.log(Level.INFO, "loading file: {0}", path);
		File file = new File(path);
		JsonNode root = mapper.readTree(file);
		title = root.get("title").asText();
		
		JsonNode mapNode = root.get("map");
		map = new Map(mapNode.get("width").asInt(), mapNode.get("height").asInt());
		
		JsonNode termNode = root.get("terminal");
		term = new Terminal(
			termNode.get("width").asInt(),
			termNode.get("height").asInt(),
			termNode.get("fontSize").asInt()
		);	
	}
	
	public String title() { return title; }
	public Map map() { return map; }
	public Terminal term() { return term; }
}
