/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import chensley.da.Config;

/**
 * Factory for standardized GUI elements
 */
public class GUIFactory {
	
	//Modular dialog used to simulate terminal based menu
	public class TermMenu extends JPanel {
		
		private static final long serialVersionUID = -5394668402612936175L;

		public TermMenu() {
			super();
			setLayout(new GridBagLayout());
		}
		
		@Override
		public Component add(Component component) {
			GridBagConstraints constraints = new GridBagConstraints();
	        constraints.weightx = 1.0;
	        constraints.weighty = 0.0;
	        constraints.fill = GridBagConstraints.HORIZONTAL;
	        constraints.anchor = GridBagConstraints.NORTHEAST;
	        constraints.gridx = 0;
	        constraints.gridy = this.getComponentCount();
			
			this.add(component, constraints);
			return component;
		}
		
		/**
		 * Call at the very end to build menu
		 */
		public void build() {
			GridBagConstraints constraints = new GridBagConstraints();
	        constraints.weightx = 1.0;
	        constraints.weighty = 1.0;
	        constraints.fill = GridBagConstraints.BOTH;
	        constraints.anchor = GridBagConstraints.NORTHEAST;
	        constraints.gridx = 0;
	        constraints.gridy = this.getComponentCount();
	        
	        this.add(new JLabel(""), constraints);
		}
	}
	
	
	private final Config config;
	
	public GUIFactory(Config config) {
		this.config = config;
	};
	
	//Returns simulated terminal menu
	public TermMenu menu() {
		TermMenu menu = new TermMenu();

		menu.setBackground(config.term().background());
		menu.setForeground(config.term().foreground(2));
		
		menu.setPreferredSize(new Dimension(config.term().fontSize() * config.term().width() + config.term().fontSize()/2, 
				config.term().fontSize() * config.term().height() + config.term().fontSize()/2 ));
		menu.setBorder(new EmptyBorder(
					config.term().fontSize()/2 , 
					config.term().fontSize()/2 , 
					config.term().fontSize()/2 + config.term().fontSize()%2,
					config.term().fontSize()/2 + config.term().fontSize()%2
				));
		return menu;
	}
	
	/**
	 * Creates section header for terminal menu
	 * @param text
	 * 		Header text
	 * @return
	 * 		Header label
	 */
	public JLabel menuTitle(String text) {
		JLabel title =  new JLabel("### " + text.toUpperCase() + " ###");
		title.setForeground(config.term().foreground(0));
		title.setFont(new Font(config.term().font(), Font.PLAIN, config.term().fontSize()));
		
		return title;
	}
	
	/**
	 * Creates a single item for a terminal menu
	 * @param icon
	 * 		Unicode string to display as icon for item
	 * @param option
	 * 		Option label displayed to user
	 * @param description
	 * 		Option description, null if none
	 * @param keyCode
	 * 		Keycode to send when this item is clicked. Currently not implemented.
	 * @return
	 * 		Menu item component
	 */
	public JLabel menuItem(String icon, String option, String description, int keyCode) {
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
		
		JLabel item = new JLabel(content.toString());
		item.setFont(new Font(config.term().font(), Font.PLAIN, config.term().fontSize()));
		item.setForeground(config.term().foreground(1));
		item.setBackground(config.term().background());
		item.setOpaque(true);
		
		return item;
	}
	
	/**
	 * Plain text label inside a terminal menu
	 * @param text
	 * 		Label text
	 * @return
	 * 		Label
	 */
	public JLabel menuLabel(String text) {
		JLabel label = new JLabel(text);
		label.setForeground(config.term().foreground(2));
		label.setFont(new Font(config.term().font(), Font.PLAIN, config.term().fontSize()));
		return label;
	}
	
	//Creates a blank label for use inside a terminal menu
	public JLabel menuSpacer() {
		return new JLabel(" ");
	}
}
