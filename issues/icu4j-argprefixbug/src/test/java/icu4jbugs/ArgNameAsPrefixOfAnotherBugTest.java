package icu4jbugs;

import static org.assertj.core.api.Assertions.assertThat;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import org.junit.Test;

public class ArgNameAsPrefixOfAnotherBugTest {

	@Test
	public void argShouldNotIgnoreFormatIfPreviousArgIsItsPrefix() {
		String pattern = "{abc}, {abcd,number}, {abx,number}";
		MessageFormat messageFormat = new MessageFormat(pattern);
		assertThat(messageFormat.getFormatByArgumentName("abc")).isNull();
		assertThat(messageFormat.getFormatByArgumentName("abx"))
			.isNotNull()
			.isInstanceOf(NumberFormat.class); // this one is OK, not starting with "abc"
		assertThat(messageFormat.getFormatByArgumentName("abcd"))
			.isNotNull() // FAILS now, because format for "abc" is returned
			.isInstanceOf(NumberFormat.class);
	}

	// This one demonstrates it depends on the order, only prefix name before messes it up
	@Test
	public void argFormatIsOkIfPrefixArgIsAfterIt() {
		String pattern = "{abcd,number}, {abc}";
		MessageFormat messageFormat = new MessageFormat(pattern);
		assertThat(messageFormat.getFormatByArgumentName("abc")).isNull();
		assertThat(messageFormat.getFormatByArgumentName("abcd")).isNotNull();
	}

	@Test
	public void realisticScenarioThatLeadToDiscovery() {
		String pattern = "{date,date}, {datetime}";
		MessageFormat messageFormat = new MessageFormat(pattern);
		assertThat(messageFormat.getFormatByArgumentName("date"))
			.isNotNull()
			.isInstanceOf(SimpleDateFormat.class);
		assertThat(messageFormat.getFormatByArgumentName("datetime"))
			.isNull(); // we did not specify the type, but it fails (date implied incorrectly)
	}

	@Test
	public void identityOfFormats() {
		String pattern = "{a,date}, {b,date,yyyyMMdd}, {aa,date,yyyy}";
		MessageFormat messageFormat = new MessageFormat(pattern);
		assertThat(messageFormat.getFormatByArgumentName("a"))
			.isNotSameAs(messageFormat.getFormatByArgumentName("b"));
		// currently this picks the same object for a and aa, it works the same way when both are
		// the same 'date' format (OK), but in this case should definitely return different format
		assertThat(messageFormat.getFormatByArgumentName("a"))
			.isNotSameAs(messageFormat.getFormatByArgumentName("aa"));
	}
}
