package org.javasimon.utils.bean;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToBooleanConverterTest extends SimonUnitTest {

	private ToBooleanConverter toBooleanConverter = new ToBooleanConverter();

	@DataProvider(name = "validValues")
	public Object[][] validValues() {
		return new Object[][]{
			{"true", true},
			{"false", false},
			{"True", true},
			{"False", false}
		};
	}

	@Test(dataProvider = "validValues")
	public void testConvertingValidValues(String from, Boolean expected) {
		Assert.assertEquals(toBooleanConverter.convert(Boolean.class, from), expected);
	}

	@DataProvider(name = "invalidValues")
	public Object[][] invalidValues() {
		return new Object[][]{
			{"12345"}
		};
	}

	@Test(dataProvider = "invalidValues", expectedExceptions = {ConvertException.class})
	public void testConvertingValidValues(String invalidVal) {
		toBooleanConverter.convert(Boolean.class, invalidVal);
	}
}
