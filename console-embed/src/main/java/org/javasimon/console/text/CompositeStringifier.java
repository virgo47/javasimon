package org.javasimon.console.text;

import java.util.HashMap;
import java.util.Map;

/**
 * Dictionary (Class type,String subType)&rarr;Stringifier.
 *
 * @author gquintana
 */
public class CompositeStringifier implements Stringifier<Object> {
	/**
	 * Key of the dictionary is the couple (Class type,String subType).
	 * subType can be null
	 */
	private static final class StringifierKey {

		private final Class<?> type;
		private final String subType;

		/**
		 * Constructor
		 */
		public StringifierKey(Class<?> type, String subType) {
			this.type = type;
			this.subType = subType;
		}

		/**
		 * Compares 2 objects including the case one of them is null.
		 */
		private boolean equalsTo(Object o1, Object o2) {
			if (o1 == o2) {
				return true;
			} else {
				if ((o1 == null) || (o2 == null)) {
					return false;
				} else {
					return o1.equals(o2);
				}
			}
		}

		/**
		 * Generate an hashCode, including the case null object
		 */
		private int hashCode(Object o) {
			return o == null ? 0 : o.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final StringifierKey other = (StringifierKey) obj;

			return equalsTo(this.type, other.type) && equalsTo(this.subType, other.subType);
		}

		@Override
		public int hashCode() {
			return 3 + 5 * hashCode(this.type) + 7 * hashCode(this.subType);
		}

		@Override
		public String toString() {
			return "StringifierKey[" + type.getName() + "," + subType + "]";
		}
	}

	/**
	 * Main attribute of this class as it contains the dictionnary
	 */
	private final Map<StringifierKey, Stringifier> stringifiers = new HashMap<>();
	/**
	 * Null stringifier used to format null values
	 */
	private Stringifier nullStringifier;
	/**
	 * Default Stringier used when no value is found in the dictionnary
	 */
	private Stringifier defaultStringifier;

	/**
	 * Adds a stringifier to the dictionnary
	 *
	 * @param type Type (null sub-type)
	 * @param stringifier Stringifier
	 */
	public final <T> void add(Class<? extends T> type, Stringifier<T> stringifier) {
		add(type, null, stringifier);
	}

	/**
	 * Adds a stringifier to the dictionnary.
	 *
	 * @param type Type
	 * @param stringifier Stringifier
	 */
	public final <T> void add(Class<? extends T> type, String name, Stringifier<T> stringifier) {
		stringifiers.put(new StringifierKey(type, name), stringifier);
	}

	/**
	 * Look for a stringifier in the dictionnary
	 *
	 * @param type Type (null sub-type)
	 * @return Stringifier
	 */
	public final <T> Stringifier<T> getForType(Class<? extends T> type) {
		return getForType(type, null);
	}

	/**
	 * Look for a stringifier in the dictionary.<ol>
	 * <li>First look with type+subtype</li>
	 * <li>If not found, try with type alone</li>
	 * </ol>
	 *
	 * @param type Type
	 * @param subType Sub type
	 * @return Stringifier
	 */
	@SuppressWarnings("unchecked")
	private <T> Stringifier<T> get(Class<? extends T> type, String subType) {
		Stringifier<T> stringifier = null;
		if (subType != null) {
			stringifier = stringifiers.get(new StringifierKey(type, subType));
		}
		if (stringifier == null) {
			stringifier = stringifiers.get(new StringifierKey(type, null));
		}
		return stringifier;
	}

	/**
	 * Look for a stringifier in the dictionary.<ol>
	 * <li>First look with type+subtype</li>
	 * <li>If not found, try with type alone</li>
	 * </ol>
	 *
	 * @param type Type
	 * @param subType Sub type
	 * @return Stringifier
	 */
	@SuppressWarnings("unchecked")
	public final <T> Stringifier<T> getForType(Class<? extends T> type, String subType) {
		return NoneStringifier.checkInstance(get(type, subType));
	}

	/**
	 * Get stringifier for an instance:
	 * <ul>
	 * <li>If instance is null, return null stringifier</li>
	 * <li>Else look in the dictionary with instance's class, if found return it</li>
	 * <li>Else return default strinfigier</li>
	 * </ul>
	 *
	 * @param object Object instance
	 */
	@SuppressWarnings("unchecked")
	private <T> Stringifier<T> getForInstance(T object, String subType) {
		Stringifier<T> stringifier;
		if (object == null) {
			stringifier = nullStringifier;
		} else {
			stringifier = (Stringifier<T>) get(object.getClass(), subType);
			if (stringifier == null) {
				stringifier = defaultStringifier;
			}
		}
		return NoneStringifier.checkInstance(stringifier);
	}

	/**
	 * Converts an object into a String looking for appropriate
	 * stringfier among dictionary
	 *
	 * @param object Object
	 * @return String representing the object
	 */
	public String toString(Object object) {
		return getForInstance(object, null).toString(object);
	}

	/**
	 * Converts an object into a String looking for appropriate
	 * stringfier among dictionary
	 *
	 * @param object Object
	 * @param subType Sub type
	 * @return String representing the object
	 */
	public String toString(Object object, String subType) {
		return getForInstance(object, subType).toString(object);
	}

	public Stringifier getDefaultStringifier() {
		return defaultStringifier;
	}

	public void setDefaultStringifier(Stringifier defaultStringifier) {
		this.defaultStringifier = defaultStringifier;
	}

	public Stringifier getNullStringifier() {
		return nullStringifier;
	}

	public void setNullStringifier(Stringifier nullStringifier) {
		this.nullStringifier = nullStringifier;
	}

}
