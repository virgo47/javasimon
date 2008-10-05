package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Stores Simon API configuration.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 6, 2008
 */
public final class SimonConfigManager {
	/**
	 * Property name for the Simon configuration file.
	 */
	public static final String PROPERTY_CONFIG_FILE_NAME = "javasimon.config.file";

	/**
	 * Property name for the Simon configuration resource.
	 */
	public static final String PROPERTY_CONFIG_RESOURCE_NAME = "javasimon.config.resource";

	private static Map<ConfigPattern, SimonConfiguration> configs;

	private static boolean strictConfig;

	/**
	 * Initilizes the configuration facility.
	 *
	 * @throws java.io.IOException thrown if config file or config resource is not found
	 */
	public static void init() throws IOException {
		String fileName = System.getProperty(PROPERTY_CONFIG_FILE_NAME);
		if (fileName != null) {
			BufferedReader reader = new BufferedReader(
				new FileReader(fileName));
			try {
				initFromReader(reader);
			} finally {
				reader.close();
			}
		}
		String resourceName = System.getProperty(PROPERTY_CONFIG_RESOURCE_NAME);
		if (resourceName != null) {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(SimonManager.class.getClassLoader().getResourceAsStream(resourceName)));
			try {
				initFromReader(reader);
			} finally {
				reader.close();
			}
		}
	}

	/**
	 * Reads config from provided buffered reader. Package level because of tests.
	 *
	 * @param reader reader containing configuration
	 * @throws IOException thrown if problem occurs while reading from the reader
	 */
	static void initFromReader(BufferedReader reader) throws IOException {
		strictConfig = false;
		configs = new LinkedHashMap<ConfigPattern, SimonConfiguration>();
		int lineNum = 0;
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			lineNum++;
			line = line.trim();
			if (line.length() == 0) {
				continue;
			}
			if (line.equalsIgnoreCase("strict")) {
				strictConfig = true;
				continue;
			}
			processConfigLine(lineNum, line);
		}
	}

	private static void processConfigLine(int lineNum, String line) {
		line = line.trim().split("[#;]", 2)[0]; // ignore comments
		String[] sa = line.split("[ =]+", 2);
		if (sa.length != 2) {
			reportError("Invalid format on line " + lineNum);
			return;
		}
		String name = sa[0];
		String value = sa[1];

		processSimonConfig(lineNum, name, value);
	}

	private static void reportError(String error) {
		if (strictConfig) {
			throw new SimonException("Config error: " + error);
		}
		System.out.println(error);
	}

	private static void processSimonConfig(int lineNum, String name, String value) {
		String simonType = null;
		StatProcessorType statProcessorType = null;
		SimonState state = null;
		boolean create = false;

		for (String keyword : value.split("[, ]+")) {
			keyword = keyword.toLowerCase();
			if (keyword.equals("stopwatch")) {
				simonType = keyword;
			} else if (keyword.equals("counter")) {
				simonType = keyword;
			} else if (keyword.equals("basicstats")) {
				statProcessorType = StatProcessorType.BASIC;
			} else if (keyword.equals("nullstats")) {
				statProcessorType = StatProcessorType.NULL;
			} else if (keyword.equals("enabled")) {
				state = SimonState.ENABLED;
			} else if (keyword.equals("disabled")) {
				state = SimonState.DISABLED;
			} else if (keyword.equals("inherit")) {
				state = SimonState.INHERIT;
			} else if (keyword.equals("create")) {
				if (SimonUtils.checkName(name)) {
					create = true;
				} else {
					reportError("Invalid name used with create option on line " + lineNum);
				}
			} else {
				reportError("Unknown config value '" + keyword + "' for name '" + name + "' on line " + lineNum + ".");
			}
		}
		configs.put(new ConfigPattern(name), new SimonConfiguration(simonType, statProcessorType, state));
		if (create) {
			// TODO
		}
	}

	/**
	 * Returns configuration for the Simon with the specified name.
	 *
	 * @param name Simon name
	 * @return configuration for that particular Simon
	 */
	static SimonConfiguration getConfig(String name) {
		String type = null;
		StatProcessorType spType = null;
		SimonState state = null;

		for (ConfigPattern pattern : configs.keySet()) {
			if (pattern.matches(name)) {
				SimonConfiguration config = configs.get(pattern);
				if (config.getState() != null) {
					state = config.getState();
				}
				if (config.getType() != null) {
					type = config.getType();
				}
				if (config.getStatProcessorType() != null) {
					spType = config.getStatProcessorType();
				}
			}
		}
		return new SimonConfiguration(type, spType, state);
	}

	/**
	 * Matches Simon name patterns from configuration.
	 */
	static class ConfigPattern {
		private String pattern;
		private String all;
		private String start;
		private String end;
		private String middle;

		/**
		 * Creates Simon name pattern used to match config file entries.
		 *
		 * @param pattern Simon name pattern
		 * @throws org.javasimon.SimonException if pattern is not valid (runtime exception)
		 */
		ConfigPattern(String pattern) {
			this.pattern = pattern;
			if (!pattern.contains("*")) {
				all = pattern;
				if (!SimonUtils.checkName(all)) {
					throw new SimonException("Invalid configuration pattern: " + pattern);
				}
				return;
			}
			if (pattern.equals("*")) {
				middle = ""; // everything contains this
				return;
			}
			if (pattern.startsWith("*") && pattern.endsWith("*")) {
				middle = pattern.substring(1, pattern.length() - 2);
				if (!SimonUtils.checkName(middle)) {
					throw new SimonException("Invalid configuration pattern: " + pattern);
				}
				return;
			}
			int ix = pattern.lastIndexOf('*');
			if (ix != pattern.indexOf('*')) {
				throw new SimonException("Invalid configuration pattern: " + pattern);
			}
			if (!pattern.endsWith("*")) {
				end = pattern.substring(ix + 1);
				if (!SimonUtils.checkName(end)) {
					throw new SimonException("Invalid configuration pattern: " + pattern);
				}
			}
			if (!pattern.startsWith("*")) {
				start = pattern.substring(0, ix);
				if (!SimonUtils.checkName(start)) {
					throw new SimonException("Invalid Simon configuration pattern: " + pattern);
				}
			}
		}

		/**
		 * Checks if Simon name matches this pattern.
		 *
		 * @param name tested name
		 * @return true if tested pattern matches this pattern
		 */
		public boolean matches(String name) {
			if (all != null) {
				return all.equals(name);
			}
			if (middle != null) {
				return name.contains(middle);
			}
			if (start != null && !name.startsWith(start)) {
				return false;
			}
			return end == null || name.endsWith(end);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			ConfigPattern that = (ConfigPattern) o;

			return !(pattern != null ? !pattern.equals(that.pattern) : that.pattern != null);
		}

		@Override
		public int hashCode() {
			return (pattern != null ? pattern.hashCode() : 0);
		}

		@Override
		public String toString() {
			return "ConfigPattern: " + pattern;
		}
	}
}