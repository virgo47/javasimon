package org.javasimon.jmx;

import java.beans.ConstructorProperties;

/**
 * Value object for retrieving simon name and type info via Simon MXBean ({@link SimonMXBean}).
 * This value object make possible to retrieve list of all instantiated simons together with
 * their types, so no multiple roundtrips are needed.
 *
 * @author Radovan Sninsky
 * @version $ Revision $ $ Date $
 * @created 26.1.2009 16:05:29
 * @since 2
 * @see SimonMXBean#getSimonInfos
 */
public final class SimonInfo {

	public static final String UNKNOWN = "unknown";
	public static final String STOPWATCH = "stopwatch";
	public static final String COUNTER = "counter";

	private String name;
	private String type;

	/**
	 * Class constructor due to JMX requirements.
	 * @param name simon name
	 * @param type simon type ({@code 'stopwatch'} or {@code 'counter'})
	 */
	@ConstructorProperties({"name", "type"})
	public SimonInfo(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Returns fully hierarchical name of simon.
	 * @return simon name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns simon type, either {@code 'stopwatch'} or {@code 'counter'} strings.
	 * 
	 * @return simon type
	 * @see org.javasimon.jmx.SimonInfo#UNKNOWN
	 * @see org.javasimon.jmx.SimonInfo#STOPWATCH
	 * @see org.javasimon.jmx.SimonInfo#COUNTER
	 */
	public String getType() {
		return type;
	}
}
