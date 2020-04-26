/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		private final String about;
		
		Controls(String up, String down, String left, String right, String about) {
			this.down = down;
			this.left = left;
			this.right = right;
			this.up = up;
			this.about = about;
		}
		
		public String about() { return about; }
		public String down() { return down; }
		public String left() { return left; }
		public String right() { return right; }
		public String up() { return up; }
	}
	
	//GUI settings not related to simulated terminal
	public class GUI {
		private final int sideBarWidth;
		private final int logSize;
		
		public GUI(int sideBarWidth, int logSize) {
			this.sideBarWidth = sideBarWidth;
			this.logSize = logSize;
		}
		
		public int logSize() { return logSize; }
		public int sideBarWidth() { return sideBarWidth; }
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
	
	//Map generation settings
	public class MapGen {
		private final MapPartition partition;
		private final int minDoors;
		private final int maxDoors;
		
		public MapGen(MapPartition partition, int minDoors, int maxDoors) {
			this.partition = partition;
			this.minDoors = minDoors;
			this.maxDoors = maxDoors;
		}
		
		public MapPartition partition() { return partition; }
		public int minDoors() { return minDoors; }
		public int maxDoors() { return maxDoors; }
		
	}
	
	//Settings for partitioning map into buildings
	public class MapPartition {
		private final int depth;
		private final int minWidth;
		private final int minHeight;
		
		public MapPartition(int depth, int minWidth, int minHeight) {
			this.depth = depth;
			this.minWidth = minWidth;
			this.minHeight = minHeight;
		}
		
		public int depth() { return depth; }
		public int minWidth() { return minWidth; }
		public int minHeight() { return minHeight; }
	}
	
	//Simulated main terminal settings
	public class Terminal {
		private final String font;
		private final int fontSize;
		private final int height;
		private final int width;
		private final List<Color> foregrounds;
		private final Color background;
		
		Terminal(int width, int height, String font, int fontSize, List<Color> foregrounds, Color background) {
			this.width = width;
			this.height = height;
			this.font = font;
			this.fontSize = fontSize;
			this.foregrounds = foregrounds;
			this.background = background;
		}
		
		public Color foreground(int index) { return foregrounds.get(index); }
		public Color background() { return background; }
		public String font() { return font; }
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
		
		public String downloadUrl() { return downloadUrl; }
		public String jar() { return jar; }
		public String cmd() { return cmd; }
	}
	
	private final String title;
	private final Controls controls;
	private final GUI gui;
	private final Map map;
	private final MapGen mapGen;
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
		controls = new Controls(
			ctrlNode.get("up").asText(), 
			ctrlNode.get("down").asText(), 
			ctrlNode.get("left").asText(), 
			ctrlNode.get("right").asText(),
			ctrlNode.get("about").asText()
		);
		
		JsonNode guiNode = root.get("gui");
		gui = new GUI(
			guiNode.get("sidebar_width").asInt(),
			guiNode.get("log_size").asInt()
		);
		
		JsonNode mapNode = root.get("map");
		map = new Map(mapNode.get("width").asInt(), mapNode.get("height").asInt());
		
		JsonNode termNode = root.get("terminal");
		
		List<Color> foregrounds = new ArrayList<>();
		for(JsonNode color : termNode.get("foreground")) {
			foregrounds.add(new Color(colors.get(color.asText())));
		}
		
		JsonNode mapGenNode = root.get("map_gen");
		JsonNode partitionNode = mapGenNode.get("partition");
		MapPartition partition = new MapPartition(
			partitionNode.get("depth").asInt(),
			partitionNode.get("min_width").asInt(),
			partitionNode.get("min_height").asInt()
		);
		mapGen = new MapGen(
				partition,
				mapGenNode.get("min_doors").asInt(),
				mapGenNode.get("max_doors").asInt()
		);
		
		term = new Terminal(
			termNode.get("width").asInt(),
			termNode.get("height").asInt(),
			termNode.get("font").asText(),
			termNode.get("fontSize").asInt(),
			foregrounds,
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
	public GUI gui() { return gui; }
	public Map map() { return map; }
	public MapGen mapGen() { return mapGen; }
	public Terminal term() { return term; }
	public Update update() { return update; }
}
