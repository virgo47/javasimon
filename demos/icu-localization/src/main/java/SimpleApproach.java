import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class SimpleApproach {
	public static void main(String[] args) {
		showDemo("transaction", Locale.getDefault());
		showDemo("client", Locale.getDefault());
		showDemo("transaction", Locale.forLanguageTag("sk"));
		showDemo("client", Locale.forLanguageTag("sk"));
	}

	private static void showDemo(String domainObject, Locale locale) {
		System.out.println("\nLOCALE: " + locale);
		print(locale, domainObject + ".saved");
		print(locale, domainObject + ".saved.alt");
		for (Integer count : Arrays.asList(0, 1, 2, 4, 5, 99)) {
			print(locale, domainObject + ".deleted", count);
		}
	}

	private static void print(Locale locale, String messageKey, Object... args) {
		String message = format(locale, messageKey, args);
		System.out.println(messageKey + Arrays.toString(args) + ": " + message);
	}

	private static String format(Locale locale, String key, Object... args) {
		ResourceBundle bundle = ResourceBundle.getBundle("Simple", locale);
		try {
			String pattern = bundle.getString(key);
			return new MessageFormat(pattern, locale)
				.format(args);
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
