package org.javasimon.console.action;

import java.io.IOException;
import java.io.Writer;
import org.javasimon.Simon;
import org.javasimon.console.html.HtmlBuilder;
import org.javasimon.console.text.StringifierFactory;

/**
 * HTML generator for Detail view
 * @author gquintana
 */
public class DetailHtmlBuilder extends HtmlBuilder<DetailHtmlBuilder> {

	public DetailHtmlBuilder(Writer writer, StringifierFactory stringifierFactory) {
		super(writer, stringifierFactory);
	}

	public DetailHtmlBuilder beginSection(String id, String title) throws IOException {
		return begin("div", id, "sectionPanel")
			.begin("div", null, "title").write(title).end("div")
			.begin("table", null, "sectionTable");
	}

	public DetailHtmlBuilder endSection() throws IOException {
		return end("table").end("div");
	}

	public DetailHtmlBuilder beginRow() throws IOException {
		return begin("tr");
	}

	public DetailHtmlBuilder endRow() throws IOException {
		return end("tr");
	}

	public DetailHtmlBuilder labelCell(String propertyLabel) throws IOException {
		return begin("td class=\"label\"").text(propertyLabel).end("td");
	}

	public DetailHtmlBuilder beginValueCell() throws IOException {
		return beginValueCell("");
	}

	public DetailHtmlBuilder beginValueCell(String extraAttrs) throws IOException {
		return begin("td class=\"value\"" + extraAttrs);
	}

	public DetailHtmlBuilder endValueCell() throws IOException {
		return end("td");
	}
	public DetailHtmlBuilder valueCell(String text) throws IOException {
		return beginValueCell().write(text).endValueCell();
	}
	public DetailHtmlBuilder valueCell(String extraAttr, String text) throws IOException {
		return beginValueCell(extraAttr).write(text).endValueCell();
	}
	public DetailHtmlBuilder simonProperty(Simon simon, String propertyLabel, String propertyName) throws IOException {
		return labelCell(propertyLabel).beginValueCell().simonProperty(simon, propertyName).endValueCell();
	}

	public DetailHtmlBuilder simonProperty(Simon simon, String propertyLabel, String propertyName, Integer colSpan) throws IOException {
		return labelCell(propertyLabel).beginValueCell(" colspan=\"" + colSpan + "\"").simonProperty(simon, propertyName).endValueCell();
	}
    
}
