package org.javasimon.console.json;

import org.javasimon.console.TimeFormatType;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

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
	public void testDouble() {
		SimpleJS<Double> simpleJS = new SimpleJS<>(5e15d, stringifierFactory.getStringifier(Double.class));
		String json = simpleJS.toString();
		assertEquals(json, "5000000000000000.000");
	}

	@Test
	public void testDoubleNaN() {
		SimpleJS<Double> simpleJS = new SimpleJS<>(Double.NaN, stringifierFactory.getStringifier(Double.class));
		String json = simpleJS.toString();
		assertEquals(json, "\"\"");
	}

	@Test
	public void testLong() {
		SimpleJS<Long> simpleJS = new SimpleJS<>(12345L, stringifierFactory.getStringifier(Long.class));
		String json = simpleJS.toString();
		assertEquals(json, "12345");
	}

	/** @noinspection SameParameterValue*/
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
		SimpleJS<String> simpleJS = new SimpleJS<>("\"Hello, /\r\n world!\"", stringifierFactory.getStringifier(String.class));
		String json = simpleJS.toString();
		assertEquals(json, "\"\\\"Hello, \\/\\r\\n world!\\\"\"");
	}
}
