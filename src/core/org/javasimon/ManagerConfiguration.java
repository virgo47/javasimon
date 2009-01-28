package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Holds configuration for one Simon Manager. Configuration is read from the stream
 * and it is merged with any existing configuration read before. Method {@link #clear()}
 * must be used in order to reset this configuration object.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 6, 2008
 */
public final class ManagerConfiguration {
	private static final String WILDCARD_STAR = "*";

	private Map<ConfigPattern, SimonConfiguration> configs;

	private final Manager manager;

	ManagerConfiguration(Manager manager) {
		this.manager = manager;
		clear();
	}

	void clear() {
		configs = new LinkedHashMap<ConfigPattern, SimonConfiguration>();
	}

	/**
	 * Reads config from provided buffered reader. Package level because of tests.
	 *
	 * @param reader reader containing configuration
	 * @throws IOException thrown if problem occurs while reading from the reader
	 */
	void readConfig(Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		try {
			int lineNum = 0;
			while (true) {
				String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				lineNum++;
				line = line.trim();
				if (line.length() == 0) {
					continue;
				}
				processConfigLine(lineNum, line);
			}
		} finally {
			bufferedReader.close();
		}
	}

	private void processConfigLine(int lineNum, String line) {
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

	private void reportError(String error) {
		manager.callback().warning("Config error: " + error, null);
		System.out.println(error);
	}

	private void processSimonConfig(int lineNum, String name, String value) {
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
	SimonConfiguration getConfig(String name) {
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
	class ConfigPattern {
		private String pattern;
		private String all;
		private String start;
		private String end;
		private String middle;
		private static final String INVALID_PATTERN = "Invalid configuration pattern: ";

		/**
		 * Creates Simon name pattern used to match config file entries.
		 *
		 * @param pattern Simon name pattern
		 * @throws org.javasimon.SimonException if pattern is not valid (runtime exception)
		 */
		ConfigPattern(String pattern) {
			this.pattern = pattern;
			if (!pattern.contains(WILDCARD_STAR)) {
				all = pattern;
				if (!SimonUtils.checkName(all)) {
					throw new SimonException(INVALID_PATTERN + pattern);
				}
				return;
			}
			if (pattern.equals(WILDCARD_STAR)) {
				middle = ""; // everything contains this
				return;
			}
			if (pattern.startsWith(WILDCARD_STAR) && pattern.endsWith(WILDCARD_STAR)) {
				middle = pattern.substring(1, pattern.length() - 2);
				if (!SimonUtils.checkName(middle)) {
					throw new SimonException(INVALID_PATTERN + pattern);
				}
				return;
			}
			int ix = pattern.lastIndexOf('*');
			if (ix != pattern.indexOf('*')) {
				throw new SimonException(INVALID_PATTERN + pattern);
			}
			if (!pattern.endsWith(WILDCARD_STAR)) {
				end = pattern.substring(ix + 1);
				if (!SimonUtils.checkName(end)) {
					throw new SimonException(INVALID_PATTERN + pattern);
				}
			}
			if (!pattern.startsWith(WILDCARD_STAR)) {
				start = pattern.substring(0, ix);
				if (!SimonUtils.checkName(start)) {
					throw new SimonException(INVALID_PATTERN + pattern);
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
			if (this == o) {
				return true;
			}
			if (o == null || getClass() != o.getClass()) {
				return false;
			}

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