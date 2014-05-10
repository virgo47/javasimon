package org.javasimon.callback.lastsplits;

import org.javasimon.SimonUnitTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * @author gquintana
 */
public class CircularListTest extends SimonUnitTest {

	@Test
	public void testAddAndSize() {
		CircularList<String> list = new CircularList<>(5);
		assertEquals(0, list.size());
		list.add("A");
		assertEquals(1, list.size());
		list.add("B");
		assertEquals(2, list.size());
		list.add("C");
		assertEquals(3, list.size());
		list.add("D");
		assertEquals(4, list.size());
		list.add("E");
		assertEquals(5, list.size());
		list.add("F");
		assertEquals(5, list.size());
		list.add("G");
		assertEquals(5, list.size());
	}

	private String toString(List<?> elements) {
		StringBuilder stringBuilder = new StringBuilder();
		boolean first = true;
		for (Object element : elements) {
			if (first) {
				first = false;
			} else {
				stringBuilder.append(",");
			}
			stringBuilder.append(element.toString());
		}
		return stringBuilder.toString();
	}

	@Test
	public void testIterator() {
		CircularList<String> list = new CircularList<>(5);
		assertEquals("", toString(list));
		list.addAll(Arrays.asList("A"));
		assertEquals("A", toString(list));
		list.addAll(Arrays.asList("B", "C"));
		assertEquals("A,B,C", toString(list));
		list.addAll(Arrays.asList("D", "E"));
		assertEquals("A,B,C,D,E", toString(list));
		list.addAll(Arrays.asList("F", "G", "H"));
		assertEquals("D,E,F,G,H", toString(list));
		list.addAll(Arrays.asList("I", "J"));
		assertEquals("F,G,H,I,J", toString(list));
	}

	private void assertArrayEquals(String[] expected, Object[] actual) {
		assertEquals(actual.length, expected.length, "Element count");
		for (int i = 0; i < expected.length; i++) {
			assertEquals(actual[i], expected[i], "Element #" + i);
		}
	}

	@Test
	public void testToArray() {
		CircularList<String> list = new CircularList<>(5);
		assertArrayEquals(new String[0], list.toArray());
		list.addAll(Arrays.asList("A", "B", "C"));
		assertArrayEquals(new String[]{"A", "B", "C"}, list.toArray());
		list.addAll(Arrays.asList("D", "E"));
		assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, list.toArray());
		list.addAll(Arrays.asList("F", "G"));
		assertArrayEquals(new String[]{"C", "D", "E", "F", "G"}, list.toArray());
	}
}
