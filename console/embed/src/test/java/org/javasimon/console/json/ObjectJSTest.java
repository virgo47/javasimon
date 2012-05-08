package org.javasimon.console.json;

import org.javasimon.console.TimeFormatType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Unit test for {@link ObjectJS}
 *
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

	private JsonStringifierFactory stringifierFactory = new JsonStringifierFactory();

	@BeforeMethod
	public void setUp() {
		stringifierFactory.init(TimeFormatType.MILLISECOND, JsonStringifierFactory.READABLE_DATE_PATTERN, JsonStringifierFactory.READABLE_NUMBER_PATTERN);
	}

	@Test
	public void testCreate() {
		Foo foo = new Foo(123L, "Hello");
		ObjectJS fooJS = ObjectJS.create(foo, stringifierFactory);
		assertNotNull(fooJS.getAttribute("bar"));
		assertEquals(((SimpleJS) fooJS.getAttribute("bar")).getValue(), 123L);
		assertNotNull(fooJS.getAttribute("baz"));
		assertEquals(((SimpleJS) fooJS.getAttribute("baz")).getValue(), "Hello");
	}

	@Test
	public void testWrite() {
		Foo foo = new Foo(123L, "Hello");
		ObjectJS fooJS = ObjectJS.create(foo, stringifierFactory);
		String json = fooJS.toString();
		assertEquals(json, "{\"baz\":\"Hello\",\"bar\":123}");
	}
}
