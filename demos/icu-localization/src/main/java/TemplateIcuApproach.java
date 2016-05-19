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
		Locale defLocale = Locale.getDefault();
		Locale sk = Locale.forLanguageTag("sk");

		showDemo("transaction", defLocale);
		showDemo("client", defLocale);
		showDemo("transaction", sk);
		showDemo("client", sk);

		System.out.println("\nMulti-object insertion:");
		for (Locale loc : Arrays.asList(defLocale, sk)) {
			ResourceBundle bundle = ResourceBundle.getBundle("TemplateIcu", loc);
			print(bundle, "object.delete.constraint.warning",
				Collections.singletonMap("name", "SuperCo."),
				new DomainObject("client", "master"),
				new DomainObject("transaction", "slave"));
		}
	}

	private static void showDemo(String domainObject, Locale locale) {
		ResourceBundle bundle = ResourceBundle.getBundle("TemplateIcu", locale);
		System.out.println("\nLOCALE: " + locale);
		print(bundle, "object.saved", Collections.emptyMap(),
			new DomainObject(domainObject));
		print(bundle, "object.saved.alt", Collections.emptyMap(),
			new DomainObject(domainObject));
		for (Integer count : Arrays.asList(0, 1, 2, 4, 5, 99)) {
			print(bundle, "object.deleted", Collections.singletonMap("count", count),
				new DomainObject(domainObject));
		}
	}

	private static void print(
		ResourceBundle bundle, String messageKey, Map args, DomainObject... domainObjects)
	{
		// not generified, sorry
		Map finalArgs = new HashMap(args);
		for (DomainObject domainObject : domainObjects) {
			finalArgs.putAll(domainObject.parseObjectInfo(bundle));
		}

		String message = format(bundle, messageKey, finalArgs);
		System.out.println(messageKey + args + ": " + message);
	}

	private static String format(ResourceBundle bundle, String key, Map args) {
		try {
			String pattern = bundle.getString(key);
			return new MessageFormat(pattern, bundle.getLocale())
				.format(args);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		} catch (MissingResourceException e) {
			return "";
		}
	}

	static class DomainObject {
		private final String domainObject;
		private final String role;

		DomainObject(String domainObject, String role) {
			this.domainObject = domainObject;
			this.role = role;
		}

		DomainObject(String domainObject) {
			this.domainObject = domainObject;
			this.role = null;
		}

		// no sanity checking here, but there should be some
		private Map parseObjectInfo(ResourceBundle bundle) {
			String objectInfoString = bundle.getString("domain.object." + domainObject);
			Map map = new HashMap();
			for (String form : objectInfoString.split("\\s*,\\s*")) {
				String[] sa = form.split("\\s*:\\s*");
				String key = role != null ? role + '_' + sa[0] : sa[0];
				map.put(key, sa[1]);
			}
			return map;
		}
	}
}
