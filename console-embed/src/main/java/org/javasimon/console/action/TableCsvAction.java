package org.javasimon.console.action;

import org.javasimon.Sample;
import org.javasimon.console.ActionContext;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Export Simons as a flat CSV table.
 *
 * @author gquintana
 */
public class TableCsvAction extends AbstractTableAction {

	public static final String PATH = "/data/table.csv";

	/**
	 * Column separator (defaults to comma).
	 */
	private static final String columnSeparator = ",";

	/**
	 * Row separator (defaults to CRLF).
	 */
	private static final String rowSeparator = "\r\n";

	public TableCsvAction(ActionContext context) {
		super(context, "text/csv");
		stringifierFactory = new CsvStringifierFactory();
	}

	@Override
	protected void printHeaderRow(PrintWriter writer) throws IOException {
		super.printHeaderRow(writer);
		printEndOfLine(writer);
	}

	@Override
	protected void printBodyRow(Sample sample, PrintWriter writer) throws IOException {
		super.printBodyRow(sample, writer);
		printEndOfLine(writer);
	}

	@Override
	protected void printCell(Column column, String s, PrintWriter writer) throws IOException {
		super.printCell(column, s, writer);
		writer.write(columnSeparator);
	}

	private void printEndOfLine(PrintWriter writer) {
		writer.write(rowSeparator);
	}
}
