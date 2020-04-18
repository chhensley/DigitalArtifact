/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import chensley.da.ecs.factory.ColorFactory;

/**
 * Game configuration loaded from external file
 */
public class Config {
	//Control scheme
	public class Controls {
		private final String down;
		private final String left;
		private final String right;
		private final String up;
		
		Controls(String up, String down, String left, String right) {
			this.down = down;
			this.left = left;
			this.right = right;
			this.up = up;
		}
		
		public String down() { return down; }
		public String left() { return left; }
		public String right() { return right; }
		public String up() { return up; }
	}
	
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
		private final Color background;
		
		Terminal(int width, int height, int fontSize, Color background) {
			this.width = width;
			this.height = height;
			this.fontSize = fontSize;
			this.background = background;
		}
		
		public Color background() { return background; }
		public int fontSize() { return fontSize; }
		public int height() { return height; }
		public int width() { return width; }
	}
	
	//Game update information
	public class Update {
		private final String downloadUrl;
		private final String jar;
		private final String cmd;
		
		public Update(String downloadUrl, String jar, String cmd) {
			this.downloadUrl = downloadUrl;
			this.jar = jar;
			this.cmd = cmd;
		}
		
		String downloadUrl() { return downloadUrl; }
		String jar() { return jar; }
		String cmd() { return cmd; }
	}
	
	private final String title;
	private final Controls controls;
	private final Map map;
	private final Terminal term;
	private final Update update;
	
	/**
	 * Constructor
	 * Loads configuration data from external file
	 * @param path
	 * 		Path to configuration file
	 * @param colors
	 * 		Initialized color factory
	 * @param mapper
	 * 		Object mapper
	 * @param logger
	 * 		Logger
	 * @throws IOException
	 */
	Config(String path, ColorFactory colors, ObjectMapper mapper, Logger logger) throws IOException {
		logger.log(Level.INFO, "loading file: {0}", path);
		File file = new File(path);
		JsonNode root = mapper.readTree(file);
		title = root.get("title").asText();
		
		JsonNode ctrlNode = root.get("controls");
		controls = new Controls(ctrlNode.get("up").asText(), ctrlNode.get("down").asText(), ctrlNode.get("left").asText(), ctrlNode.get("right").asText());
		
		JsonNode mapNode = root.get("map");
		map = new Map(mapNode.get("width").asInt(), mapNode.get("height").asInt());
		
		JsonNode termNode = root.get("terminal");
		term = new Terminal(
			termNode.get("width").asInt(),
			termNode.get("height").asInt(),
			termNode.get("fontSize").asInt(),
			new Color(colors.get(termNode.get("background").asText()))
		);
		
		JsonNode updateNode = root.get("update");
		update = new Update(
			updateNode.get("download_url").asText(),
			updateNode.get("jar").asText(),
			updateNode.get("cmd").asText()
		);
	}
	
	public String title() { return title; }
	public Controls controls() { return controls; }
	public Map map() { return map; }
	public Terminal term() { return term; }
	public Update update() { return update; }
}
