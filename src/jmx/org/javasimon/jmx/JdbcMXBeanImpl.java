package org.javasimon.jmx;

import org.javasimon.*;
import org.javasimon.utils.SimonUtils;
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

	private Manager manager;
	private String prefix;

	/**
	 * MXBean constructor with prefix initialization to default ({@code org.javasimon.jdbc}).
	 *
	 * @param manager instance of {@link Manager}, typically {@code SimonManager.manager()}.
	 */
	public JdbcMXBeanImpl(Manager manager) {
		this.manager = manager;
		this.prefix = "org.javasimon.jdbc";
	}

	/**
	 * MXBean constructor with custom prefix initialization.
	 *
	 * @param manager instance of {@link Manager}, typically {@code SimonManager.manager()}.
	 * @param prefix custom prefix
	 */
	public JdbcMXBeanImpl(Manager manager, String prefix) {
		this.manager = manager;
		this.prefix = prefix;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setPrefix(String value) {
		prefix = value;
	}

	/**
	 * {@inheritDoc}
	 */
	public void enableMonitoring() {
		Simon s = manager.getSimon(prefix);
		if (s != null) {
			s.setState(SimonState.ENABLED, true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void disableMonitoring() {
		Simon s = manager.getSimon(prefix);
		if (s != null) {
			s.setState(SimonState.DISABLED, true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isMonitoringEnabled() {
		return manager.getSimon(prefix) != null && manager.getSimon(prefix).isEnabled();
	}

	/**
	 * {@inheritDoc}
	 */
	public void logToFile(String filename, String format) {
		stopLogging();

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
		stopLogging();

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
		stopLogging();

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
	public JdbcObjectInfo connectionsStat() {
		Stopwatch s = manager.getStopwatch(prefix+".conn");

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
	public JdbcObjectInfo statementsStat() {
		Stopwatch s = manager.getStopwatch(prefix+".stmt");

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
	public JdbcObjectInfo resultsetsStat() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getSqlCommands() {
		Simon s = manager.getSimon(prefix+".sql");
		if (s != null) {
			String[] names = new String[s.getChildren().size()];
			int i=0;
			for (Simon sn : s.getChildren()) {
				names[i++] = SimonUtils.localName(sn.getName());
			}
			return names;
		} else {
			return new String[0];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public StopwatchSample getSqlCommandStat(String cmdId) {
		if (manager.getSimon(prefix+".sql."+cmdId) != null) {
			return new StopwatchSample(
				(org.javasimon.StopwatchSample)manager.getStopwatch(prefix+".sql."+cmdId).sample());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String[] getSqls(String cmdId) {
		Simon s = manager.getSimon(prefix+".sql."+cmdId);
		if (s != null) {
			String[] names = new String[s.getChildren().size()];
			int i=0;
			for (Simon sn : s.getChildren()) {
				names[i++] = SimonUtils.localName(sn.getName());
			}
			return names;
		} else {
			return new String[0];
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public StopwatchSample getSqlStat(String sqlId) {
		if (manager != null) {
			for (String s : manager.simonNames()) {
				if (SimonUtils.localName(s).equals(sqlId)) {
					return new StopwatchSample(
						(org.javasimon.StopwatchSample)manager.getStopwatch(s).sample());
				}
			}
		}
		return null;
	}
}
