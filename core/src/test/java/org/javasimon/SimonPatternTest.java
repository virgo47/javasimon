package org.javasimon;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link SimonPattern}.
 *
 * @author virgo47@gmail.com
 */
public final class SimonPatternTest extends SimonUnitTest {

	@Test(expectedExceptions = SimonException.class, expectedExceptionsMessageRegExp = "Invalid Simon pattern: ")
	public void testEmpty() {
		SimonPattern.create("");
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

	@Test(expectedExceptions = SimonException.class, expectedExceptionsMessageRegExp = "Invalid Simon pattern: \\*\\*")
	public void testDoubleWildcard() {
		SimonPattern.create("**");
	}

	@Test(expectedExceptions = SimonException.class, expectedExceptionsMessageRegExp = "Invalid Simon pattern: \\*//")
	public void testStartWildcardIllegalName() {
		SimonPattern.create("*//");
	}

	@Test(expectedExceptions = SimonException.class, expectedExceptionsMessageRegExp = "Invalid Simon pattern: //\\*")
	public void testEndWildcardIllegalName() {
		SimonPattern.create("//*");
	}

	@Test(expectedExceptions = SimonException.class, expectedExceptionsMessageRegExp = "Invalid Simon pattern: \\*//\\*")
	public void testDoubleWildcardInvalidName() {
		SimonPattern.create("*//*");
	}

	@Test
	public void testStopwatchPattern() {
		SimonPattern pattern = SimonPattern.createForStopwatch("start*");

		Assert.assertTrue(pattern.accept(stopwatch("start.end")));
		Assert.assertTrue(pattern.accept(stopwatch("start.e")));

		Assert.assertFalse(pattern.accept(stopwatch("end")));
		Assert.assertFalse(pattern.accept(counter("start.end")));
		Assert.assertFalse(pattern.accept(counter("end")));
	}

	private Counter counter(String name) {
		Counter counter = mock(Counter.class);
		when(counter.getName()).thenReturn(name);
		return counter;
	}

	private Stopwatch stopwatch(String name) {
		Stopwatch stopwatch = mock(Stopwatch.class);
		when(stopwatch.getName()).thenReturn(name);
		return stopwatch;
	}

	@Test
	public void testStopwatchNullPattern() {
		SimonPattern pattern = SimonPattern.createForStopwatch(null);

		Assert.assertTrue(pattern.accept(stopwatch("start.end")));
		Assert.assertTrue(pattern.accept(stopwatch("start.e")));
		Assert.assertTrue(pattern.accept(stopwatch("end")));

		Assert.assertFalse(pattern.accept(counter("start.end")));
		Assert.assertFalse(pattern.accept(counter("end")));
	}

	@Test
	public void testCounterPattern() {
		SimonPattern pattern = SimonPattern.createForCounter("start*");

		Assert.assertTrue(pattern.accept(counter("start.end")));
		Assert.assertTrue(pattern.accept(counter("start.e")));

		Assert.assertFalse(pattern.accept(counter("end")));
		Assert.assertFalse(pattern.accept(stopwatch("start.end")));
		Assert.assertFalse(pattern.accept(stopwatch("end")));
	}

	@Test
	public void testCounterNullPattern() {
		SimonPattern pattern = SimonPattern.createForCounter(null);

		Assert.assertTrue(pattern.accept(counter("start.end")));
		Assert.assertTrue(pattern.accept(counter("start.e")));
		Assert.assertTrue(pattern.accept(counter("end")));

		Assert.assertFalse(pattern.accept(stopwatch("start.end")));
		Assert.assertFalse(pattern.accept(stopwatch("end")));
	}

	@Test
	public void testSameSimonPatternAreEquals() {
		String pattern = "*abc*";
		SimonPattern pattern1 = SimonPattern.create(pattern);
		SimonPattern pattern2 = SimonPattern.create(pattern);
		Assert.assertEquals(pattern1, pattern2);
	}

	@Test
	public void testDifferentSimonPatternAreNotEquals() {
		SimonPattern pattern1 = SimonPattern.create("*abc*");
		SimonPattern pattern2 = SimonPattern.create("*cba*");
		Assert.assertNotEquals(pattern1, pattern2);
	}

	@Test
	public void testSameStopwatchPatternAreEquals() {
		String pattern = "*abc*";
		SimonPattern pattern1 = SimonPattern.createForStopwatch(pattern);
		SimonPattern pattern2 = SimonPattern.createForStopwatch(pattern);
		Assert.assertEquals(pattern1, pattern2);
	}

	@Test
	public void testDifferentTypePatternAreNotEquals() {
		String pattern = "*abc*";
		SimonPattern pattern1 = SimonPattern.createForStopwatch(pattern);
		SimonPattern pattern2 = SimonPattern.createForCounter(pattern);
		Assert.assertNotEquals(pattern1, pattern2);
	}

}
