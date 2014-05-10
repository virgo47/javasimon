package org.javasimon.console.json;

import org.javasimon.console.reflect.Getter;
import org.javasimon.console.reflect.GetterFactory;
import org.javasimon.console.text.Stringifier;
import org.javasimon.console.text.StringifierFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JavaScript Object.
 *
 * @author gquintana
 */
public class ObjectJS extends AnyJS {

	private List<Attribute> attributes;
	private Map<String, Attribute> attributesByName;

	public ObjectJS() {
		this.attributes = new ArrayList<>();
		this.attributesByName = new HashMap<>();
	}

	public void setAttribute(String name, AnyJS value) {
		assert name != null;
		assert value != null;
		Attribute attribute = attributesByName.get(name);
		if (attribute == null) {
			attribute = new Attribute(name, value);
			attributes.add(attribute);
			attributesByName.put(name, attribute);
		} else {
			attribute.value = value;
		}
	}

	public <T> void setSimpleAttribute(String name, T value, Stringifier<T> stringifier) {
		SimpleJS propertyJS = new SimpleJS<>(value, stringifier);
		setAttribute(name, propertyJS);
	}

	public AnyJS getAttribute(String name) {
		assert name != null;
		Attribute attribute = attributesByName.get(name);
		return attribute == null ? null : attribute.value;
	}

	@Override
	public void write(Writer writer) throws IOException {
		writer.write("{");
		boolean first = true;
		for (Attribute attribute : attributes) {
			if (first) {
				first = false;
			} else {
				writer.write(",");
			}
			writeString(writer, attribute.name);
			writer.write(":");
			attribute.value.write(writer);
		}
		writer.write("}");
	}

	@SuppressWarnings("unchecked")
	public static ObjectJS create(Object o, StringifierFactory stringifierFactory) {
		if (o == null) {
			return null;
		} else {
			ObjectJS objectJS = new ObjectJS();
			// Export properties using reflection
			for (Getter getter : GetterFactory.getGetters(o.getClass())) {
				String propertyName = getter.getName();
				Stringifier propertyStringifier = stringifierFactory.getStringifier(getter.getType(), getter.getSubType());
				if (propertyStringifier != null) {
					Object propertyValue = getter.get(o);
					objectJS.setSimpleAttribute(propertyName, propertyValue, propertyStringifier);
				}
			}
			return objectJS;
		}
	}

	/** Export Simon attribute map as a JS object. */
	private static class Attribute {

		private final String name;
		private AnyJS value;

		public Attribute(String name, AnyJS value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Attribute other = (Attribute) obj;
			if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			return this.name == null ? 0 : this.name.hashCode();
		}
	}
}
