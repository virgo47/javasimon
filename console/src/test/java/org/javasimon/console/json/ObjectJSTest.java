package org.javasimon.console.json;
import org.javasimon.console.ValueFormatter;
import static org.testng.Assert.*;
import org.testng.*;
import org.testng.annotations.*;
/**
 * Unit test for {@link ObjectJS}
 * @author gquintana
 */
public class ObjectJSTest {
    class Foo {
        private long bar;
        private String baz;
        public Foo(long bar, String baz) {
            this.bar = bar;
            this.baz = baz;
        }
        public long getBar() {
            return bar;
        }
        public String getBaz() {
            return baz;
        }
        
    }
    private ValueFormatter valueFormatter=new JsonValueFormatter();
    @Test
    public void testCreate() {
        Foo foo=new Foo(123L,"Hello");
        ObjectJS fooJS=ObjectJS.create(foo, valueFormatter);
        assertNotNull(fooJS.getAttribute("bar"));
        assertEquals(((SimpleJS)fooJS.getAttribute("bar")).getValue(),123L);
        assertNotNull(fooJS.getAttribute("baz"));
        assertEquals(((SimpleJS)fooJS.getAttribute("baz")).getValue(),"Hello");
    }
    @Test
    public void testWrite() {
        Foo foo=new Foo(123L,"Hello");
        ObjectJS fooJS=ObjectJS.create(foo, valueFormatter);
        String json=fooJS.toString();
        assertEquals(json,"{\"bar\":123,\"baz\":\"Hello\"}");
    }
}
