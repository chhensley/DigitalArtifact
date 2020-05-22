/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import chensley.da.Config;
import chensley.da.ui.GUIFactory.TermMenu;
import chensley.da.util.Util;

/**
 * Main UI
 */
public class GUI {
	//Log which displays most recent log message on the bottom
	private class MsgLog extends JTextArea {
		private static final long serialVersionUID = 1185768508876489650L;
		private final int size;
		
		public MsgLog(int size) {
			super();
			this.size = size;
		}
		
		@Override
		public void append(String msg) {
			try {
				while(getLineCount() > size) {
					
					replaceRange("", 0, getLineEndOffset(0));
				}
			} catch(BadLocationException e) {
				//Do nothing
			}
			
			super.append(msg);
		}
	}
	
	
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
	private JPanel gameOver;
	private final GUIKeyListener keyListener = new GUIKeyListener();
	
	//Widget meter colors
	private final String meterForeground;
	
	//Status display elements
	private final StatusWidget healthWidget;
	private final JLabel infoDisplay;
	
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
	
	//Builds info display 
	private JLabel infoDisplay(Config config) {
		JLabel infoDisplay = new JLabel("");
		
		infoDisplay.setBackground(config.term().background());
		infoDisplay.setForeground(config.term().foreground(2));
		infoDisplay.setVerticalAlignment(JLabel.TOP);
		infoDisplay.setVerticalTextPosition(JLabel.TOP);
		infoDisplay.setFont(new Font(config.term().font(), Font.PLAIN, config.term().fontSize()));
		
		return infoDisplay;
	}
	
	//Builds the player status panel
	private JPanel statusPanel(Config config, GUIFactory factory) {
		JPanel statusPanel = new JPanel();
		
		statusPanel.setPreferredSize(new Dimension(config.gui().sideBarWidth(), config.term().height() * config.term().fontSize()));
		statusPanel.setBackground(config.term().background());
		statusPanel.setLayout(new GridBagLayout());
		statusPanel.setBorder(new EmptyBorder(0, config.term().fontSize(), 0, 0));
		
		//Melee status
		GridBagConstraints labelCon = new GridBagConstraints();
		labelCon.weightx = 1.0;
		labelCon.weighty = 0.0;
		labelCon.fill = GridBagConstraints.HORIZONTAL;
		labelCon.anchor = GridBagConstraints.NORTHEAST;
		labelCon.gridx = 0;
		labelCon.gridy = 0;
		
		statusPanel.add(factory.statusTitle("Melee Weapon"), labelCon);
		
		GridBagConstraints statCon = new GridBagConstraints();
		statCon.weightx = 1.0;
		statCon.weighty = 0.0;
		statCon.fill = GridBagConstraints.BOTH;
		statCon.anchor = GridBagConstraints.NORTHEAST;
		statCon.gridx = 0;
		statCon.gridy = 1;
		
		StatusWidget melee = factory.statusWidget(infoDisplay);
		melee.setText("\u270A", "Fist");
		melee.setDetails("<html>\u270A fist"
				+ "<br>An unaugmented human fist"
				+ "<br>Impact Damage: 1 to 3</html>"
		);
		statusPanel.add(melee, statCon);
		
		//Armor status
		labelCon = new GridBagConstraints();
		labelCon.weightx = 1.0;
		labelCon.weighty = 0.0;
		labelCon.fill = GridBagConstraints.HORIZONTAL;
		labelCon.anchor = GridBagConstraints.NORTHEAST;
		labelCon.gridx = 0;
		labelCon.gridy = 2;
		
		statusPanel.add(factory.statusTitle("Armor"), labelCon);
		
		statCon = new GridBagConstraints();
		statCon.weightx = 1.0;
		statCon.weighty = 0.0;
		statCon.fill = GridBagConstraints.BOTH;
		statCon.anchor = GridBagConstraints.NORTHEAST;
		statCon.gridx = 0;
		statCon.gridy = 3;
		
		StatusWidget armor = factory.statusWidget(infoDisplay);
		armor.setText("\u26E8", "Plot Armor");
		armor.setDetails("<html>\u26E8 Plot Armor"
			+ "<br>A little extra protection to keep the action dramatic"
			+ "<br>Impact Resistance: 1</html>"
		);
		statusPanel.add(armor, statCon);
		
		//Player health display
		labelCon = new GridBagConstraints();
		labelCon.weightx = 1.0;
		labelCon.weighty = 0.0;
		labelCon.fill = GridBagConstraints.HORIZONTAL;
		labelCon.anchor = GridBagConstraints.NORTHEAST;
		labelCon.gridx = 0;
		labelCon.gridy = 4;
		
		statusPanel.add(factory.statusTitle("Health"), labelCon);
		
		statCon = new GridBagConstraints();
		statCon.weightx = 1.0;
		statCon.weighty = 0.0;
		statCon.fill = GridBagConstraints.BOTH;
		statCon.anchor = GridBagConstraints.NORTHEAST;
		statCon.gridx = 0;
		statCon.gridy = 5;
		
		healthWidget.setForeground(config.term().foreground(0));
		
		statusPanel.add(healthWidget, statCon);
		
		//Information panel
		labelCon = new GridBagConstraints();
		labelCon.weightx = 1.0;
		labelCon.weighty = 0.0;
		labelCon.fill = GridBagConstraints.HORIZONTAL;
		labelCon.anchor = GridBagConstraints.NORTHEAST;
		labelCon.gridx = 0;
		labelCon.gridy = 6;
		
		statusPanel.add(factory.statusTitle("Information Display"), labelCon);
		
		GridBagConstraints infoCon = new GridBagConstraints();
		infoCon.weightx = 1.0;
		infoCon.weighty = 1.0;
		infoCon.fill = GridBagConstraints.BOTH;
		infoCon.anchor = GridBagConstraints.NORTHEAST;
		infoCon.gridx = 0;
		infoCon.gridy = 7;
		
		statusPanel.add(infoDisplay, infoCon);
		
		return statusPanel;
	}
	
