package org.javasimon.console.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.console.*;

/**
 * Base class for exporting Simons as tabular data
 * The following methods a called during rendering and can be overriden
 * <ul>
 *	<li>{@link #printTable }: <ul>
 *		<li>{@link #printHeaderRow}: <ul>
 *			<li>{@link #printHeaderCell} : {@link #printCell} </li> 
 *		</ul></li>
 *		<li>{@link #printBody}: <ul>
 *			<li>{@link #printBodyRow}: <ul>
 *				<li>{@link #printBodyCell} : {@link #printCell} </li> 
 *			</ul></li>
 *		</ul></li>
 *	</ul></li>
 * </ul>
 *
 * @author gquintana
 */
public class AbstractTableAction extends Action {

	/**
	 * Value formatter
	 */
	protected ValueFormatter valueFormatter;
	/**
	 * Pattern for Simon name filtering
	 */
	private String pattern;
	/**
	 * Type for Simon type filtering
	 */
	private SimonType type;
	/**
	 * Column list in response
	 */
	private List<Column> columns = new ArrayList<Column>();
	/**
	 * Content type of response
	 */
	protected final String contentType;
	/**
	 * Base constructor initialiszes columns list
	 */
	protected AbstractTableAction(ActionContext context, String contentType) {
		super(context);
		this.contentType = contentType;
		columns.add(new StringColumn("Name", "name"));
		columns.add(new EnumColumn<SimonType>("Type", "type") {

			@Override
			public SimonType getValue(Simon simon) {
				return SimonType.getValueFromInstance(simon);
			}
		});
		columns.add(new LongColumn("Counter", "counter"));
		columns.add(new LongTimeColumn("Total", "total"));
		columns.add(new LongTimeColumn("Min", "min"));
		columns.add(new DoubleTimeColumn("Mean", "mean"));
		columns.add(new LongTimeColumn("Last", "last"));
		columns.add(new LongTimeColumn("Max", "max"));
		columns.add(new DoubleTimeColumn("Std Dev", "standardDeviation"));
		columns.add(new DateColumn("First Use", "firstUsage"));
		columns.add(new DateColumn("Last Use", "lastUsage"));
		columns.add(new StringColumn("Note", "note"));

	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void readParameters() {
		valueFormatter.setTimeFormat(getContext().getParameterAsEnum("timeFormat", TimeFormatType.class, TimeFormatType.MILLISECOND));
		pattern = getContext().getParameterAsString("pattern", null);
		type = getContext().getParameterAsEnum("type", SimonType.class, null);
	}

	protected void printTable(PrintWriter writer) throws IOException {
		printHeaderRow(writer);
		printBody(writer);
	}

	protected void printHeaderRow(PrintWriter writer) throws IOException {
		for (Column column : columns) {
			printHeaderCell(column, writer);
		}
	}

	protected void printHeaderCell(Column c, PrintWriter writer) {
		printCell(c, c.getTitle(), writer);
	}

	protected void printBody(PrintWriter writer) throws IOException {
		SimonVisitors.visitList(SimonManager.manager(), pattern, type, new SimonVisitorImpl(writer));
	}

	protected void printBodyRow(Simon simon, PrintWriter writer) throws IOException {
		for (Column column : columns) {
			printBodyCell(column, simon, writer);
		}
	}

	protected void printBodyCell(Column c, Simon s, PrintWriter writer) {
		printCell(c, c.getFormattedValue(s), writer);
	}

	protected void printCell(Column c, String s, PrintWriter writer) {
		if (s != null) {
			writer.write(s);
		}
	}

	public void execute() throws ServletException, IOException, ActionException {
		PrintWriter writer = null;
		try {
			getContext().setContentType(contentType);
			writer = getContext().getWriter();
			printTable(writer);
		} finally {
			if (writer != null) {
				writer.flush();
			}
		}
	}

	private class SimonVisitorImpl implements SimonVisitor {

		private final PrintWriter writer;

		public SimonVisitorImpl(PrintWriter writer) {
			this.writer = writer;
		}

		public void visit(Simon simon) throws IOException {
			printBodyRow(simon, writer);
		}
	}
	/**
	 * Base class for columns 
	 */
	protected abstract class Column<T> {
		/**
		 * Column title/header
		 */
		private final String title;
		/**
		 * Column property name
		 */
		private final String name;

		public Column(String title, String name) {
			this.title = title;
			this.name = name;
		}
		/**
		 * Get column property name
		 */
		public String getName() {
			return name;
		}
		/**
		 * Get column property title/header
		 */
		public String getTitle() {
			return title;
		}
		/**
		 * Get raw column value
		 */
		public T getValue(Simon simon) {
			Getter<T> getter = Getter.getGetter(simon.getClass(), name);
			return getter == null ? null : getter.get(simon);
		}
		/**
		 * Get formatted column value
		 */
		public abstract String getFormattedValue(Simon simon);
	}
	/**
	 * String column 
	 */
	protected class StringColumn extends Column<String> {

		public StringColumn(String title, String name) {
			super(title, name);
		}

		public String getFormattedValue(Simon simon) {
			return valueFormatter.formatString(getValue(simon));
		}
	}
	/**
	 * Long column 
	 */
	protected class LongColumn extends Column<Long> {

		public LongColumn(String title, String name) {
			super(title, name);
		}

		public String getFormattedValue(Simon simon) {
			return valueFormatter.formatNumber(getValue(simon));
		}
	}
	/**
	 * Long Time column 
	 */
	protected class LongTimeColumn extends Column<Long> {

		public LongTimeColumn(String title, String name) {
			super(title, name);
		}

		public String getFormattedValue(Simon simon) {
			Long value = getValue(simon);
			String formattedValue;
			if (simon instanceof Stopwatch) {
				formattedValue = valueFormatter.formatTime(value);
			} else {
				formattedValue = valueFormatter.formatNumber(value);
			}
			return formattedValue;
		}
	}

	/**
	 * Double Time column 
	 */
	protected class DoubleTimeColumn extends Column<Double> {

		public DoubleTimeColumn(String title, String name) {
			super(title, name);
		}

		public String getFormattedValue(Simon simon) {
			Double value = getValue(simon);
			String formattedValue;
			if (simon instanceof Stopwatch) {
				formattedValue = valueFormatter.formatTime(value);
			} else {
				formattedValue = valueFormatter.formatNumber(value);
			}
			return formattedValue;
		}
	}
	/**
	 * Enum column 
	 */
	protected class EnumColumn<T extends Enum<T>> extends Column<T> {

		public EnumColumn(String title, String name) {
			super(title, name);
		}

		public String getFormattedValue(Simon simon) {
			return valueFormatter.formatEnum(getValue(simon));
		}
	}

	protected class DateColumn extends Column<Long> {

		public DateColumn(String title, String name) {
			super(title, name);
		}

		public String getFormattedValue(Simon simon) {
			return valueFormatter.formatDate(getValue(simon));
		}
	}
}
