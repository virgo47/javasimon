package org.javasimon.console.json;

import org.javasimon.console.text.BaseStringifier;
import org.javasimon.console.text.Stringifier;
import org.javasimon.console.text.StringifierFactory;

/**
 * Value formatter for JSON reponses
 * @author gquintana
 */
public class JsonStringifierFactory extends StringifierFactory {

	@Override
	protected Stringifier registerNullStringifier() {
		return registerNullStringifier("\"\"");
	}

	@Override
	protected Stringifier<String> registerStringStringifier(Stringifier nullStringifier) {
		Stringifier<String> stringStringifier=new BaseStringifier<String>(nullStringifier) {
			@Override
			protected String doToString(String s) {
				return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("/", "\\/") + "\"";
			}
		};
		compositeStringifier.add(String.class, stringStringifier);
		return stringStringifier;
	}
};