	private JPanel sidePanel(Config config) {
		JPanel sidePanel = new JPanel();
		
		sidePanel.setPreferredSize(new Dimension(config.gui().sideBarWidth(), config.term().height() * config.term().fontSize()));
		sidePanel.setBackground(config.term().background());
		sidePanel.setLayout(new GridBagLayout());
		sidePanel.setBorder(new EmptyBorder(0, config.term().fontSize(), 0, 0));
		
		//Message log
		GridBagConstraints labelCon = new GridBagConstraints();
		labelCon.weightx = 1.0;
		labelCon.weighty = 0.0;
		labelCon.fill = GridBagConstraints.HORIZONTAL;
		labelCon.anchor = GridBagConstraints.NORTHEAST;
		labelCon.gridx = 0;
		labelCon.gridy = 0;
		
		JLabel logLabel = new JLabel("=== Message Log ===");
		logLabel.setForeground(config.term().foreground(0));
		logLabel.setFont(new Font(config.term().font(), Font.BOLD, config.term().fontSize()));
		sidePanel.add(logLabel, labelCon);
		
		GridBagConstraints logCon = new GridBagConstraints();
		logCon.weightx = 1.0;
		logCon.weighty = 1.0;
		logCon.fill = GridBagConstraints.BOTH;
		logCon.anchor = GridBagConstraints.NORTHEAST;
		logCon.gridx = 0;
		logCon.gridy = 1;
		
		MsgLog msgLog = new MsgLog(config.gui().logSize());
		msgLog.setForeground(config.term().foreground(2));
		msgLog.setBackground(config.term().background());
		msgLog.setFont(new Font(config.term().font(), Font.PLAIN, config.term().fontSize()));
		msgLog.setFocusable(false);
		logger.addHandler(new JTextAreaHandler(msgLog));
		sidePanel.add(msgLog, logCon);
		
		return sidePanel;
	}
	
	private JPanel gameOver(Config config) {
		JLabel label = new JLabel("GAME OVER!");
		label.setBackground(config.term().background());
		label.setForeground(config.term().foreground(2));
		label.setFont(new Font(config.term().font(), Font.PLAIN, 26));
		
		JPanel gameOver = new JPanel();
		gameOver.setPreferredSize(new Dimension(config.term().fontSize() * config.term().width() + config.term().fontSize()/2, 
				config.term().fontSize() * config.term().height() + config.term().fontSize()/2));
		gameOver.setBackground(config.term().background());
		gameOver.add(label);
		return gameOver;
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
		menu.add(factory.menuLabel("Mouse over UI elements for more information"));
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
		gameOver = gameOver(config);
		
		infoDisplay = infoDisplay(config);
		
		meterForeground = Util.htmlColor(config.term().foreground(0));
		
		healthWidget = factory.statusWidget(infoDisplay);
		
		window.getContentPane().add(termPanel, BorderLayout.LINE_START);
		window.getContentPane().add(statusPanel(config, factory), BorderLayout.CENTER);
		window.getContentPane().add(sidePanel(config), BorderLayout.LINE_END);
		window.getContentPane().add(factory.menuLabel(config.controls().about() + " for help and to update"), BorderLayout.PAGE_END);
		
		window.pack();
	}
	
	//Launches the main UI window
	public void launch() {
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.requestFocusInWindow();
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
	
	/**
	 * Sets the current hit points
	 * @param max
	 * 		Maximum hit points
	 * @param dmg
	 * 		Total damage taken
	 */
	public void setHealth(int max, int total) {
		//Calculate the number of characters are needed to represent total health on a 25 bar meter
		int bars = (total * 10)/max;
		if (bars > 10) bars = 10;
		if (bars < 1) bars = total > 0 ? 1 : 0;
		
		//Add filled section of health meter
		StringBuilder meter = new StringBuilder("<html><font color=\"")
				.append(meterForeground)
				.append("\">");
		
		for(int i = 0; i < bars; i++) {
			meter.append("\u25AE");
		}
		
		//Add empty section of health meter
		meter.append("</font><font color=\"")
			.append(meterForeground)
			.append("\">");
		
		for(int i = 0; i < 10 - bars; i++) {
			meter.append("\u25AF");
		}
		meter.append("</font></html>");
		
		healthWidget.setText(meter.toString());
		
		//Set detailed info for health
		healthWidget.setDetails(new StringBuilder("<html>Maximum Hit Points: ")
				.append(max)
				.append("<br>Total Hit Points: ")
				.append(total)
				.append("</html>").toString()
		);
	}
	
	//Shows about menu
	public void showAbout() {
		resetTerm();
		window.getContentPane().add(aboutMenu, BorderLayout.LINE_START);
		window.pack();
		window.repaint();
	}
	
	//Shows game over splash
	public void showGameOver() {
		resetTerm();
		window.getContentPane().add(gameOver, BorderLayout.LINE_START);
		window.pack();
		window.repaint();
	}
	
	//Shows terminal window
	public void showTerm() {
		resetTerm();
		window.getContentPane().add(termPanel, BorderLayout.LINE_START);
		window.pack();
		window.repaint();
	}
}
