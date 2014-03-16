package org.javasimon.utils.bean;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToLongConverterTest extends SimonUnitTest {

	private ToLongConverter toLongConverter = new ToLongConverter();

	@DataProvider(name = "toLongConverter")
	public Object[][] toLongConverter() {
		return new Object[][]{
			{"0", 0L},
			{"123", 123L},
			{null, null}
		};
	}

	@Test(dataProvider = "toLongConverter")
	public void testToIntegerConverter(String from, Long expectedLong) throws Exception {
		Assert.assertEquals(toLongConverter.convert(Long.class, from), expectedLong);
	}

	@DataProvider(name = "unconvertableValues")
	public Object[][] unconvertableValues() {
		return new Object[][]{
			{"someStr"},
			{"123?456"}
		};
	}

	@Test(dataProvider = "unconvertableValues", expectedExceptions = {ConvertException.class})
	public void testUnconvertableValues(String val) throws ConvertException {
		toLongConverter.convert(Long.class, val);
	}
}
