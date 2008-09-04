package org.javasimon;

/**
 * Enumerates available types of {@link org.javasimon.StatProcessor}. Instances of this enumeration
 * are also factories to create the new StatProcesors instance of that type.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public enum StatProcessorType {
	NULL,
	BASIC;

	public StatProcessor create() {
		switch (this) {
			case BASIC:
				return new BasicStatProcessor();
			default:
				return NullStatProcessor.INSTANCE;
		}
	}
}
