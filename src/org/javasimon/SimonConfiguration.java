package org.javasimon;

import java.io.IOException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Properties;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * SimonConfiguration.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Sep 6, 2008
 */
public final class SimonConfiguration {
	public static final String PROPERTY_CONFIG_FILE_NAME = "javasimon.config.file";

	public static final String PROPERTY_CONFIG_RESOURCE_NAME = "javasimon.config.resource";

	private static Map<String, SimonConfiguration> configs;

	private String type = null;
	private StatProcessorType statProcessorType = null;
	private SimonState state = null;

	public SimonConfiguration(String type, StatProcessorType statProcessorType, SimonState state) {
		this.type = type;
		this.statProcessorType = statProcessorType;
		this.state = state;
	}

	public static void init() {
		try {
			configs = new LinkedHashMap<String, SimonConfiguration>();
			initFromConfig();
		} catch (IOException e) {
			Logger.getLogger(SimonConfiguration.class.getName()).log(Level.SEVERE, "Simon config couldn't be processed correctly", e);
		}
	}

	private static void initFromConfig() throws IOException {
		String fileName = System.getProperty(PROPERTY_CONFIG_FILE_NAME);
		if (fileName != null) {
			FileReader reader = new FileReader(fileName);
			try {
				initFromReader(reader);
			} finally {
				reader.close();
			}
		}
		String resourceName = System.getProperty(PROPERTY_CONFIG_RESOURCE_NAME);
		if (resourceName != null) {
			InputStreamReader reader = new InputStreamReader(SimonManager.class.getClassLoader().getResourceAsStream(resourceName));
			try {
				initFromReader(reader);
			} finally {
				reader.close();
			}
		}
	}

	private static void initFromReader(Reader reader) throws IOException {
		Properties properties = new Properties();
		properties.load(reader);
		for (String name : properties.stringPropertyNames()) {
			String simonType = null;
			StatProcessorType statProcessorType = StatProcessorType.NULL;
			SimonState state = SimonState.INHERIT;

			String value = properties.getProperty(name);
			for (String keyword : value.split("[, ]+")) {
				keyword = keyword.toLowerCase();
				if (keyword.equals("stopwatch")) {
					simonType = keyword;
				} else if (keyword.equals("counter")) {
					simonType = keyword;
				} else if (keyword.equals("basic")) {
					statProcessorType = StatProcessorType.BASIC;
				} else if (keyword.equals("enable")) {
					state = SimonState.ENABLED;
				} else if (keyword.equals("disable")) {
					state = SimonState.DISABLED;
				} else {
					System.out.println("Unknown config value '" + keyword + "' for name '" + name + "'.");
				}
			}
			Simon simon = null;
			if (simonType == null) {
				simon = SimonManager.getUnknown(name);
			} else if (simonType.equals("stopwatch")) {
				simon = SimonManager.getStopwatch(name);
			} else if (simonType.equals("counter")) {
				simon = SimonManager.getCounter(name);
			}
			if (simon != null) {
				simon.setStatProcessor(statProcessorType.create());
				simon.setState(state, false);
			}
		}
	}
}
