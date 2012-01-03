package org.javasimon.console.action;

import org.javasimon.console.ValueFormatter;

/**
 * Value formatter for CSV reponse format
 * @author gquintana
 */
public class CsvValueFormatter extends ValueFormatter {

	@Override
	public String formatNull() {
		return "";
	}

	@Override
	public String formatString(String value) {
		String sValue;
		if (value == null) {
			sValue = formatNull();
		} else {
			sValue = "\"" + value.replace("\"", "\"\"") + "\"";
		}
		return sValue;
	}
};
