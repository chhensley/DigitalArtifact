/**
 * Copyright(C) 2020 Christopher Hensley. 
 * Do as thou wilt shall be the whole of the License. 
 * Love is the License, love under will.
 **/
package chensley.da.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 * Logs messages to a swing JTextArea
 * Used to log messages to onscreen log
 */
public class JTextAreaHandler extends Handler {
	
	/**
	 * Default formatter for JTextAreaHandler
	 */
	private static class JTextAreaFormatter extends Formatter {
		
		private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
		
		public JTextAreaFormatter() {
			super();
			DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
		}
		
		@Override
		//[HH::mm::ss]message text
		public String format(LogRecord record) {
			Date date = new Date(record.getMillis());
			String timestamp = DATE_FORMAT.format(date);
			
			return new StringBuilder("[")
					.append(timestamp)
					.append("]")
					.append(record.getMessage()).toString();
		 }
	}
	
	private final JTextArea textArea;
	
	public JTextAreaHandler(JTextArea textArea) {
		super();
		this.textArea = textArea;
		this.setFormatter(new JTextAreaFormatter());
	}

	@Override
	public void close() throws SecurityException {
		//Do nothing
	}

	@Override
	public void flush() {
		//Do nothing
	}

	@Override
	public void publish(LogRecord record) {
		if (this.isLoggable(record)) {
			SwingUtilities.invokeLater(() -> {
				textArea.append(this.getFormatter().format(record));
				textArea.append(System.lineSeparator());
			});
		}
	}

}
