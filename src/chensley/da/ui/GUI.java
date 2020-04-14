/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;

import chensley.da.Config;

/**
 * Main UI
 */
public class GUI {
	private final JFrame window;
	private final Logger logger;
	private CanvasPanel termPanel;
	
	//Builds main game window
	private JFrame window(Config config) {
		JFrame window = new JFrame(config.title());
		window.getContentPane().setBackground(config.term().background());
		window.setResizable(false);
		window.setFocusable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return window;
	}
	
	//Builds the simulated terminal panel
	private CanvasPanel termPanel(Config config) {
		int width = config.term().width() * config.term().fontSize() + config.term().fontSize()/2;
		int height = config.term().height() * config.term().fontSize() + config.term().fontSize()/2;
		
		CanvasPanel termPanel = new CanvasPanel(config);
		termPanel.setPreferredSize(new Dimension(width, height));
		termPanel.setFocusable(false);
		termPanel.repaint();
		return termPanel;
	}
	
	public GUI(Config config, Logger logger) {
		this.logger = logger;
		
		logger.log(Level.INFO, "building ui");
		window = window(config);
		termPanel = termPanel(config);
		window.getContentPane().add(termPanel, BorderLayout.PAGE_START);
		
		window.pack();
	}
	
	//Launches the main UI window
	public void launch() {
		window.setVisible(true);
		window.requestFocusInWindow();
		logger.log(Level.INFO, "starting ui");
	}
	
	//Draw to simualted terminal canvas
	public void termClear() {
		termPanel.clear();
	}
	
	/**
	 * Draws a single item on the terminal panel
	 * @param icon
	 * 		Unicode character
	 * @param color
	 * 		AWT color
	 * @param x
	 * 		X position
	 * @param y
	 * 		Y position
	 */
	public void termDraw(String icon, Color color, int x, int y, double xOffset, double yOffset) {
		termPanel.draw(icon, color, x, y, xOffset, yOffset);
	}
}
