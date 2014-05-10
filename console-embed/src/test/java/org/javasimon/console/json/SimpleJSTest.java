package org.javasimon.console.json;

import org.javasimon.console.TimeFormatType;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Unit test for {@link SimpleJS}.
 *
 * @author gquintana
 */
public class SimpleJSTest {
	private JsonStringifierFactory stringifierFactory = createStringifierFactory(TimeFormatType.MILLISECOND);

	private JsonStringifierFactory createStringifierFactory(TimeFormatType timeFormat) {
		stringifierFactory = new JsonStringifierFactory();
		stringifierFactory.init(timeFormat, JsonStringifierFactory.READABLE_DATE_PATTERN, JsonStringifierFactory.READABLE_NUMBER_PATTERN);
		return stringifierFactory;
	}

	@Test
	public void testLong() {
		SimpleJS<Long> simpleJS = new SimpleJS<>(12345L, stringifierFactory.getStringifier(Long.class));
		String json = simpleJS.toString();
		assertEquals(json, "12345");
	}

	private void testTime(long inputTime, TimeFormatType timeFormat, String outputTime) {
		stringifierFactory = createStringifierFactory(timeFormat);
		SimpleJS<Long> simpleJS = new SimpleJS<>(inputTime, stringifierFactory.getStringifier(Long.class, JsonStringifierFactory.TIME_SUBTYPE));
		String json = simpleJS.toString();
		assertEquals(json, outputTime);
	}

	@Test
	public void testTime() {
		testTime(123456789L, TimeFormatType.MILLISECOND, "123");
		testTime(123456789L, TimeFormatType.MICROSECOND, "123456");
		testTime(123456789L, TimeFormatType.NANOSECOND, "123456789");
		testTime(123456789L, TimeFormatType.AUTO, "\"123 ms\"");
	}

	@Test
	public void testString() {
		SimpleJS<String> simpleJS = new SimpleJS<>("Hello world! \"\\/", stringifierFactory.getStringifier(String.class));
		String json = simpleJS.toString();
		assertEquals(json, "\"Hello world! \\\"\\\\\\/\"");
	}
}
