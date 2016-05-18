import java.text.Format;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ibm.icu.text.MessageFormat;

public class TemplateIcuFormatIntrospection {
	public static void main(String[] args) {
		String pattern = "{gend, select," +
			"  mas {{count,plural," +
			"    =0 {Žiadny {nom} nebol zmazaný}" +
			"    one {Jeden {nom} bol zmazaný}" +
			"    few {{count} {pl} boli zmazaní}" +
			"    other {{count} {plgen} bolo zmazaných}}} " +
			"  fem {{count,plural," +
			"    =0 {Žiadna {nom} nebola zmazaná}" +
			"    one {Jedna {nom} bola zmazaná}" +
			"    few {{count} {pl} boli zmazané}" +
			"    other {{count} {plgen} bolo zmazaných}}}" +
			"  other {!!!}}.";

		System.out.println("pattern = " + pattern);

		MessageFormat msgFormat = new MessageFormat(pattern);
		for (String argName : msgFormat.getArgumentNames()) {
			Format format = msgFormat.getFormatByArgumentName(argName);
			System.out.println("argName: " + format);
		}
		Format format = msgFormat.getFormatByArgumentName("pl");
		System.out.println("pl: " + format);

		Map<String, String> formatMap = new HashMap<>();
		// regex finds only arguments requiring attention, Strings don't bother us
		// select actually also means String
		Pattern regex = Pattern.compile("\\{\\s*([^,{\\s]+)\\s*,\\s*([^,}]+)\\s*[,}]");
		Matcher matcher = regex.matcher(pattern);
		while (matcher.find()) {
			formatMap.put(matcher.group(1), matcher.group(2));
		}
		System.out.println("formatMap = " + formatMap);
	}
}
