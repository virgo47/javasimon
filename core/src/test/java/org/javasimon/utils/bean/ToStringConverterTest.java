package org.javasimon.utils.bean;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToStringConverterTest extends SimonUnitTest {

	private ToStringConverter toStringConverter = new ToStringConverter();

	@DataProvider(name = "stringConverter")
	public Object[][] stringConverterProvider() {
		return new Object[][]{
			{"str", "str"},
			{null, null}
		};
	}

	@Test(dataProvider = "stringConverter")
	public void testToStringConverter(String src, String expectedStr) {
		Assert.assertEquals(toStringConverter.convert(String.class, src), expectedStr);
	}
}
