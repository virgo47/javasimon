package org.javasimon.console.action;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit test for {@link ListJsonAction}
 *
 * @author gquintana
 */
public class ListJsonActionTest {

	@BeforeClass
	public static void setUpClass() {
		SimonData.initialize();
	}

	@Test
	public void testExecute() throws Exception {
		TestActionContext context = new TestActionContext("/data/table.json");
		ListJsonAction action = new ListJsonAction(context);
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
		if (object instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) object;
			for (int i = 0; i < jsonArray.length(); i++) {
				object = jsonArray.get(i);
				if (object instanceof JSONObject) {
					JSONObject jsonObject = (JSONObject) object;
					String name = jsonObject.getString("name");
					names.remove(name);
				}
			}
		}
		assertTrue(names.isEmpty());
	}
}
