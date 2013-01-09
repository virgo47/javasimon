package org.javasimon.console.action;

import org.javasimon.console.html.HtmlBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import org.javasimon.Sample;
import org.javasimon.console.ActionContext;
import org.javasimon.console.SimonType;
import org.javasimon.console.text.StringifierFactory;

/**
 * Export Simons as a flat static HTML table which can be used for printing.
 * Each row as an odd/even CSS class.
 * Each cell as a CSS class corresponding to property name
 * Only a subset (definined in {@link AbstractTableAction#columns}) of attributes are exported.
 * Path: http://.../data/table.html?pattern=SimonPattern&type=STOPWATCH&type=COUNTER&timeFormat=MILLISECOND
 *
 * @author gquintana
 */
public class TableHtmlAction extends AbstractTableAction {
	/**
	 * Path to display Flat HTML table
	 */
	public static final String PATH = "/data/table.html";
	/**
	 * Constructor.
	 */
	public TableHtmlAction(ActionContext context) {
		super(context, "text/html");
		this.stringifierFactory = new StringifierFactory();
		this.numberPattern=StringifierFactory.INTEGER_NUMBER_PATTERN;
	}
	/**
	 * Row index
	 */
	private Integer index;
	/**
	 * HTML Builder
	 */
	private HtmlBuilder htmlBuilder;
	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void printTable(PrintWriter writer) throws IOException {
		htmlBuilder=new HtmlBuilder(writer);
		htmlBuilder.header("List View", Collections.emptyList())
			.begin("table", "flatTable", "flatTable");
		super.printTable(writer);
		htmlBuilder.end("table").footer();
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void printHeaderRow(PrintWriter writer) throws IOException {
		htmlBuilder.begin("thead").begin("tr");
		super.printHeaderRow(writer);
		htmlBuilder.end("tr").end("thead");
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void printBody(PrintWriter writer) throws IOException {
		htmlBuilder.begin("tbody");
		index = 0;
		super.printBody(writer);
		index = null;
		htmlBuilder.end("tbody");
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void printBodyRow(Sample sample, PrintWriter writer) throws IOException {
		htmlBuilder.begin("tr",Integer.toString(index), index % 2 == 0 ? "even" : "odd");
		super.printBodyRow(sample, writer);
		index++;
		htmlBuilder.end("tr");
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	protected void printCell(Column column, String s, PrintWriter writer) throws IOException {
		htmlBuilder.begin("td",null, column.getName());
		if (column.getName().equals("type")) {
			try {
				htmlBuilder.simonTypeImg(SimonType.valueOf(s), "../../");
			} catch(IllegalArgumentException illegalArgumentException) {
				// Else unknown type: non image
			}
		}
		super.printCell(column, s, writer);
		htmlBuilder.end("td");
	}
}
