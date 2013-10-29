package org.javasimon;

import java.util.Iterator;
import java.util.Map;

/**
 * Interface that declares support for arbitrary attributes that can be attached to the object (servlet style).
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @since 3.4
 */
public interface HasAttributes {
	/**
	 * Stores an attribute in this Simon. Attributes can be used to store any custom objects.
	 *
	 * @param name a String specifying the name of the attribute
	 * @param value the Object to be stored
	 */
	void setAttribute(String name, Object value);

	/**
	 * Returns the value of the named attribute as an Object, or null if no attribute of
	 * the given name exists.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return an Object containing the value of the attribute, or null if the attribute does not exist
	 */
	Object getAttribute(String name);

	/**
	 * Removes an attribute from this Simon.
	 *
	 * @param name a String specifying the name of the attribute to remove
	 */
	void removeAttribute(String name);

	/**
	 * Returns an Iterator containing the names of the attributes available to this Simon.
	 * This method returns an empty Iterator if the Simon has no attributes available to it.
	 *
	 * @return an Iterator of strings containing the names of the Simon's attributes
	 */
	Iterator<String> getAttributeNames();

	/**
	 * Returns copy of attributes as a sorted map, this can be used further for operations like {@code toString}.
	 *
	 * @return copy of attributes as a sorted map
	 * @since 3.4
	 */
	Map<String, Object> getCopyAsSortedMap();

	/**
	 * Returns the value of the named attribute typed to the specified class, or {@code null} if no attribute of
	 * the given name exists.
	 *
	 * @param name a String specifying the name of the attribute
	 * @return the value of the attribute typed to T, or {@code null} if the attribute does not exist
	 * @since 3.4
	 */
	<T> T getAttribute(String name, Class<T> clazz);
}
