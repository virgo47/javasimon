package org.javasimon.console.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaScript Array
 *
 * @author gquintana
 */
public class ArrayJS extends AnyJS {
	/**
	 * Elements of the array
	 */
	private List<AnyJS> elements;

	public ArrayJS() {
		this.elements = new ArrayList<AnyJS>();
	}

	public ArrayJS(List<AnyJS> elements) {
		this.elements = elements;
	}

	/**
	 * Get elements of the array
	 */
	public List<AnyJS> getElements() {
		return elements;
	}
	/**
	 * Add an element in the array
	 */
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
}
