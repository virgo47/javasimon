package org.javasimon;

/**
 * Enumerates available types of {@link org.javasimon.StatProcessor}. Instances of this enumeration
 * are also factories to create the new StatProcesors instance of that type.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public enum StatProcessorType {
	/**
	 * Null stat processor computes nothing and is incredibly fast. :-)
	 */
	NULL,
	/**
	 * Basic stat processor computes some basic statistics for measured values.
	 */
	BASIC;

	/**
	 * Factory method produces instances of the stat processor of the specific type.
	 *
	 * @return new instance of the stat processor of the specific type
	 */
	public StatProcessor create() {
		switch (this) {
			case BASIC:
				return new BasicStatProcessor();
			default:
				return NullStatProcessor.INSTANCE;
		}
	}
}
