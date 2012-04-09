package org.javasimon.console.action;

import java.io.IOException;
import java.io.PrintWriter;
import org.javasimon.Sample;
import org.javasimon.console.ActionContext;
import org.javasimon.console.text.StringifierFactory;

/**
 * Export Simons as a flat static HTML table which can be used for printing.
 * Each row as an odd/even CSS class.
 * Each cell as a CSS class corresponding to property name
 * Only a subset (definined in {@link AbstractTableAction#columns}) of attributes are exported.
 *
 * @author gquintana
 */
public class TableHtmlAction extends AbstractTableAction {

	public static final String PATH = "/data/table.html";

	public TableHtmlAction(ActionContext context) {
		super(context, "text/html");
		this.stringifierFactory = new StringifierFactory();
		this.numberPattern=StringifierFactory.INTEGER_NUMBER_PATTERN;
	}
	private Integer index;

	@Override
	protected void printTable(PrintWriter writer) throws IOException {
		writer.write("<html>");
		writer.write("<head>");
		writer.write("<title>Simon Console: Table</title>");
		writer.write("<link  href=\"../resource/css/javasimon.css\" rel=\"stylesheet\" type=\"text/css\" />");
		writer.write("</head>");
		writer.write("<body>");
		writer.write("<h1><img id=\"logo\" src=\"../resource/images/logo.png\" alt=\"Logo\" />Simon Console: Table</h1>");
		writer.write("<table class=\"flatTable\">");
		super.printTable(writer);
		writer.write("</table>");
		writer.write("</body>");
		writer.write("</html>");
	}

	@Override
	protected void printHeaderRow(PrintWriter writer) throws IOException {
		writer.write("<thead><tr>");
		super.printHeaderRow(writer);
		writer.write("</tr></thead>");
	}

	@Override
	protected void printBody(PrintWriter writer) throws IOException {
		writer.write("<tbody>");
		index = 0;
		super.printBody(writer);
		index = null;
		writer.write("</tbody>");
	}

	@Override
	protected void printBodyRow(Sample sample, PrintWriter writer) throws IOException {
		writer.write("<tr id=\"" + index + "\" class=\"" + (index % 2 == 0 ? "even" : "odd") + "\">");
		super.printBodyRow(sample, writer);
		index++;
		writer.write("</tr>");
	}

	@Override
	protected void printCell(Column column, String s, PrintWriter writer) {
		writer.write("<td class=\"");
		writer.write(column.getName());
		writer.write("\">");
		super.printCell(column, s, writer);
		writer.write("</td>");
	}
}
