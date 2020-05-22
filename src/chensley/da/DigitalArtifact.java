/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import chensley.da.ecs.EntityManager;
import chensley.da.ecs.factory.ColorFactory;
import chensley.da.ecs.factory.EntityFactory;
import chensley.da.message.MessageFactory;
import chensley.da.message.MessageManager;
import chensley.da.message.listener.AIListener;
import chensley.da.message.listener.ActionListener;
import chensley.da.message.listener.DestroyListener;
import chensley.da.message.listener.FactoryListener;
import chensley.da.message.listener.SystemListener;
import chensley.da.message.listener.UIListener;
import chensley.da.util.RandomNumberGenerator;
import chensley.da.util.Util;

public class DigitalArtifact {
	
	private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	private static final Logger logger = Logger.getLogger(DigitalArtifact.class.getName());
	private static final EntityFactory factory = load("manifest.yml", mapper, logger);
	private static final Config config = loadConfig("config.yml", factory.colors(), mapper, logger);
	private static final EntityManager entityMgr = new EntityManager(factory);
	private static final RandomNumberGenerator rng = new RandomNumberGenerator();
	private static final MessageManager msgMgr = new MessageManager(config, entityMgr, rng, logger);
	
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
			factory.machines().load(asArray(root.get("machines")));
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
	
	//Gracefully deletes a file
	private static void deleteFile(Path path, Logger logger) {
		if(Files.exists(path)) {
			logger.log(Level.INFO, "deleting {0}", path);
			try {
				Files.delete(path);
			} catch(IOException e) {
				logger.log(Level.SEVERE, "failed to delete " + path, e);
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		AIListener.register(msgMgr);
		UIListener.register(msgMgr);
		FactoryListener.register(msgMgr);
		ActionListener.register(msgMgr);
		DestroyListener.register(msgMgr);
		SystemListener.register(msgMgr);
		
		deleteFile(Paths.get(config.update().jar()), logger);
		deleteFile(Paths.get(Util.fileNameFromUrl(config.update().downloadUrl())), logger);
		
		msgMgr.publish(MessageFactory.turnEnd());
		msgMgr.publish(MessageFactory.turnStart());
		msgMgr.publish(MessageFactory.appStart());
		
		//Exit is triggered through GUI
		for(;;) {
			while(!msgMgr.isEmpty()) msgMgr.consume();
			msgMgr.publish(MessageFactory.turnEnd());
			msgMgr.publish(MessageFactory.turnStart());
		}
	}
}
