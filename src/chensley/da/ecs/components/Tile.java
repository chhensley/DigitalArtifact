/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

import java.awt.Color;

public class Tile {
	private final String icon;
	private final Color color;
	private final Color fowColor; 
	private final double xOffset;
	private final double yOffset;
	
	public Tile(String icon, Color color, Color fowColor, double xOffset, double yOffset) {
		this.icon = icon;
		this.color = color;
		this.fowColor = fowColor;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
	
	public String icon() {
		return icon;
	}
	
	public Color color() {
		return color;
	}
	
	public Color fowColor() {
		return fowColor;
	}
	
	public double xOffset() {
		return xOffset;
	}
	
	public double yOffset() { 
		return yOffset;
	}
}
