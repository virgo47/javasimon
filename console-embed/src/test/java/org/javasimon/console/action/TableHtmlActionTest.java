package org.javasimon.console.action;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Unit test for {@link TableHtmlAction}.
 *
 * @author gquintana
 */
public class TableHtmlActionTest {

	@BeforeClass
	public static void setUpClass() {
		SimonData.initialize();
	}

	@Test
	public void testExecute() throws Exception {
		TestActionContext context = new TestActionContext("/data/table.html");
		TableHtmlAction action = new TableHtmlAction(context);
		action.readParameters();
		action.execute();
		assertEquals(context.getContentType(), "text/html");
		String html = context.toString();
		assertTrue(html.contains("<td class=\"name\">B</td><td class=\"type\"><img class=\"stopwatch icon\" src=\"../../resource/images/TypeStopwatch.png\" alt=\"stopwatch\" />STOPWATCH</td><td class=\"counter\">2</td><td class=\"total\">300</td><td class=\"min\">100</td><td class=\"mean\">150</td><td class=\"last\">100</td><td class=\"max\">200</td><td class=\"standardDeviation\">71</td>"));
		assertTrue(html.contains("<td class=\"name\">A</td><td class=\"type\"><img class=\"stopwatch icon\" src=\"../../resource/images/TypeStopwatch.png\" alt=\"stopwatch\" />STOPWATCH</td><td class=\"counter\">3</td><td class=\"total\">600</td><td class=\"min\">100</td><td class=\"mean\">200</td><td class=\"last\">300</td><td class=\"max\">300</td><td class=\"standardDeviation\">100</td>"));
		assertTrue(html.contains("<td class=\"name\">C</td><td class=\"type\"><img class=\"stopwatch icon\" src=\"../../resource/images/TypeStopwatch.png\" alt=\"stopwatch\" />STOPWATCH</td><td class=\"counter\">1</td><td class=\"total\">300</td><td class=\"min\">300</td><td class=\"mean\">300</td><td class=\"last\">300</td><td class=\"max\">300</td><td class=\"standardDeviation\">0</td>"));
	}

	@Test
	public void testExecuteTimeFormat() throws Exception {
		TestActionContext context = new TestActionContext("/data/table.html");
		TableHtmlAction action = new TableHtmlAction(context);
		context.setParameter("timeFormat", "AUTO");
		action.readParameters();
		action.execute();
		assertEquals(context.getContentType(), "text/html");
		String html = context.toString();
		assertTrue(html.contains("<td class=\"name\">B</td><td class=\"type\"><img class=\"stopwatch icon\" src=\"../../resource/images/TypeStopwatch.png\" alt=\"stopwatch\" />STOPWATCH</td><td class=\"counter\">2</td><td class=\"total\">300 ms</td><td class=\"min\">100 ms</td><td class=\"mean\">150 ms</td><td class=\"last\">100 ms</td><td class=\"max\">200 ms</td><td class=\"standardDeviation\">70.7 ms</td>"));
		assertTrue(html.contains("<td class=\"name\">A</td><td class=\"type\"><img class=\"stopwatch icon\" src=\"../../resource/images/TypeStopwatch.png\" alt=\"stopwatch\" />STOPWATCH</td><td class=\"counter\">3</td><td class=\"total\">600 ms</td><td class=\"min\">100 ms</td><td class=\"mean\">200 ms</td><td class=\"last\">300 ms</td><td class=\"max\">300 ms</td><td class=\"standardDeviation\">100 ms</td>"));
		assertTrue(html.contains("<td class=\"name\">C</td><td class=\"type\"><img class=\"stopwatch icon\" src=\"../../resource/images/TypeStopwatch.png\" alt=\"stopwatch\" />STOPWATCH</td><td class=\"counter\">1</td><td class=\"total\">300 ms</td><td class=\"min\">300 ms</td><td class=\"mean\">300 ms</td><td class=\"last\">300 ms</td><td class=\"max\">300 ms</td><td class=\"standardDeviation\">0</td>"));
	}
}
