package org.javasimon.console.action;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit test for {@link org.javasimon.console.action.TreeJsonAction}.
 *
 * @author gquintana
 */
public class TreeJsonActionTest {

	@BeforeClass
	public static void setUpClass() {
		SimonData.initialize();
	}

	private void visitJSONArray(JSONArray jsonArray, Set<String> names) throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			Object object = jsonArray.get(i);
			if (object instanceof JSONObject) {
				visitJSONObject((JSONObject) object, names);
			}
		}
	}

	private void visitJSONObject(JSONObject jsonObject, Set<String> names) throws JSONException {
		if (jsonObject.has("name")) {
			String name = jsonObject.getString("name");
			if (name != null) {
				names.remove(name);
			}
		}
		if (jsonObject.has("children")) {
			JSONArray jsonArray = jsonObject.getJSONArray("children");
			if (jsonArray != null) {
				visitJSONArray(jsonArray, names);
			}
		}
	}

	@Test
	public void testExecute() throws Exception {
		TestActionContext context = new TestActionContext("/data/tree.json");
		TreeJsonAction action = new TreeJsonAction(context);
		action.readParameters();
		action.execute();
		assertEquals(context.getContentType(), "application/json");
		String json = context.toString();
		// Test JSON format with an external library
		JSONTokener jsonTokener = new JSONTokener(json);
		Set<String> names = new HashSet<>();
		names.add("A");
		names.add("B");
		names.add("C");
		Object object = jsonTokener.nextValue();
		if (object instanceof JSONObject) {
			visitJSONObject((JSONObject) object, names);
		}
		assertTrue(names.isEmpty());
	}
}
