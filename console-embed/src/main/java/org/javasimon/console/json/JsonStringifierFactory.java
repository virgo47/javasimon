package org.javasimon.console.json;

import org.javasimon.console.text.BaseStringifier;
import org.javasimon.console.text.Stringifier;
import org.javasimon.console.text.StringifierFactory;

/**
 * Value formatter for JSON responses.
 *
 * @author gquintana
 * @author raipc
 */
public class JsonStringifierFactory extends StringifierFactory {

	@Override
	protected Stringifier registerNullStringifier() {
		return registerNullStringifier("\"\"");
	}

	@Override
	protected Stringifier<String> registerStringStringifier(Stringifier nullStringifier) {
		Stringifier<String> stringStringifier = new BaseStringifier<String>(nullStringifier) {
			@Override
			protected String doToString(String string) {
				if (string.isEmpty()) {
					return "\"\"";
				}
				final int len = string.length();
				final StringBuilder sb = new StringBuilder(len + 6).append('"');
				for (int i = 0; i < len; i++) {
					char c = string.charAt(i);
					switch (c) {
						case '\\':
						case '"':
							sb.append('\\');
							sb.append(c);
							break;
						case '/':
							sb.append("\\/");
							break;
						case '\b':
							sb.append("\\b");
							break;
						case '\t':
							sb.append("\\t");
							break;
						case '\n':
							sb.append("\\n");
							break;
						case '\f':
							sb.append("\\f");
							break;
						case '\r':
							sb.append("\\r");
							break;
						default:
							if (c < ' ') {
								sb.append("\\u");
								String hexString = Integer.toHexString(c);
								for (int j = hexString.length(); j < 4; j++) {
									sb.append('0');
								}
								sb.append(hexString);
							} else {
								sb.append(c);
							}
					}
				}
				return sb.append('"').toString();
			}
		};
		compositeStringifier.add(String.class, stringStringifier);
		return stringStringifier;
	}
}
