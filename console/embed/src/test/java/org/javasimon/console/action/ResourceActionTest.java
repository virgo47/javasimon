package org.javasimon.console.action;

import org.javasimon.console.TestActionContext;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Unit test for {@link TableCsvAction}
 *
 * @author gquintana
 */
public class ResourceActionTest {

	@Test
	public void testExecute() throws Exception {
		TestActionContext context = new TestActionContext("/resource/index.html");
		ResourceAction action = new ResourceAction(context,"/index.html");
		action.execute();
		assertEquals(context.getContentType(), "text/html");
		assertTrue(context.toByteArray().length > 128);
	}
}
