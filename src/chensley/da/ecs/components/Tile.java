/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ecs.components;

import javafx.scene.paint.Color;

@SuppressWarnings("restriction") //Incorrectly tags javafx Color
public class Tile {
	private final String icon;
	private final Color color;
	
	public Tile(String icon, Color color) {
		this.icon = icon;
		this.color = color;
	}
	
	public String icon() {
		return icon;
	}
	
	public Color color() {
		return color;
	}
}
