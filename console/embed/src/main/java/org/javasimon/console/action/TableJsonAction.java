package org.javasimon.console.action;

import java.io.IOException;
import java.io.PrintWriter;
import org.javasimon.Simon;
import org.javasimon.console.ActionContext;
import org.javasimon.console.json.ArrayJS;
import org.javasimon.console.json.JsonValueFormatter;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.json.SimpleJS;

/**
 * Export Simons as a flat JSON array to be displayed in datatable.
 * Each JSONized simon as all attributes even if not present on real Simon.
 * Only a subset (definined in {@link AbstractTableAction#columns}) of attributes are exported.
 *
 * @author gquintana
 */
public class TableJsonAction extends AbstractTableAction {

	public static final String PATH = "/data/table.json";

	public TableJsonAction(ActionContext context) {
		super(context, "application/json");
		this.valueFormatter = new JsonValueFormatter();
	}
	/**
	 * Current array of JSONized Simons 
	 */
	private ArrayJS simonsJS;
	/**
	 * Current JSONized Simon
	 */
	private ObjectJS simonJS;

	@Override
	protected void printTable(PrintWriter writer) throws IOException {
		simonsJS = new ArrayJS();
		super.printTable(writer);
		simonsJS.write(writer);
		simonJS = null;
	}

	@Override
	protected void printHeaderRow(PrintWriter writer) throws IOException {
		// No header
	}

	@Override
	protected void printBodyRow(Simon simon, PrintWriter writer) throws IOException {
		simonJS = new ObjectJS();
		simonsJS.addElement(simonJS);
		super.printBodyRow(simon, writer);
		simonJS = null;
	}

	@Override
	protected void printBodyCell(Column column, Simon s, PrintWriter writer) {
		simonJS.setAttribute(column.getName(), SimpleJS.createObject(column.getFormattedValue(s), valueFormatter));
	}
}
