/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import chensley.da.Config;

/**
 * JPanel with a drawable unicode text canvas
 * Serves as simulated text console output
 */
public class CanvasPanel extends JPanel {
	
	//A single unicode character
	private class Symbol {
		public Symbol(String icon, Color color, double xOffset, double yOffset) {
			this.icon = icon;
			this.color = color;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
		public String icon;
		public Color color;
		public double xOffset;
		public double yOffset;
	}
	
	
	private static final long serialVersionUID = 3535931962426783107L;
	
	private final Config config;
	private final List<Symbol> canvas;
	
	public CanvasPanel(Config config) {
		super();
		this.config = config;
		canvas = new ArrayList<>(config.term().width() * config.term().height());
		for(int i = 0; i < config.term().width() * config.term().height(); i++)
			canvas.add(null);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Clears any existing characters
		g.setFont(new Font(config.term().font(), Font.PLAIN, config.term().fontSize()));
		g.setColor(config.term().background());
		g.fillRect(0,  0, 
				config.term().width() * config.term().fontSize() + config.term().fontSize(), 
				config.term().height() * config.term().fontSize() + config.term().fontSize());
		//Draw each unicode character
		for(int x = 0; x < config.term().width(); x++) {
			for(int y = 0; y < config.term().height(); y++) {
				Symbol symbol = canvas.get(x * config.term().width() + y);
				if(symbol != null) {
					g.setColor(symbol.color);
					g.drawString(symbol.icon, 
							x * config.term().fontSize()  + config.term().fontSize()/2 + (int)(config.term().fontSize() * symbol.xOffset), 
							y * config.term().fontSize() + config.term().fontSize() + (int)(config.term().fontSize() * symbol.yOffset)
					);
				}
			}
		}
	}
	
	//Methods for drawing on canvas
	
	//Clear canvas
	public void clear() {
		for(int i = 0; i < canvas.size(); i++) canvas.set(i, null);
	}
	
	/**
	 * Draws a single unicode character on the canvas
	 * Replaces any item already on the canvas at that position
	 * @param icon
	 * 		Unicode character
	 * @param color
	 * 		AWT color
	 * @param x
	 * 		x position, as measured in characters from the left
	 * @param y
	 * 		y position, as measured in characters from the top
	 */
	public void draw(String icon, Color color, int x, int y, double xOffset, double yOffset) { 
		canvas.set(x * config.term().width() + y, new Symbol(icon, color, xOffset, yOffset));
	}

}
