package org.javasimon.console.action;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

/**
 * Unit test for {@link DetailHtmlAction}
 *
 * @author gerald
 */
public class DetailHtmlActionTest {

	private TestActionContext actionContext;
	private DetailHtmlAction action;

	@BeforeClass
	public static void beforeClass() {
		SimonData.initialize();
	}

	@BeforeMethod
	public void before() {
		actionContext = new TestActionContext(DetailHtmlAction.PATH);
		action = new DetailHtmlAction(actionContext);
	}

	private void assertContainsCell(String label, String value, String html) {
		// <td class="label">Name</td><td class="value" colspan="5">X</td>
		String regex = "<td class=\"label\">" + label + "</td><td class=\"value\"[^>]*>" + value + "</td>";
		assertTrue(Pattern.compile(regex).matcher(html).find(), regex + " found");
	}

	/** Test get Stopwatch detail in JSON format. */
	@Test
	public void testExecuteStopwatch() throws Exception {
		actionContext.setParameter("name", "A");
		action.readParameters();
		action.execute();
		assertEquals(actionContext.getContentType(), "text/html");
		String html = actionContext.toString();
		assertContainsCell("Name", "A", html);
		assertContainsCell("Min", "100", html);
		assertContainsCell("Max", "300", html);
		assertContainsCell("Mean", "200.000", html);
		assertContainsCell("Total", "600", html);
		assertContainsCell("Last", "300", html);
		assertContainsCell("Counter", "3", html);
		assertContainsCell("Standard Deviation", "100.000", html);
	}

	/** Test get Counter detail in JSON format. */
	@Test
	public void testExecuteCounter() throws Exception {
		actionContext.setParameter("name", "X");
		action.readParameters();
		action.execute();
		assertEquals(actionContext.getContentType(), "text/html");
		String html = actionContext.toString();
		assertContainsCell("Name", "X", html);
		assertContainsCell("Min", "1", html);
		assertContainsCell("Max", "4", html);
		assertContainsCell("Counter", "2", html);
	}
}
