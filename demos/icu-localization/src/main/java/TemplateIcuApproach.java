import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.ibm.icu.text.MessageFormat;

public class TemplateIcuApproach {
	public static void main(String[] args) {
		showDemo("transaction", Locale.getDefault());
		showDemo("client", Locale.getDefault());
		showDemo("transaction", Locale.forLanguageTag("sk"));
		showDemo("client", Locale.forLanguageTag("sk"));
	}

	private static void showDemo(String domainObject, Locale locale) {
		System.out.println("\nLOCALE: " + locale);
		print(locale, domainObject, "object.saved", Collections.emptyMap());
		print(locale, domainObject, "object.saved.alt", Collections.emptyMap());
		for (Integer count : Arrays.asList(0, 1, 2, 4, 5, 99)) {
			print(locale, domainObject, "object.deleted", Collections.singletonMap("count", count));
		}
	}

	private static void print(Locale locale, String domainObject, String messageKey, Map args) {
		ResourceBundle bundle = ResourceBundle.getBundle("TemplateIcu", locale);
		Map objectInfo = parseObjectInfo(bundle.getString("domain.object." + domainObject));
		// not generified, sorry; we know that objectInfo is mutable, so we do it this way
		objectInfo.putAll(args);
		String message = format(bundle, locale, messageKey, objectInfo);
		// I decided to go with Nom/nom variants in the property file
//		if (sentenceRequiresCapitalization(message, true)) {
//			message = Character.toUpperCase(message.charAt(0)) + message.substring(1);
//		}
		System.out.println(messageKey + args + ": " + message);
	}

	private static boolean sentenceRequiresCapitalization(String message, boolean isSentence) {
		return isSentence && message != null && !(message.isEmpty())
			&& Character.isLowerCase(message.charAt(0));
	}

	// no sanity checking here, but there should be some
	private static Map parseObjectInfo(String objectInfoString) {
		Map map = new HashMap();
		for (String form : objectInfoString.split(" *, *")) {
			String[] sa = form.split(" *: *");
			map.put(sa[0], sa[1]);
		}
		return map;
	}

	private static String format(ResourceBundle bundle, Locale locale, String key, Map args) {
		try {
			String pattern = bundle.getString(key);
			return new MessageFormat(pattern, locale)
				.format(args);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
