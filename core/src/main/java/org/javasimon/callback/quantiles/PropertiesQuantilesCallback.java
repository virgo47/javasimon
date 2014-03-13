package org.javasimon.callback.quantiles;

import org.javasimon.Simon;
import org.javasimon.Stopwatch;

import java.util.Properties;

/**
 * Callback which stores data in buckets to compute quantiles.
 * Buckets are created from configuration stored in a {@link java.util.Properties}
 * file. Configuration file should look like:
 * <pre>
 * # Global default values are set on root
 * .min=0
 * .max=60000
 * .nb=5
 * .type=LINEAR
 *
 * # For org.javasimon group: more buckets
 * org.javasimon.nb=10
 *
 * # SlowClass is a performance bottleneck: higher upper bound
 * org.javasimon.slow.SlowClass.max=300000
 *
 * # Can use Exponential buckets for some Stopwatches
 * org.javasimon.special.type=EXPONENTIAL
 * </pre>
 *
 * @author gquintana
 */
public class PropertiesQuantilesCallback extends QuantilesCallback {

	/** Properties containing configuration. */
	private final Properties properties;

	/**
	 * Main constructor.
	 *
	 * @param properties Properties containing configuration
	 */
	public PropertiesQuantilesCallback(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Create buckets using callback attributes.
	 *
	 * @param stopwatch Target stopwatch
	 * @return Created buckets for given stopwatch
	 */
	@Override
	protected Buckets createBuckets(Stopwatch stopwatch) {
		// Get configuration
		BucketsType type = bucketsTypeEnumPropertyType.get(stopwatch, "type");
		Long min = longPropertyType.get(stopwatch, "min");
		Long max = longPropertyType.get(stopwatch, "max");
		Integer nb = integerPropertyType.get(stopwatch, "nb");
		// Build buckets
		Buckets buckets = type.createBuckets(stopwatch, min, max, nb);
		buckets.setLogTemplate(createLogTemplate(stopwatch));
		return buckets;
	}

	/**
	 * Returns value of Simon property.
	 *
	 * @param simon Simon
	 * @param name Property name
	 * @return Raw property value
	 */
	private String getProperty(Simon simon, String name) {
		return properties.getProperty(simon.getName() + "." + name);
	}

	/** Remove space at both ends and convert empty strings to null. */
	private static String cleanString(String s) {
		if (s != null) {
			s = s.trim();
			if (s.equals("")) {
				s = null;
			}
		}
		return s;
	}

	/** Base class for property types. */
	private abstract class PropertyType<T> {
		public abstract T parse(String value);

		public T get(Simon simon, String name) {
			Simon currentSimon = simon;
			T result = null;
			while (result == null && currentSimon != null) {
				String s = getProperty(currentSimon, name);
				s = cleanString(s);
				if (s != null) {
					result = parse(s);
				}
				currentSimon = currentSimon.getParent();
			}
			return result;
		}
	}

	/** Returns long property for Simon. */
	private final PropertyType<Long> longPropertyType = new PropertyType<Long>() {
		@Override
		public Long parse(String s) {
			Long l;
			try {
				l = Long.valueOf(s);
			} catch (NumberFormatException numberFormatException) {
				l = null;
			}
			return l;
		}
	};

	/** Returns integer property for Simon. */
	private final PropertyType<Integer> integerPropertyType = new PropertyType<Integer>() {
		@Override
		public Integer parse(String s) {
			Integer l;
			try {
				l = Integer.valueOf(s);
			} catch (NumberFormatException numberFormatException) {
				l = null;
			}
			return l;
		}
	};

	/** Returns enum property for Simon. */
	private class EnumPropertyType<E extends Enum<E>> extends PropertyType<E> {
		private final Class<E> enumClass;

		private EnumPropertyType(Class<E> enumClass) {
			this.enumClass = enumClass;
		}

		@Override
		public E parse(String s) {
			E e;
			try {
				e = Enum.valueOf(enumClass, s.toUpperCase());
			} catch (IllegalArgumentException exc) {
				e = null;
			}
			return e;
		}
	}

	/** Returns bucket type property for Simon. */
	private final EnumPropertyType<BucketsType> bucketsTypeEnumPropertyType = new EnumPropertyType<BucketsType>(BucketsType.class) {
		@Override
		public BucketsType get(Simon simon, String name) {
			BucketsType type = super.get(simon, name);
			if (type == null) {
				type = BucketsType.LINEAR;
			}
			return type;
		}
	};
}
