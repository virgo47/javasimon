import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.text.MessageFormat;

public class SimpleIcuApproach {
	public static void main(String[] args) {
		showDemo("transaction", Locale.getDefault());
		showDemo("client", Locale.getDefault());
		showDemo("transaction", Locale.forLanguageTag("sk"));
		showDemo("client", Locale.forLanguageTag("sk"));
	}

	private static void showDemo(String domainObject, Locale locale) {
		System.out.println("\nLOCALE: " + locale);
		print(locale, domainObject + ".saved", Collections.emptyMap());
		print(locale, domainObject + ".saved.alt", Collections.emptyMap());
		for (Integer count : Arrays.asList(0, 1, 2, 4, 5, 99)) {
			print(locale, domainObject + ".deleted", Collections.singletonMap("count", count));
		}
	}

	private static void print(Locale locale, String messageKey, Map args) {
		String message = format(locale, messageKey, args);
		System.out.println(messageKey + args + ": " + message);
	}

	private static String format(Locale locale, String key, Map args) {
		ResourceBundle bundle = ResourceBundle.getBundle("SimpleIcu", locale);
		try {
			String pattern = bundle.getString(key);
			return new MessageFormat(pattern, locale)
				.format(args);
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
