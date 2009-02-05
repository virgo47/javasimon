package org.javasimon;

import org.javasimon.utils.SimonUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

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
		try {
			XMLStreamReader xr = XMLInputFactory.newInstance().createXMLStreamReader(reader);
			try {
				while (!xr.isStartElement()) {
					xr.next();
				}
				processStartElement(xr, "simon-configuration");
				if (isStartTag(xr, "callback")) {
					manager.installCallback(processCallback(xr));
				}
				while (isStartTag(xr, "simon")) {
					processSimon(xr);
				}
				assertEndTag(xr, "simon-configuration");
			} finally {
				xr.close();
			}
		} catch (XMLStreamException e) {
			manager.callback().warning(null, e);
		}
	}

	private Callback processCallback(XMLStreamReader xr) throws XMLStreamException {
		Map<String, String> attrs = processStartElement(xr, "callback");
		String klass = attrs.get("class");
		if (klass == null) {
			klass = CompositeCallback.class.getName();
		}
		System.out.println("klass = " + klass);
		Callback callback;
		try {
			callback = (Callback) Class.forName(klass).newInstance();
		} catch (InstantiationException e) {
			throw new SimonException(e);
		} catch (IllegalAccessException e) {
			throw new SimonException(e);
		} catch (ClassNotFoundException e) {
			throw new SimonException(e);
		} catch (ClassCastException e) {
			throw new SimonException(e);
		}

		while (isStartTag(xr, "set")) {
			processSet(xr, callback);
		}
		while (isStartTag(xr, "callback")) {
			callback.addCallback(processCallback(xr));
		}
		processEndElement(xr, "callback");
		return callback;
	}

	private void processSet(XMLStreamReader xr, Callback callback) throws XMLStreamException {
		Map<String, String> attrs = processStartElement(xr, "set", "property", "value");
		processEndElement(xr, "set");
	}

	private void processSimon(XMLStreamReader xr) throws XMLStreamException {
		Map<String, String> attrs = processStartElement(xr, "simon", "pattern");
		String pattern = attrs.get("pattern");
		String simonType = attrs.get("type");
		StatProcessorType statProcessorType = attrs.get("stats") != null ? StatProcessorType.valueOf(attrs.get("stats").toUpperCase()) : null;
		SimonState state = attrs.get("state") != null ? SimonState.valueOf(attrs.get("state").toUpperCase()) : null;
		configs.put(new ConfigPattern(pattern), new SimonConfiguration(simonType, statProcessorType, state));

//		boolean create = attrs.get("create") != null && Boolean.valueOf(attrs.get("create"));
//		if (SimonUtils.checkName(name)) {
//			create = true;
//		} else {
//			reportError("Invalid name used with create option on line " + lineNum);
//		}
		processEndElement(xr, "simon");
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

	/**
	 * Sets the callback property.
	 *
	 * @param callback callback object
	 * @param property name of the property
	 * @param value value of the property
	 */
	// TODO ...
	private void setProperty(Callback callback, String property, String value) {
		try {
			Method setter = callback.getClass().getMethod(setterName(property), String.class);
			setter.invoke(callback, value);
		} catch (NoSuchMethodException e) {
			throw new SimonException(e);
		} catch (IllegalAccessException e) {
			throw new SimonException(e);
		} catch (InvocationTargetException e) {
			throw new SimonException(e);
		}
	}

	private String setterName(String name) {
		return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	// XML Utils

	private Map<String, String> processStartElement(XMLStreamReader reader, String elementName, String... requiredAttributes) throws XMLStreamException {
		Map<String, String> attrs = processStartElementPrivate(reader, elementName, requiredAttributes);
		reader.nextTag();
		return attrs;
	}

	private Map<String, String> processStartElementPrivate(XMLStreamReader reader, String elementName, String... requiredAttributes) throws XMLStreamException {
		assertStartTag(reader, elementName);
		Map<String, String> attrs = readAttributes(reader);
		for (String attr : requiredAttributes) {
			if (!attrs.containsKey(attr)) {
				throw new XMLStreamException("Attribute '" + attr + "' MUST be present (element: " + elementName + "). " + readerPosition(reader));
			}
		}
		return attrs;
	}

	private void assertStartTag(XMLStreamReader reader, String name) throws XMLStreamException {
		if (!reader.isStartElement()) {
			throw new XMLStreamException("Assert start tag - wrong event type " + reader.getEventType() + " (expected name: " + name + ") " + readerPosition(reader));
		}
		assertName(reader, "start tag", name);
	}

	private Map<String, String> readAttributes(XMLStreamReader reader) {
		Map<String, String> attributes = new LinkedHashMap<String, String>();
		int attrCount = reader.getAttributeCount();
		for (int i = 0; i < attrCount; i++) {
			attributes.put(reader.getAttributeName(i).toString(), reader.getAttributeValue(i));
		}
		return attributes;
	}

	private void assertName(XMLStreamReader reader, String operation, String name) throws XMLStreamException {
		if (!reader.getLocalName().equals(name)) {
			throw new XMLStreamException("Assert " + operation + " - wrong element name " + reader.getName().toString() + " (expected name: " + name + ") " + readerPosition(reader));
		}
	}

	private String readerPosition(XMLStreamReader reader) {
		return "[line: " + reader.getLocation().getLineNumber() + ", column: " + reader.getLocation().getColumnNumber() + "]";
	}

	private void assertEndTag(XMLStreamReader reader, String name) throws XMLStreamException {
		if (!reader.isEndElement()) {
			throw new XMLStreamException("Assert end tag - wrong event type " + reader.getEventType() + " (expected name: " + name + ") " + readerPosition(reader));
		}
		assertName(reader, "end tag", name);
	}

	private boolean isStartTag(XMLStreamReader reader, String name) {
		return reader.isStartElement() && reader.getLocalName().equals(name);
	}

	private void processEndElement(XMLStreamReader reader, String name) throws XMLStreamException {
		assertEndTag(reader, name);
		reader.nextTag();
	}
}