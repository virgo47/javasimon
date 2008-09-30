package org.javasimon;

import org.javasimon.utils.SimonUtils;

import java.io.*;
import java.util.logging.Logger;
import java.util.logging.Level;
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

	/**
	 * Initilizes the configuration facility.
	 */
	public static void init() {
		try {
			configs = new LinkedHashMap<ConfigPattern, SimonConfiguration>();
			initFromConfig();
		} catch (IOException e) {
			Logger.getLogger(SimonConfigManager.class.getName()).log(Level.SEVERE, "Simon config couldn't be processed correctly", e);
		}
	}

	private static void initFromConfig() throws IOException {
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

	private static void initFromReader(BufferedReader reader) throws IOException {
		int lineNum = 0;
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}
			lineNum++;
			if (line.trim().length() == 0) {
				continue;
			}
			line = line.trim().split("[#;]", 2)[0]; // ignore comments
			String[] sa = line.split("[ =]+", 2);
			if (sa.length != 2) {
				System.out.println("Invalid format on line " + lineNum);
				continue;
			}
			String name = sa[0];
			String value = sa[1];

			String simonType = null;
			StatProcessorType statProcessorType = null;
			SimonState state = null;

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
				} else if (keyword.equals("enable")) {
					state = SimonState.ENABLED;
				} else if (keyword.equals("disable")) {
					state = SimonState.DISABLED;
				} else if (keyword.equals("inherit")) {
					state = SimonState.INHERIT;
				} else {
					System.out.println("Unknown config value '" + keyword + "' for name '" + name + "' on line " + lineNum + ".");
				}
			}
			configs.put(new ConfigPattern(name), new SimonConfiguration(simonType, statProcessorType, state));
		}
	}

	/**
	 * Returns configuration for the Simon with the specified name.
	 *
	 * @param name Simon name
	 * @return configuration for that particular Simon
	 */
	public SimonConfiguration getConfig(String name) {
		return null; // TODO
	}

	/**
	 * Matches Simon name patterns from configuration.
	 */
	static class ConfigPattern {
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
					throw new SimonException("Invalid configuration pattern: " + pattern);
				}
			}
		}

		/**
		 * Checks if tested pattern matches this pattern.
		 *
		 * @param pattern tested pattern
		 * @return true if tested pattern matches this pattern
		 */
		public boolean matches(String pattern) {
			if (all != null) {
				return all.equals(pattern);
			}
			if (middle != null) {
				return pattern.contains(middle);
			}
			if (start != null && !pattern.startsWith(start)) {
				return false;
			}
			return end == null || pattern.endsWith(end);
		}
	}
}