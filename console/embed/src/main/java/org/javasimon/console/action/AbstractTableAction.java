package org.javasimon.console.action;

import org.javasimon.console.reflect.Getter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.javasimon.Sample;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.console.*;
import org.javasimon.console.text.Stringifier;
import org.javasimon.console.text.StringifierFactory;

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
	protected StringifierFactory stringifierFactory;
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
	 * Decimal format pattern used for printing doubles
	 */
	protected String numberPattern=StringifierFactory.READABLE_NUMBER_PATTERN;
	/**
	 * Base constructor initialiszes columns list
	 */
	protected AbstractTableAction(ActionContext context, String contentType) {
		super(context);
		this.contentType = contentType;
		columns.add(new Column<String>("Name", "name"));
		columns.add(new Column<SimonType>("Type", "type") {
			private Stringifier<SimonType> stringifier;
			@Override
			public SimonType getValue(Object object) {
				return SimonType.getValueFromInstance((Sample) object);
			}
			@Override
			public String getFormattedValue(Object object) {
				return getStringifier(object).toString(getValue(object));
			}
			@Override
			public Stringifier<SimonType> getStringifier(Object object) {
				if (stringifier==null) {
					stringifier=stringifierFactory.getStringifier(SimonType.class);
				}
				return stringifier;
			}
		});
		columns.add(new Column<Long>("Counter", "counter"));
		columns.add(new Column<Long>("Total", "total"));
		columns.add(new Column<Long>("Min", "min"));
		columns.add(new Column<Long>("Mean", "mean"));
		columns.add(new Column<Long>("Last", "last"));
		columns.add(new Column<Long>("Max", "max"));
		columns.add(new Column<Double>("Std Dev", "standardDeviation"));
		columns.add(new Column<Long>("First Use", "firstUsage"));
		columns.add(new Column<Long>("Last Use", "lastUsage"));
		columns.add(new Column<String>("Note", "note"));

	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void readParameters() {
		TimeFormatType timeFormat=getContext().getParameterAsEnum("timeFormat", TimeFormatType.class, TimeFormatType.MILLISECOND);
		stringifierFactory.init(timeFormat, 
			StringifierFactory.READABLE_DATE_PATTERN,
			numberPattern);
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
		printBodyRow(simon.sample(), writer);
	}
	protected void printBodyRow(Sample sample, PrintWriter writer) throws IOException {
		for (Column column : columns) {
			printBodyCell(column, sample, writer);
		}
	}

	protected void printBodyCell(Column c, Sample sample, PrintWriter writer) {
		printCell(c, c.getFormattedValue(sample), writer);
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
	 * Columns 
	 */
	protected class Column<T> {
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
		 * Get Getter for given column and row (simon)
		 * @param object Simon row
		 * @return Getter
		 */
		@SuppressWarnings("unchecked")
		private Getter<T> getGetter(Object object) {
			return Getter.getGetter(object.getClass(), name);
		}
		/**
		 * Get raw column value
		 */
		public T getValue(Object object) {
			Getter<T> getter = getGetter(object);
			return getter == null ? null : getter.get(object);
		}
		/**
		 * Get stringier used for given column and row
		 * @param object Row (simon)
		 * @return Stringifier
		 */
		public Stringifier<T> getStringifier(Object object) {
			Getter<T> getter = getGetter(object);
			if (getter==null) {
				return stringifierFactory.getNullStringifier();
			} else {
				return stringifierFactory.getStringifier(getter.getType(), getter.getSubType());
			}			
		}
		/**
		 * Get formatted column value
		 */
		public String getFormattedValue(Object object) {
			Getter<T> getter = getGetter(object);
			if (getter==null) {
				return stringifierFactory.toString(null);
			} else {
				return stringifierFactory.toString(getter.get(object), getter.getSubType());
			}
		}
	}

}
