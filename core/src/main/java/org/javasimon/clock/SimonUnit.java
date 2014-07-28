package org.javasimon.clock;

/**
 * Units typically used in Java Simon with amount of nanoseconds they contain.
 */
public enum SimonUnit {

	NANOSECOND("ns", 1),
	MICROSECOND("μs", 1000),
	MILLISECOND("ms", 1000_000),
	SECOND("s", 1000_000_000);

	private String symbol;
	private int divisor;

	SimonUnit(String symbol, int divisor) {
		this.symbol = symbol;
		this.divisor = divisor;
	}

	/**
	 * Returns the symbol of time unit.
	 *
	 * @return symbol of time unit
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Returns number of nanoseconds in this unit.
	 *
	 * @return number of nanoseconds in this unit
	 */
	public int getDivisor() {
		return divisor;
	}
}
