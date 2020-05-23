/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

//Single menu item
public class MenuItem extends JLabel {
	
	//Allows the menu item to be clicked, triggering the key event associated with this menu item
	private class ItemMouseListener implements MouseListener {
		
		private final int keyCode;
		private final Robot robot;

		private ItemMouseListener(int keyCode, Robot robot) {
			super();
			this.keyCode = keyCode;
			this.robot = robot;
		}
		
		private void invertColors(Component parent) {
			Color foreground = parent.getForeground();
			parent.setForeground(parent.getBackground());
			parent.setBackground(foreground);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			robot.keyPress(keyCode);
			robot.keyRelease(keyCode);
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			invertColors(e.getComponent());
		}

		@Override
		public void mouseExited(MouseEvent e) {
			invertColors(e.getComponent());	
		}

		@Override
		public void mousePressed(MouseEvent e) {
			//Do nothing
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//Do nothing
		}
		
	}
	
	private static final long serialVersionUID = -6703079337419461189L;
	
	//Used to trigger key press events
	private static Robot ROBOT;
	static {
		try {
			ROBOT = new Robot();
		} catch (AWTException e) {
			//Do nothing
		}
		
	}
	
	/**
	 * Constructor
	 * @param icon
	 * 		Menu icon
	 * @param option
	 * 		Menu option text
	 * @param description
	 * 		Optional description
	 * @param keyCode
	 * 		Key code associated with this menu item
	 */
	public MenuItem(String icon, String option, String description, int keyCode) {
		super();
		String key = KeyEvent.getKeyText(keyCode);
		
		StringBuilder content = new StringBuilder("[")
				.append(key)
				.append("] ")
				.append(icon)
				.append(" ")
				.append(option.toUpperCase());
		if (description != null) {
			content.append("|").append(description);
		}
		
		setText(content.toString());
		
		addMouseListener(new ItemMouseListener(keyCode, ROBOT));
	}
}
