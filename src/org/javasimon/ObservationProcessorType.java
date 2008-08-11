package org.javasimon;

/**
 * ObservationProcessorType.
*
* @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
* @created Aug 12, 2008
*/
public enum ObservationProcessorType {
	NULL,
	BASIC;

	ObservationProcessor create() {
		switch (this) {
			case BASIC:
				return new BasicObservationProcessor();
		}

		// default - null implementation
		return new NullObservationProcessor();
	}
}
