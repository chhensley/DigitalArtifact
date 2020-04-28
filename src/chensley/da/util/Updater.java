/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Updates software
 */
public class Updater {
	private URL downloadUrl = null;
	private Path archive = null;
	private final String runCmd;
	private final String jarPath;
	private final Logger logger;
	
	/**
	 * Opens a URL connection
	 * @param url
	 * 		URL
	 * @return
	 * 		Input Stram
	 * @throws IOException
	 */
	private InputStream openConnection(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		return con.getInputStream();
	}

	/**
	 * Constructor
	 * @param downloadUrl
	 * 		URL to download compressed archive
	 * @param runCmd
	 * 		Command to restart this application
	 * @param jarPath
	 * 		Path to the unzip jar file embedded within this jar file
	 * @param logger
	 */
	public Updater(String downloadUrl, String runCmd, String jarPath, Logger logger) {
		this.logger = logger;
		this.jarPath = jarPath;
		this.runCmd = runCmd;
		try {
			this.downloadUrl = new URL(downloadUrl);
			archive = Paths.get(Util.fileNameFromUrl(downloadUrl));
		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, "malformed url " + downloadUrl, e);
		}
	}
	
	/**
	 * Runs the update process
	 */
	public void update() {
		//Download archive
		try(InputStream istream = openConnection(downloadUrl)) {
			logger.log(Level.INFO, "downloading {0}", downloadUrl);
			Files.copy(istream, archive, StandardCopyOption.REPLACE_EXISTING);
		} catch(IOException e) {
			logger.log(Level.SEVERE, "failed to download " + downloadUrl, e);
			return;
		}
		
		//Extracts unzip jar stored in this jar
		logger.log(Level.INFO, "extracting {0}", jarPath);
		try(InputStream istream = Updater.class.getResourceAsStream("/" + jarPath)) {
			Files.copy(istream, Paths.get(jarPath), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "failed to extract file " + jarPath, e);
			return;
		}
		
		//Unzips archive and restarts this application
		String cmd = new StringBuilder("java -jar ")
				.append(jarPath)
				.append(" ")
				.append(archive.toString())
				.append(" \"")
				.append(runCmd)
				.append("\"").toString();
		
		logger.log(Level.INFO, "running {0}", cmd);
		try {
			Runtime.getRuntime().exec(cmd);
			System.exit(0);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "failed to run updater " + jarPath, e);
			return;
		}
	}
}
