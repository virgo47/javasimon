package org.javasimon.utils.bean;

import org.javasimon.SimonUnitTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToEnumCoverterTest extends SimonUnitTest {

	private ToEnumConverter enumCoverter = new ToEnumConverter();

	private static enum TestEnum {
		VAL1,
		VAL2,
		VAL3
	}

	@DataProvider(name = "enumConversion")
	Object[][] enumConversionDataProvider() {
		return new Object[][]{
			{"VAL1", TestEnum.VAL1},
			{"VAL2", TestEnum.VAL2},
			{"VAL3", TestEnum.VAL3},
		};
	}

	@Test(dataProvider = "enumConversion")
	public void testEnumConversion(String valStr, TestEnum enumVal) {
		Assert.assertEquals(enumCoverter.convert(TestEnum.class, valStr), enumVal);
	}

	@Test(expectedExceptions = ConvertException.class)
	public void testNonExistingEnumValConversion() {
		enumCoverter.convert(TestEnum.class, "SOME_NON_EXISTING_ENUM_VAL");
	}
}
