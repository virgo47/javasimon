package org.javasimon;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests for {@link SimonPattern}.
 *
 * @author virgo47@gmail.com
 */
public final class SimonPatternTest {
	@Test
	public void testEmpty() {
		SimonPattern simonPattern = SimonPattern.create("");

		Assert.assertTrue(simonPattern.matches(Manager.ROOT_SIMON_NAME));

		Assert.assertFalse(simonPattern.matches("whatever"));
	}

	@Test
	public void testExact() {
		SimonPattern simonPattern = SimonPattern.create("exact.name");

		Assert.assertTrue(simonPattern.matches("exact.name"));

		Assert.assertFalse(simonPattern.matches("whatever"));
		Assert.assertFalse(simonPattern.matches(""));
	}

	@Test
	public void testSingleWildcard() {
		SimonPattern simonPattern = SimonPattern.create("*");

		Assert.assertTrue(simonPattern.matches("whatever"));
		Assert.assertTrue(simonPattern.matches("whatever.with.delimiter.too"));
		Assert.assertTrue(simonPattern.matches(""));
	}

	@Test
	public void testWildcardStart() {
		SimonPattern simonPattern = SimonPattern.create("*whatever");

		Assert.assertTrue(simonPattern.matches("whatever"));
		Assert.assertTrue(simonPattern.matches("something.and.then.whatever"));
		Assert.assertTrue(simonPattern.matches("something.and.then.bubuwhatever"));

		Assert.assertFalse(simonPattern.matches("whatever.and.something"));
		Assert.assertFalse(simonPattern.matches(""));
		Assert.assertFalse(simonPattern.matches("something.else"));
	}

	@Test
	public void testWildcardEnd() {
		SimonPattern simonPattern = SimonPattern.create("whatever*");

		Assert.assertTrue(simonPattern.matches("whatever"));
		Assert.assertTrue(simonPattern.matches("whatever.and.something"));
		Assert.assertTrue(simonPattern.matches("whateverbubu.and.something"));

		Assert.assertFalse(simonPattern.matches("something.and.then.whatever"));
		Assert.assertFalse(simonPattern.matches(""));
		Assert.assertFalse(simonPattern.matches("something.else"));
	}

	@Test
	public void testWildcardStartAndEnd() {
		SimonPattern simonPattern = SimonPattern.create("*whatever*");

		Assert.assertTrue(simonPattern.matches("whatever"));
		Assert.assertTrue(simonPattern.matches("whatever.and.something"));
		Assert.assertTrue(simonPattern.matches("whateverbubu.and.something"));
		Assert.assertTrue(simonPattern.matches("something.and.then.whatever"));
		Assert.assertTrue(simonPattern.matches("something.and.then.bubuwhatever"));
		Assert.assertTrue(simonPattern.matches("something.and.then.whatever.plus.end"));
		Assert.assertTrue(simonPattern.matches("something.and.then.bubuwhatever.and.end.again"));

		Assert.assertFalse(simonPattern.matches(""));
		Assert.assertFalse(simonPattern.matches("something.else"));
	}

	@Test
	public void testWildcardMiddle() {
		SimonPattern simonPattern = SimonPattern.create("start*end");

		Assert.assertTrue(simonPattern.matches("startbubu.something.bubuend"));
		Assert.assertTrue(simonPattern.matches("start.bubu.something.bubu.end"));
		Assert.assertTrue(simonPattern.matches("startend"));

		Assert.assertFalse(simonPattern.matches("late.start.with.end"));
		Assert.assertFalse(simonPattern.matches("late.start.with.end.premature"));
		Assert.assertFalse(simonPattern.matches(""));
		Assert.assertFalse(simonPattern.matches("something.else"));
	}
}
