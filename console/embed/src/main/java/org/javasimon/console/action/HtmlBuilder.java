package org.javasimon.console.action;

import java.io.IOException;
import java.io.Writer;
import org.javasimon.Simon;
import org.javasimon.console.SimonType;
import org.javasimon.console.reflect.Getter;
import org.javasimon.console.reflect.GetterFactory;
import org.javasimon.console.text.StringifierFactory;

/**
 * Class to ease HTML generation
 * @author gquintana
 */
public class HtmlBuilder<T extends HtmlBuilder> {
	/**
	 * Delegate write
	 */
	private final Writer writer;
	/**
	 * Stringifier factory
	 */
	private final StringifierFactory stringifierFactory;
	/**
	 * Constructor without stringifier factory.
	 */
	public HtmlBuilder(Writer writer) {
		this.writer = writer;
		this.stringifierFactory = null;
	}
	/**
	 * Constructor with stringifier factory.
	 */
	public HtmlBuilder(Writer writer, StringifierFactory stringifierFactory) {
		this.writer = writer;
		this.stringifierFactory = stringifierFactory;
	}
	/**
	 * Begin an element without closing (no ending &gt; )
	 * @param element Tag name
	 * @param id Element Id
	 * @param cssClass CSS class name
	 */
	private void doBegin(String element, String id, String cssClass) throws IOException {
		writer.write('<');
		writer.write(element);
		attr("id",id);
		attr("class",cssClass);
	}
	/**
	 * Add attribute
	 * @param name Attribute name
	 * @param value Attribute value
	 */
	public T attr(String name, String value) throws IOException {
		if (value!=null) {
			writer.write(' ');
			writer.write(name);
			writer.write("=\"");
			writer.write(value);
			writer.write('\"');
		}
		return (T) this;
	}
	/**
	 * Begin an element.
	 * @param element Tag name
	 * @param id Element Id
	 * @param cssClass CSS class name
	 */
	public T begin(String element, String id, String cssClass) throws IOException {
		doBegin(element, id, cssClass);
		writer.write('>');
		return (T) this;
	}
	/**
	 * Begin an element.
	 */
	public T begin(String element) throws IOException {
		return begin(element, null, null);
	}
	/**
	 * Simple element.
	 * @param element Tag name
	 * @param id Element Id
	 * @param cssClass CSS class name
	 */
	public T element(String element, String id, String cssClass) throws IOException {
		doBegin(element, id, cssClass);
		writer.write(" />");
		return (T) this;
	}
	/**
	 * End an element.
	 * @param element Tag name
	 */
	public T end(String element) throws IOException {
		writer.write("</");
		writer.write(element);
		writer.write('>');
		return (T) this;
	}
	/**
	 * Write text.
	 * @param text Text (can be null)
	 */
	public T text(String text) throws IOException {
		if (text!=null) {
			writer.write(text);
		}
		return (T) this;
	}
	/**
	 * Write verbatim .
	 * @param text Text
	 */
	public T write(String text) throws IOException {
		writer.write(text);
		return (T) this;
	}
	/**
	 * Page header.
	 * @param title Page title
	 */
	public T header(String title) throws IOException {
		return (T) begin("html")
			.begin("head")
				.begin("title").text("Simon Console: ").text(title).end("title")
				.write("<link  href=\"../resource/css/javasimon.css\" rel=\"stylesheet\" type=\"text/css\" />")
			.end("head")
			.begin("body")
				.begin("h1")
					.write("<img id=\"logo\" src=\"../resource/images/logo.png\" alt=\"Logo\" />")
					.text("Simon Console: ").text(title)
				.end("h1");
	}
	/**
	 * Page footer.
	 */	
	public T footer() throws IOException {
		return (T) end("body").end("html");
	}
	/**
	 * Write Simon property (using Java Bean convention).
	 * @param simon Simon
	 * @param propertyName Property name
	 */
	public T simonProperty(Simon simon, String propertyName) throws IOException {
		Getter getter=GetterFactory.getGetter(simon.getClass(), propertyName);
		if (getter!=null) {
			text(stringifierFactory.toString(getter.get(simon), getter.getSubType()));
		}
		return (T) this;
	}
	/**
	 * Apply stringifier on object and write it
	 * @param object Object
	 */
	public T object(Object object) throws IOException {
		text(stringifierFactory.toString(object));
		return (T) this;
	}
	/**
	 * Icon image representing Simon Type
	 * @param simonType Simon Type
	 * @param rootPath Path to root of Simon Console resources
	 */
	public T simonTypeImg(SimonType simonType, String rootPath) throws IOException {
		String image=null;
		switch(simonType) {
			case COUNTER:	image="TypeCounter.png";	break;
			case STOPWATCH: image="TypeStopwatch.png";	break;
			case UNKNOWN:	image="TypeUnknown.png";	break;
		}
		String label=simonType.name().toLowerCase();
		doBegin("img", null, label+" icon");
		attr("src",rootPath+"resource/images/"+image);
		attr("alt", label);
		writer.write(" />");
		return (T) this;
	}
}
