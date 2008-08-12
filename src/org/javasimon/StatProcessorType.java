package org.javasimon;

/**
 * StatProcessorType.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 12, 2008
 */
public enum StatProcessorType {
	NULL,
	BASIC;

	StatProcessor create() {
		switch (this) {
			case BASIC:
				return new BasicStatProcessor();
			default:
				return NullStatProcessor.INSTANCE;
		}
	}
}
