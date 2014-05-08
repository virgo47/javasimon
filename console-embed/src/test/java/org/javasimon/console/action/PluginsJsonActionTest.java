package org.javasimon.console.action;

import static org.testng.Assert.assertEquals;

import org.javasimon.console.TestActionContext;
import org.javasimon.console.plugin.DummyDetailPlugin;
import org.javasimon.console.plugin.DummyOtherPlugin;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Unit test for {@link PluginsJsonAction}
 *
 * @author gerald
 */
public class PluginsJsonActionTest {
	private PluginsJsonAction action;
	private TestActionContext actionContext;

	@BeforeMethod
	public void before() {
		actionContext = new TestActionContext(PluginsJsonAction.PATH);
		actionContext.getPluginManager().addPlugin(DummyDetailPlugin.class);
		actionContext.getPluginManager().addPlugin(DummyOtherPlugin.class);
		action = new PluginsJsonAction(actionContext);
	}

	/** Test get plugin list in JSON format without filtering. */
	@Test
	public void testExecuteAll() throws Exception {
		action.readParameters();
		action.execute();
		assertEquals(actionContext.getContentType(), "application/json");
		JSONArray jsonArray = new JSONArray(actionContext.toString());
		assertEquals(jsonArray.length(), 2);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		assertEquals(jsonObject.getString("id"), "dummy");
		jsonObject = jsonArray.getJSONObject(1);
		assertEquals(jsonObject.getString("id"), "other");
		jsonArray = jsonObject.getJSONArray("resources");
		assertEquals(jsonArray.length(), 2);
		jsonObject = jsonArray.getJSONObject(0);
		assertEquals(jsonObject.getString("path"), "js/other.js");
		assertEquals(jsonObject.getString("type"), "JS");
		jsonObject = jsonArray.getJSONObject(1);
		assertEquals(jsonObject.getString("path"), "js/other.css");
		assertEquals(jsonObject.getString("type"), "CSS");
	}

	/** Test get plugin list in JSON format filtering on plugin type. */
	@Test
	public void testExecuteByType() throws Exception {
		actionContext.setParameter("type", DetailPlugin.class.getName());
		action.readParameters();
		action.execute();
		assertEquals(actionContext.getContentType(), "application/json");
		JSONArray jsonArray = new JSONArray(actionContext.toString());
		assertEquals(jsonArray.length(), 1);
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		assertEquals(jsonObject.getString("id"), "dummy");
	}
}
