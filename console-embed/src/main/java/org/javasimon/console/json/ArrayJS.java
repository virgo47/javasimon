package org.javasimon.console.json;

import org.javasimon.console.text.StringifierFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JavaScript Array.
 *
 * @author gquintana
 */
public class ArrayJS extends AnyJS {

	/** Elements of the array. */
	private List<AnyJS> elements;

	public ArrayJS() {
		this.elements = new ArrayList<>();
	}

	public ArrayJS(int size) {
		this.elements = new ArrayList<>(size);
	}

	public ArrayJS(List<AnyJS> elements) {
		this.elements = elements;
	}

	/** Gets elements of the array. */
	public List<AnyJS> getElements() {
		return elements;
	}

	/** Adds an element in the array. */
	public void addElement(AnyJS element) {
		elements.add(element);
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write("[");
		boolean first = true;
		for (AnyJS element : elements) {
			if (first) {
				first = false;
			} else {
				writer.write(",");
			}
			element.write(writer);
		}
		writer.write("]");
	}

	/**
	 * Create an JSON Array of JSON objects from a collection of Java Objects.
	 *
	 * @param objects Java Objects
	 * @param stringifierFactory JSON Stringifier converter
	 * @return Array of JSON Objects
	 */
	public static ArrayJS create(Collection<?> objects, StringifierFactory stringifierFactory) {
		if (objects == null) {
			return null;
		}
		ArrayJS arrayJS = new ArrayJS(objects.size());
		for (Object object : objects) {
			arrayJS.addElement(ObjectJS.create(object, stringifierFactory));
		}
		return arrayJS;
	}

	/**
	 * Create an JSON Array of JSON objects from a collection of Java Objects.
	 *
	 * @param objects Java Objects
	 * @param stringifierFactory JSON Stringifier converter
	 * @return Array of JSON Objects
	 */
	public static ArrayJS create(Object[] objects, StringifierFactory stringifierFactory) {
		if (objects == null) {
			return null;
		}
		ArrayJS arrayJS = new ArrayJS(objects.length);
		for (Object object : objects) {
			arrayJS.addElement(ObjectJS.create(object, stringifierFactory));
		}
		return arrayJS;
	}
}
