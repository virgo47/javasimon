package org.javasimon.callback.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concrete log template using {@link Logger}.
 *
 * @author gquintana
 */
public class JULLogTemplate<C> extends LogTemplate<C> {
	/**
	 * Target log template
	 */
	private final Logger logger;
	/**
	 * Logging level
	 */
	private Level level;

	/**
	 * Constructor
	 *
	 * @param logger Logger
	 * @param level Level
	 */
	public JULLogTemplate(Logger logger, Level level) {
		this.logger = logger;
		this.level = level;
	}

	/**
	 * Constructor
	 *
	 * @param loggerName Logger name
	 * @param level Level
	 */
	public JULLogTemplate(String loggerName, Level level) {
		this.logger = Logger.getLogger(loggerName);
		this.level = level;
	}

	/**
	 * Get level
	 *
	 * @return Level
	 */
	public Level getLevel() {
		return level;
	}

	/**
	 * Change level
	 *
	 * @param level Level
	 */
	public void setLevel(Level level) {
		this.level = level;
	}

	/**
	 * Get logger
	 *
	 * @return Logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * {@inheritDoc }
	 * Logger is enabled if underlying logger is {@link Logger#isLoggable} is true for current level
	 */
	public boolean isEnabled(C context) {
		return logger.isLoggable(level);
	}

	public void log(String message) {
		logger.log(level, message);
	}
}
