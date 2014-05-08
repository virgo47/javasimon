package org.javasimon.console.action;

import static org.testng.Assert.assertEquals;

import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link DetailJsonAction}
 *
 * @author gerald
 */
public class DetailJsonActionTest {

	private TestActionContext actionContext;
	private DetailJsonAction action;

	@BeforeClass
	public static void beforeClass() {
		SimonData.initialize();
	}

	@BeforeMethod
	public void before() {
		actionContext = new TestActionContext(DetailJsonAction.PATH);
		action = new DetailJsonAction(actionContext);
	}

	/** Test get Stopwatch detail in JSON format. */
	@Test
	public void testExecuteStopwatch() throws Exception {
		actionContext.setParameter("name", "A");
		action.readParameters();
		action.execute();
		assertEquals(actionContext.getContentType(), "application/json");
		JSONObject jsonObject = new JSONObject(actionContext.toString());
		assertEquals(jsonObject.getString("name"), "A");
		assertEquals(jsonObject.getLong("min"), 100L);
		assertEquals(jsonObject.getLong("max"), 300L);
		assertEquals(jsonObject.getLong("mean"), 200L);
		assertEquals(jsonObject.getLong("total"), 600L);
		assertEquals(jsonObject.getLong("last"), 300L);
		assertEquals(jsonObject.getLong("mean"), 200L);
		assertEquals(jsonObject.getLong("counter"), 3L);
		assertEquals(jsonObject.getString("type"), "STOPWATCH");
		assertEquals(jsonObject.getLong("standardDeviation"), 100L);
	}

	/** Test get Counter detail in JSON format. */
	@Test
	public void testExecuteCounter() throws Exception {
		actionContext.setParameter("name", "X");
		action.readParameters();
		action.execute();
		assertEquals(actionContext.getContentType(), "application/json");
		JSONObject jsonObject = new JSONObject(actionContext.toString());
		assertEquals(jsonObject.getString("name"), "X");
		assertEquals(jsonObject.getLong("min"), 1L);
		assertEquals(jsonObject.getLong("max"), 4L);
		assertEquals(jsonObject.getLong("counter"), 2L);
	}
}
