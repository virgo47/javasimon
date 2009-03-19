package org.javasimon.jmx;

import org.javasimon.*;
import org.javasimon.jdbc.logging.LoggingCallback;

import java.util.Date;

/**
 * Jdbc MXBean implementation. See {@link org.javasimon.jmx.JdbcMXBean} for interface
 * documentation.
 * Most methods are implemented by calling the {@link Manager}.
 *
 * @author Radovan Sninsky
 * @version $Revision: 281 $ $Date: 2009-01-20 12:21:37 +0100 (ut, 20 I 2009) $
 * @created 3.10.2008 15:56:29
 * @since 2
 */
public class JdbcMXBeanImpl implements JdbcMXBean {

	private static final String DP = "org.javasimon.jdbc"; 

	private Manager manager;

	/**
	 * MXBean constructor.
	 *
	 * @param manager instance of {@link Manager}, typically {@code SimonManager.manager()}.
	 */
	public JdbcMXBeanImpl(Manager manager) {
		this.manager = manager;
	}

	/**
	 * {@inheritDoc}
	 */
	public void enableMonitoring() {
		enableMonitoring(DP);
	}

	/**
	 * {@inheritDoc}
	 */
	public void disableMonitoring() {
		disableMonitoring(DP);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMonitoringEnabled() {
		return isMonitoringEnabled(DP);
	}

	/**
	 * {@inheritDoc}
	 */
	public void enableMonitoring(String p) {
		Simon s = manager.getSimon(p);
		if (s != null) {
			s.setState(SimonState.ENABLED, true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void disableMonitoring(String p) {
		Simon s = manager.getSimon(DP);
		if (s != null) {
			s.setState(SimonState.DISABLED, true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMonitoringEnabled(String p) {
		return manager.getSimon(p) != null && manager.getSimon(p).isEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void logToFile(String filename, String format) {
		logToFile(filename, format, DP);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logToFile(String filename, String format, String prefix) {
		stopLogging(prefix);

		// install new jdbc log callback
		LoggingCallback jlc =  new LoggingCallback();
		jlc.setPrefix(prefix);
		jlc.setLogFilename(filename);
		jlc.setLogFormat(format);
		manager.callback().addCallback(jlc);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logToLogger(String logger, String format) {
		logToLogger(logger, format, DP);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logToLogger(String logger, String format, String prefix) {
		stopLogging(prefix);

		// install new jdbc log callback
		LoggingCallback jlc =  new LoggingCallback();
		jlc.setPrefix(prefix);
		jlc.setLoggerName(logger);
		jlc.setLogFormat(format);
		manager.callback().addCallback(jlc);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logToConsole(String format) {
		logToConsole(format, DP);
	}

	/**
	 * {@inheritDoc}
	 */
	public void logToConsole(String format, String prefix) {
		stopLogging(prefix);

		// install new jdbc log callback
		LoggingCallback jlc =  new LoggingCallback();
		jlc.setPrefix(prefix);
		jlc.setLogToConsole(true);
		jlc.setLogFormat(format);
		manager.callback().addCallback(jlc);
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopLogging() {
		stopLogging(DP);
	}

	/**
	 * {@inheritDoc}
	 */
	public void stopLogging(String prefix) {
		// remove ALL jdbc log callback if exists for default prefix
		for (Callback c : manager.callback().callbacks()) {
			if (c instanceof LoggingCallback && ((LoggingCallback)c).getPrefix().equalsIgnoreCase(prefix)) {
				manager.callback().removeCallback(c);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public JdbcObjectInfo getConnInfo() {
		Stopwatch s = manager.getStopwatch("sk.bgs.controlling.jdbc.conn");

		if (s != null) {
			return new JdbcObjectInfo(
				s.getActive(),
				s.getMaxActive(),
				new Date(s.getMaxActiveTimestamp()),
				s.getCounter(),
				s.getCounter() - s.getActive(),
				s.getMin(),
				s.getMax(),
				s.getTotal()
			);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public JdbcObjectInfo getStmtInfo() {
		Stopwatch s = manager.getStopwatch("sk.bgs.controlling.jdbc.stmt");

		if (s != null) {
			return new JdbcObjectInfo(
				s.getActive(),
				s.getMaxActive(),
				new Date(s.getMaxActiveTimestamp()),
				s.getCounter(),
				s.getCounter() - s.getActive(),
				s.getMin(),
				s.getMax(),
				s.getTotal()
			);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public JdbcObjectInfo getRsetInfo() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getSqlCommands() {
		Simon s = manager.getSimon("sk.bgs.controlling.jdbc.sql");
		if (s != null) {
			return new String[s.getChildren().size()];
		} else {
			return new String[0];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public StopwatchSample getSqlCommandInfo(String cmdId) {
		if (manager.getSimon("sk.bgs.controlling.jdbc.sql."+cmdId) != null) {
			return new StopwatchSample(
				(org.javasimon.StopwatchSample)manager.getStopwatch("sk.bgs.controlling.jdbc.sql."+cmdId).sample());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getSqls(String cmdId) {
		Simon s = manager.getSimon("sk.bgs.controlling.jdbc.sql."+cmdId);
		if (s != null) {
			return new String[s.getChildren().size()];
		} else {
			return new String[0];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public StopwatchSample getSqlInfo(String sqlId) {
		if (manager.getSimon("sk.bgs.controlling.jdbc.sql."+sqlId) != null) {
			return new StopwatchSample(
				(org.javasimon.StopwatchSample)manager.getStopwatch("sk.bgs.controlling.jdbc.sql."+sqlId).sample());
		}
		return null;
	}
}
