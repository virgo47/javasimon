package org.javasimon.console;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.SimonManager;
import org.javasimon.Stopwatch;
import org.javasimon.utils.SimonUtils;

/**
 * @author gquintana
 */
public class DataJsonAction extends Action {
	static Object PATH = "/data.json";
	private String pattern;
	private SimonType type;
	private TimeFormat timeFormat = TimeFormat.MILLISECOND;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public DataJsonAction(HttpServletRequest request, HttpServletResponse response) {
		super(request, response);
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public void readParameters() {
		pattern = getParameterAsString("pattern", null);
		type = getParameterAsEnum("type", SimonType.class, SimonType.STOPWATCH);
		timeFormat = getParameterAsEnum("timeFormat", TimeFormat.class, TimeFormat.MILLISECOND);
	}

	private String formatValue(String value) {
		String sValue = value == null ? "" : value.replace("\"", "\\\"").replace("\\", "\\\\").replace("/", "\\/");
		return "\"" + sValue + "\"";
	}

	private String formatAttribute(String attribute) {
		return "\"" + attribute + "\":";
	}

	private String formatAttribute(String attribute, String value) {
		return formatAttribute(attribute) + formatValue(value);
	}

	private String formatValue(long value) {
		return Long.toString(value);
	}

	private String formatValue(Date value) {
		return formatValue(dateFormat.format(value));
	}

	private String formatAttribute(String attribute, long value) {
		return formatAttribute(attribute) + formatValue(value);
	}

	private String formatAttribute(String attribute, long value, TimeFormat timeFormat) {
		return formatAttribute(attribute) + timeFormat.format(value);
	}

	private String formatAttribute(String attribute, Date value) {
		return formatAttribute(attribute) + formatValue(value);
	}

	enum SimonType {
		STOPWATCH, COUNTER, UNKNOWN;
	}

	enum TimeFormat {
		NANOSECOND
			{
				@Override
				public String format(long value) {
					return Long.toString(value);
				}
			},
		MICROSECOND
			{
				@Override
				public String format(long value) {
					return Long.toString(value / 1000);
				}
			},
		MILLISECOND
			{
				@Override
				public String format(long value) {
					return Long.toString(value / 1000000);
				}
			},
		SECOND
			{
				@Override
				public String format(long value) {
					return Long.toString(value / 1000000000);
				}
			},
		PRESENT
			{
				@Override
				public String format(long value) {
					return "\"" + SimonUtils.presentNanoTime(value) + "\"";
				}
			};

		public abstract String format(long value);
	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		getResponse().setContentType("application/json");
		PrintWriter printWriter = getResponse().getWriter();
		printWriter.println("[");
		int rowCount = 0;
		for (Simon simon : SimonManager.manager().getSimons(getPattern())) {
			SimonType lType;
			Stopwatch stopwatch = null;
			Counter counter = null;
			if (simon instanceof Stopwatch) {
				stopwatch = (Stopwatch) simon;
				lType = SimonType.STOPWATCH;
			} else if (simon instanceof Counter) {
				counter = (Counter) simon;
				lType = SimonType.COUNTER;
			} else {
				lType = SimonType.UNKNOWN;
			}
			if (type != null && type != lType) {
				continue;
			}
			if (rowCount > 0) {
				printWriter.print(",");
			}
			printWriter.print("{");
			printWriter.print(formatAttribute("name", simon.getName()) + ", ");
			printWriter.print(formatAttribute("note", simon.getNote()) + ", ");
			printWriter.print(formatAttribute("type", type.name().toLowerCase()) + ", ");
			switch (type) {
				case STOPWATCH:
					printWriter.print(formatAttribute("count", stopwatch.getCounter()) + ", ");
					printWriter.print(formatAttribute("total", stopwatch.getTotal(), timeFormat) + ", ");
					printWriter.print(formatAttribute("min", stopwatch.getMin(), timeFormat) + ", ");
					printWriter.print(formatAttribute("mean", (long) stopwatch.getMean(), timeFormat) + ", ");
					printWriter.print(formatAttribute("max", stopwatch.getMax(), timeFormat) + ", ");
					printWriter.print(formatAttribute("standardDeviation", (long) stopwatch.getStandardDeviation(), timeFormat) + ", ");
					printWriter.print(formatAttribute("last", stopwatch.getLast(), timeFormat) + ", ");
					break;
				case COUNTER:
					printWriter.print(formatAttribute("type", "counter") + ", ");
					printWriter.print(formatAttribute("min", counter.getMin()) + ", ");
					printWriter.print(formatAttribute("minDate", new Date(counter.getMinTimestamp())) + ", ");
					printWriter.print(formatAttribute("max", counter.getMax()) + ", ");
					printWriter.print(formatAttribute("maxDate", new Date(counter.getMaxTimestamp())) + ", ");
					break;
			}
			printWriter.print(formatAttribute("firstUsage", new Date(simon.getFirstUsage())) + ", ");
			printWriter.print(formatAttribute("lastUsage", new Date(simon.getLastUsage())));
			printWriter.println("}");
			rowCount++;
		}
		printWriter.println("]");
	}

}
