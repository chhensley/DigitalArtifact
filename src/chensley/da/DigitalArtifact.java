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
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import chensley.da.ecs.EntityManager;
import chensley.da.ecs.factory.ColorFactory;
import chensley.da.ecs.factory.EntityFactory;
import chensley.da.ui.GUI;

public class DigitalArtifact {
	
	private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	private static final Logger logger = Logger.getLogger(DigitalArtifact.class.getName());
	private static final EntityFactory factory = load("manifest.yml", mapper, logger);
	private static final Config config = loadConfig("config.yml", factory.colors(), mapper, logger);
	private static final EntityManager entityMgr = new EntityManager(factory);
	
	//Converts an array node to an array
	private static String[] asArray(JsonNode node) {
		String[] array = new String[node.size()];
		
		for(int i = 0; i < node.size(); i++) {
			array[i] = node.get(i).asText();
		}
		
		return array;
	}
	
	//Creates a new entity factory using the data files defined in a manifest file
	private static EntityFactory load(String path, ObjectMapper mapper, Logger logger) {
		EntityFactory factory = new EntityFactory(mapper, logger);
		File file = new File(path);
		try {
			JsonNode root = mapper.readTree(file);
			factory.colors().load(asArray(root.get("colors")));
			factory.tiles().load(asArray(root.get("tiles")));
			factory.load(asArray(root.get("entities")));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "failed to load data files", e);
			System.exit(1);
		}
		
		return factory;
	}
	
	//Creates new config object from external configuration file
	private static Config loadConfig(String path, ColorFactory colors, ObjectMapper mapper, Logger logger) {
		Config config = null;
		try {
			config = new Config(path, colors, mapper, logger);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "failed to load configuration files", e);
			System.exit(1);
		}
		
		return config;
	}
	
	public static void main(String[] args) throws IOException {
		GUI gui = new GUI(config, logger);
		gui.launch();
	}
}
