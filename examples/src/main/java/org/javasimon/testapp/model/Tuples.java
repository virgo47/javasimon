package org.javasimon.testapp.model;

import java.util.Iterator;

/**
 * Tuple generator.
 *
 * @author Radovan Sninsky
 * @version $Revision: $ $Date: $
 * @since 2.0
 * @see Tuple
 */
public class Tuples implements Iterable<Tuple> {

	private static final int[][] params = {
		{279, 1009}, {2969, 10007}, {21395, 100003}, {2107, 1000003}, {211, 10000019}, {21, 100000007}
	};

	/** number of tuples in relation */
	private int count;
	private int prime;
	private int generator;


	public Tuples(int count) {
		this.count = count;

		/* Choose prime and generator values for the desired table tuplesCount */
		for (int i = 100000000, j = 5; i >= 1000; i/=10, j--) {
			if (count <= i) {
				generator = params[j][0];
				prime = params[j][1];
			}
		}
	}

	public Iterator<Tuple> iterator() {
		return new Itr();
	}

	private class Itr implements Iterator<Tuple> {

		int cursor = 0;
		int seed = generator;

		public boolean hasNext() {
			return cursor < count;
		}

		public Tuple next() {
			seed = rand(seed, count);
			int unique1 = seed - 1;
			int index = cursor++;

			Tuple t = new Tuple();
			t.setUnique1(unique1);
			t.setIdx(index);
			t.setOne(unique1 % 100);
			t.setTen(unique1 % 10);
			t.setTwenty(unique1 % 5);
			t.setTwentyFive(unique1 % 4);
			t.setFifty(unique1 % 2);
			t.setEvenOnePercent((unique1 % 100)*2);
			t.setOddOnePercent((unique1 % 100)*2+1);
			t.setStringU1(convertUnique(unique1));
			t.setStringU2(convertUnique(index));
			if (index % 4 == 0) {
				t.setString4("AAAA");
			} else if (index % 4 == 1) {
				t.setString4("HHHH");
			} else if (index % 4 == 2) {
				t.setString4("OOOO");
			} else if (index % 4 == 3) {
				t.setString4("VVVV");
			}

			return t;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private String convertUnique(int number) {
			char tmp[] = new char[7];

			/* first set result string to "AAAAAAA" */
			char result[] = "AAAAAAA".toCharArray();

			//for (int i=0 ; i<7 ; i++) {
			//	result[i] = 'A';
			//}

			int i = 6;
			int cnt = 0;
			int rem;
			/* convert unique value from right to left into an alphabetic string in tmp */
			/* tmp digits are right justified in tmp */
			while ( (number > 0) ) {
				rem = number % 26; /* '%' is the mod operator in C */
				tmp[i] = (char) ('A' + rem);
				number = number / 26;
				i--; cnt++;
			}

			/* finally move tmp into result, left justifying it */
			for (int j=0; j < cnt; j++) {
				result[j] = tmp[++i];
			}

			return new String(result);
		}

		/**
		 * Generate a unique random number between 1 and limit.
		 * @param seed just seed
		 * @param limit max value for interval
		 * @return new random value
		 */
		private int rand(int seed, int limit) {
			do {
				seed = (generator * seed) % prime;
			} while (seed > limit);

			return seed;
		}
	}
}
