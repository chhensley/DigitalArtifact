/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

//An updatable widget for displaying player status
public class StatusWidget extends JLabel{
	
	//Mouse listener for widget
	private class ItemMouseListener implements MouseListener {
		
		private final JLabel info;
		private final StatusWidget parent;
		private String previousText;
		
		/**
		 * Constructor
		 * @param parent
		 * 		element that this listener belongs to
		 * @param info
		 * 		Label for displaying additional details
		 */
		public ItemMouseListener(StatusWidget parent, JLabel info) {
			this.info = info;
			this.parent = parent;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			//Do nothing
		}

		@Override
		//When the cursor is over the widget display details 
		public void mouseEntered(MouseEvent e) {
			previousText = info.getText();
			info.setText(parent.details());
		}

		@Override
		//When the cursor is no long over widget hide details
		public void mouseExited(MouseEvent e) {
			info.setText(previousText);
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
	
	private static final long serialVersionUID = 99018804897880490L;
	
	private String details;

	/**
	 * Constructor
	 * @param info
	 * 		Label for displaying detailed information
	 */
	public StatusWidget(JLabel info) {
		super();
		addMouseListener(new ItemMouseListener(this, info));
	}
	
	/**
	 * Sets text displayed on widget
	 * @param icon
	 * 		Icon associated with widget
	 * @param text
	 * 		Summary text displayed in widget
	 */
	public void setText(String icon, String text) {
		String content = new StringBuilder("  ")
				.append(icon)
				.append(" ")
				.append(text).toString();
		
		setText(content);
	}
	
	//Sets details displayed in on screen information window
	public String details() { return details; }
	public void setDetails(String details) { this.details = details; }
}
