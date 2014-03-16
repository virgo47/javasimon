package org.javasimon.utils.bean;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToDoubleConverterTest extends SimonUnitTest {

	private ToDoubleConverter toDoubleConverter = new ToDoubleConverter();

	@DataProvider(name = "validValues")
	public Object[][] validValues() {
		return new Object[][]{
			{"123.456", 123.456},
			{"12", 12.0}
		};
	}

	@Test(dataProvider = "validValues")
	public void testConvertValidValues(String from, Double expectedDouble) {
		Assert.assertEquals(toDoubleConverter.convert(Double.class, from), expectedDouble, 0.00001);
	}

	@Test
	public void testNullConvertedToNull() {
		Assert.assertEquals(toDoubleConverter.convert(Double.class, null), null);
	}

	@DataProvider(name = "invalidValues")
	public Object[][] invalidValues() {
		return new Object[][]{
			{"str"}
		};
	}

	@Test(dataProvider = "invalidValues", expectedExceptions = {ConvertException.class})
	public void testConvertInvalidValues(String invalidVal) {
		toDoubleConverter.convert(Double.class, invalidVal);
	}
}
