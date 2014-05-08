package org.javasimon.console.action;

import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javasimon.console.ActionException;
import org.javasimon.console.TestActionContext;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;

/**
 * Unit test for {@link ResourceAction}
 *
 * @author gquintana
 */
public class ResourceActionTest {

	@Test
	public void testExecute() throws Exception {
		TestActionContext context = new TestActionContext("/resource/index.html");
		ResourceAction action = new ResourceAction(context, "/index.html");
		action.execute();
		assertEquals(context.getContentType(), "text/html");
		assertTrue(context.toByteArray().length > 128);
	}

	@Test(expectedExceptions = ActionException.class)
	public void test4xxErrorWhenResourceDoesNotExists() throws Exception {
		TestActionContext context = new TestActionContext("/resource/non/existing");

		try {
			ResourceAction action = new ResourceAction(context, "/non/existing");
			action.execute();
		} finally {
			HttpServletResponse response = context.getResponse();
			verify(response).sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
