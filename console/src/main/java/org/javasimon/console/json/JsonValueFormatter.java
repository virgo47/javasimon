package org.javasimon.console.json;

import org.javasimon.console.ValueFormatter;

/**
 * Value formatter for JSON reponses
 * @author gquintana
 */
public class JsonValueFormatter extends ValueFormatter {

	@Override
	public String formatNull() {
		return "\"\"";
	}

	@Override
	public String formatString(String value) {
		String sValue;
		if (value == null) {
			sValue = formatNull();
		} else {
			sValue = "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"").replace("/", "\\/") + "\"";
		}
		return sValue;
	}
};
