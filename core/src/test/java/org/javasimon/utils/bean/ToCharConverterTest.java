package org.javasimon.utils.bean;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
public class ToCharConverterTest {

	private ToCharConverter toCharConverter = new ToCharConverter();

	@DataProvider(name = "validInput")
	public Object[][] validInputProvider() {
		return new Object[][] {
				{"a", 'a'},
				{"b", 'b'},
				{"\n", '\n'},
				{null, null}
		};
	}

	@Test(dataProvider = "validInput")
	public void testValidInputConversion(String input, Character expectedChar) {
		Character actualChar = toCharConverter.convert(Character.class, input);
		Assert.assertEquals(actualChar, expectedChar);
	}

	@DataProvider(name = "invalidInput")
	public Object[][] invalidInputProvider() {
		return new Object[][] {
				{"abc"},
				{"ab"},
				{""}
		};
	}

	@Test(dataProvider = "invalidInput", expectedExceptions = {ConvertException.class})
	public void testInvalidInputConversion(String input) {
		toCharConverter.convert(Character.class, input);
	}
}
