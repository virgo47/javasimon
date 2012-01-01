package org.javasimon.console.action;

//import org.javasimon.console.TestActionContext;
import java.util.HashSet;
import java.util.Set;
import org.javasimon.console.SimonData;
import org.javasimon.console.TestActionContext;
import org.json.*;
import org.testng.annotations.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Unit test for {@link TableJsonAction}
 * @author gquintana
 */
public class TableJsonActionTest {
    @BeforeClass
    public static void setUpClass() {
        SimonData.initialize();
    }
    @Test
    public void testExecute() throws Exception {
        TestActionContext context=new TestActionContext("/data/uglytable.json");
        TableJsonAction action=new TableJsonAction(context);
        action.execute();
        assertEquals(context.getContentType(),"application/json");
        String json=context.toString();
        assertTrue(json.contains("{\"name\":\"A\",\"type\":\"STOPWATCH\",\"counter\":3,\"total\":600,\"min\":100,\"mean\":200,\"last\":300,\"max\":300,\"standardDeviation\":81"));
        assertTrue(json.contains("{\"name\":\"B\",\"type\":\"STOPWATCH\",\"counter\":2,\"total\":300,\"min\":100,\"mean\":150,\"last\":100,\"max\":200,\"standardDeviation\":50"));
        assertTrue(json.contains("{\"name\":\"C\",\"type\":\"STOPWATCH\",\"counter\":1,\"total\":300,\"min\":300,\"mean\":300,\"last\":300,\"max\":300,\"standardDeviation\":0"));
        // Test JSON format with an external library
        JSONTokener jsonTokener=new JSONTokener(json);
        Set<String> names=new HashSet<String>();
        names.add("A"); names.add("B"); names.add("C");
        Object object=jsonTokener.nextValue();
        if (object instanceof JSONArray) {
            JSONArray jsonArray=(JSONArray) object;
            for (int i = 0; i < jsonArray.length(); i++) {
                object=jsonArray.get(i);
                if (object instanceof JSONObject) {
                    JSONObject jsonObject=(JSONObject) object;
                    String name=jsonObject.getString("name");
                    names.remove(name);
                }
            }
        }
        assertTrue(names.isEmpty());
    }
}
