package org.javasimon.console.action;

import org.javasimon.console.text.BaseStringifier;
import org.javasimon.console.text.Stringifier;
import org.javasimon.console.text.StringifierFactory;

/**
 * Value formatter for CSV reponse format
 * @author gquintana
 */
public class CsvStringifierFactory extends StringifierFactory {
	@Override
	protected Stringifier<String> registerStringStringifier(Stringifier nullStringifier) {
		Stringifier<String> stringStringifier=new BaseStringifier<String>(nullStringifier) {
			@Override
			protected String doToString(String s) {
				return"\"" + s.replace("\"", "\"\"") + "\"";
			}
		};
		compositeStringifier.add(String.class, stringStringifier);
		return stringStringifier;
	}
};
