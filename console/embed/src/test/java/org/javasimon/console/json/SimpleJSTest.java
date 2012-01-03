package org.javasimon.console.json;
import org.javasimon.console.TimeFormatType;
import org.javasimon.console.ValueFormatter;
import static org.testng.Assert.*;
import org.testng.annotations.*;

/**
 * Unit test for {@link SimpleJS}
 * @author gquintana
 */
public class SimpleJSTest {
    private ValueFormatter valueFormatter=new JsonValueFormatter();
    @Test
    public void testLong() {
        SimpleJS simpleJS=SimpleJS.createNumber(12345L,valueFormatter);
        String json=simpleJS.toString();
        assertEquals(json,"12345");
    }
    @Test
    public void testTime() {
        valueFormatter.setTimeFormat(TimeFormatType.MILLISECOND);
        SimpleJS simpleJS=SimpleJS.createTime(123456789L,valueFormatter);
        String json=simpleJS.toString();
        assertEquals(json,"123");
        valueFormatter.setTimeFormat(TimeFormatType.MICROSECOND);
        json=simpleJS.toString();
        assertEquals(json,"123456");
        valueFormatter.setTimeFormat(TimeFormatType.NANOSECOND);
        json=simpleJS.toString();
        assertEquals(json,"123456789");
        valueFormatter.setTimeFormat(TimeFormatType.PRESENT);
        json=simpleJS.toString();
        assertEquals(json,"\"123 ms\"");
    }
    @Test
    public void testString() {
        SimpleJS simpleJS=SimpleJS.createString("Hello world! \"\\/",valueFormatter);
        String json=simpleJS.toString();
        assertEquals(json,"\"Hello world! \\\"\\\\\\/\"");
    }
}
