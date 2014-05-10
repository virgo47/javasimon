package org.javasimon.console;

import org.javasimon.Sample;
import org.javasimon.Simon;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Simon type factory.
 *
 * @author gquintana
 */
public class SimonTypeFactory {

	/** Cache Class (either Simon or Sample descendants) &rarr; Simon Type. */
	private static final Map<Class, SimonType> SIMON_TYPE_CACHE = new HashMap<>();

	/** Interface of function to check if Simon type matches Simon or Simon Sample class. */
	private static interface SimonTypeMatcher<T> {
		boolean matches(Class<? extends T> type, SimonType simonType);
	}

	/** Function to check if Simon type matches Simon class. */
	private static final SimonTypeMatcher<Simon> TYPE_MATCHER = new SimonTypeMatcher<Simon>() {
		public boolean matches(Class<? extends Simon> type, SimonType simonType) {
			return simonType.getType().isAssignableFrom(type);
		}
	};

	/** Function to check if Simon type matches Simon Sample class. */
	private static final SimonTypeMatcher<Sample> SAMPLE_TYPE_MATCHER = new SimonTypeMatcher<Sample>() {
		public boolean matches(Class<? extends Sample> type, SimonType simonType) {
			return simonType.getSampleType().isAssignableFrom(type);
		}
	};

	/** Get Simon type corresponding to class. */
	private static <T> SimonType getValue(Class<? extends T> type, SimonTypeMatcher<T> typeMatcher) {
		SimonType simonType = SIMON_TYPE_CACHE.get(type);
		if (simonType == null) {
			for (SimonType lSimonType : SimonType.values()) {
				if (typeMatcher.matches(type, lSimonType)) {
					simonType = lSimonType;
					if (!Proxy.isProxyClass(type)) {
						SIMON_TYPE_CACHE.put(type, simonType);
					}
					break;
				}
			}
		}
		return simonType;
	}

	/**
	 * Get simon type from simon class.
	 *
	 * @param type Simon class
	 * @return Type
	 */
	public static SimonType getValueFromType(Class<? extends Simon> type) {
		return getValue(type, TYPE_MATCHER);
	}

	/**
	 * Get simon type from simon sample class.
	 *
	 * @param sampleType Sample class
	 * @return Type
	 */
	public static SimonType getValueFromSampleType(Class<? extends Sample> sampleType) {
		return getValue(sampleType, SAMPLE_TYPE_MATCHER);
	}

	/**
	 * Get simon type from simon instance.
	 *
	 * @param simon Simon
	 * @return Type
	 */
	public static SimonType getValueFromInstance(Simon simon) {
		return simon == null ? null : getValueFromType(simon.getClass());
	}

	/**
	 * Get simon type from simon sample instance.
	 *
	 * @param sample Simon sample
	 * @return Type
	 */
	public static SimonType getValueFromInstance(Sample sample) {
		return sample == null ? null : getValueFromSampleType(sample.getClass());
	}

	/**
	 * Get the main interface of the type.
	 *
	 * @param type Implementation class
	 * @return Main interface class
	 */
	public static Class normalizeType(Class type) {
		SimonType simonType = SimonTypeFactory.getValueFromType(type);
		Class normalizedType;
		if (simonType == null) {
			simonType = SimonTypeFactory.getValueFromSampleType(type);
			if (simonType == null) {
				normalizedType = type;
			} else {
				normalizedType = simonType.getSampleType();
			}
		} else {
			normalizedType = simonType.getType();
		}
		return normalizedType;
	}
}
