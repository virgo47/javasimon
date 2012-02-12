package org.javasimon.console.action;

import java.io.IOException;
import java.io.PrintWriter;
import org.javasimon.Simon;
import org.javasimon.console.ActionContext;

/**
 * Export Simons as a flat CSV table
 *
 * @author gquintana
 */
public class TableCsvAction extends AbstractTableAction {

	public static final String PATH = "/data/table.csv";
	/**
	 * Column separator (defaults to comma).
	 * Excel uses semi-comma (hence they should have a called this format SCSV ;-) )
	 */
	private String columnSeparator=",";
	/**
	 * Row separator (defaults to CRLF).
	 */
	private String rowSeparator="\r\n";

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
	protected void printBodyRow(Simon simon, PrintWriter writer) throws IOException {
		super.printBodyRow(simon, writer);
		printEndOfLine(writer);
	}

	@Override
	protected void printCell(Column column, String s, PrintWriter writer) {
		super.printCell(column, s, writer);
		writer.write(columnSeparator);
	}

	private void printEndOfLine(PrintWriter writer) {
		writer.write(rowSeparator);
	}
}
