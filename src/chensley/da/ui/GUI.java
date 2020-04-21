/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import chensley.da.Config;
import chensley.da.ui.GUIFactory.TermMenu;

/**
 * Main UI
 */
public class GUI {
	//Keyboard handler for the main GUI
	private class GUIKeyListener implements KeyListener {
		public KeyEvent e = null;
		
		@Override
		public void keyPressed(KeyEvent e) {}
		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public void keyReleased(KeyEvent e) {		
			this.e = e;
		}
		
		/**
		 * Listens for next key event
		 * @return
		 * 		key event
		 */
		public KeyEvent listen() {
			e = null;
			while(e == null) {
				try { Thread.sleep(1);
				} catch (InterruptedException e) {
					return null;
				}
			}
			
			KeyEvent event = e;
			e = null;
			return event;
		}
	}
	
	private final Logger logger;
	private final GUIFactory factory;
	private final JFrame window;
	private CanvasPanel termPanel;
	private JPanel aboutMenu;
	private final GUIKeyListener keyListener = new GUIKeyListener();
	
	//Builds main game window
	private JFrame window(Config config) {
		JFrame window = new JFrame(config.title());
		window.getContentPane().setBackground(config.term().background());
		window.setResizable(false);
		window.setFocusable(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.addKeyListener(keyListener);
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
	
	//Builds about menu
	private JPanel aboutMenu(Config config, GUIFactory factory) {
		TermMenu menu = factory.menu();
		menu.add(factory.menuTitle("Controls"));
		menu.add(factory.menuLabel(new StringBuilder("Movement: ")
				.append(config.controls().up())
				.append(',')
				.append(config.controls().left())
				.append(',')
				.append(config.controls().down())
				.append(',')
				.append(config.controls().right())
				.toString()));
		menu.add(factory.menuSpacer());
		menu.add(factory.menuTitle("Options"));
		menu.add(factory.menuItem("\u2714", "Update", null, KeyEvent.VK_U));
		menu.add(factory.menuItem("\u2716", "Cancel", null , KeyEvent.VK_ESCAPE));
		menu.build();
		
		menu.setVisible(true);
		return menu;
	}
	
	public GUI(Config config, Logger logger) {
		this.logger = logger;
		this.factory = new GUIFactory(config);
		
		logger.log(Level.INFO, "building ui");
		window = window(config);
		termPanel = termPanel(config);
		aboutMenu = aboutMenu(config, factory);
		window.getContentPane().add(termPanel, BorderLayout.LINE_START);
		window.getContentPane().add(factory.menuLabel(config.controls().about() + " for help and to update"), BorderLayout.PAGE_END);
		
		window.pack();
	}
	
	//Launches the main UI window
	public void launch() {
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.requestFocusInWindow();
		logger.log(Level.INFO, "booting spectre virtual assitant");
		logger.log(Level.INFO, "initializing retinal display");
	}
	
	//Returns next keyboard input from gui
	public KeyEvent listen() {
		return keyListener.listen();
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
	
	/**
	 * Repaints terminal contents
	 */
	public void termRepaint() {
		termPanel.repaint();
	}
	
	/**
	 * Clears any displayed terminals and menus
	 * Call this before adding the new menu or terminal to the window
	 */
	private void resetTerm() {
		window.getContentPane().remove(aboutMenu);
		window.getContentPane().remove(termPanel);	
	}
	
	//Shows terminal window
	public void showTerm() {
		resetTerm();
		window.getContentPane().add(termPanel, BorderLayout.LINE_START);
		window.pack();
		window.repaint();
	}
	
	//Shows about menu
	public void showAbout() {
		resetTerm();
		window.getContentPane().add(aboutMenu, BorderLayout.LINE_START);
		window.pack();
		window.repaint();
	}
}
