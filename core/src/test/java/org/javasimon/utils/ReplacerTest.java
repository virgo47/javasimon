package org.javasimon.utils;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

public class ReplacerTest {

	@Test(timeOut = 100)
	public void nonRepeatingReplacerMakesSinglePass() {
		Replacer replacer = new Replacer("AA", "A");
		assertEquals(replacer.process("AAAA"), "AA");
	}

	@Test(timeOut = 100)
	public void repeatingReplacerReducesStringCompletely() {
		Replacer replacer = new Replacer("AA", "A", Replacer.Modificator.REPEAT_UNTIL_UNCHANGED);
		assertEquals(replacer.process("AAAA"), "A");
	}

	@Test(timeOut = 100)
	public void replacerIsCaseSensitiveByDefault() {
		Replacer replacer = new Replacer("AA", "A");
		assertEquals(replacer.process("aaaa"), "aaaa");
	}

	@Test(timeOut = 100)
	public void replacerIgnoresCaseWithModificator() {
		Replacer replacer = new Replacer("AA", "A", Replacer.Modificator.IGNORE_CASE);
		assertEquals(replacer.process("aaaa"), "AA");
	}
}