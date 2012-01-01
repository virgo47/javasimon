package org.javasimon.console.action;

import java.io.IOException;
import javax.servlet.ServletException;
import org.javasimon.Counter;
import org.javasimon.Simon;
import org.javasimon.Stopwatch;
import org.javasimon.console.*;
import org.javasimon.console.json.JsonValueFormatter;
import org.javasimon.console.json.ObjectJS;
import org.javasimon.console.json.SimpleJS;

/**
 * Base for actions returing data in JSON format
 *
 * @author gquintana
 */
public abstract class AbstractJsonAction extends Action {

	protected AbstractJsonAction(ActionContext context) {
		super(context);
	}
	protected final ValueFormatter valueFormatter = new JsonValueFormatter();

	@Override
	public void readParameters() {
		valueFormatter.setTimeFormat(getContext().getParameterAsEnum("timeFormat", TimeFormatType.class, TimeFormatType.MILLISECOND));
	}

	/**
	 * Transforms a Simon into a JSON object
	 *
	 * @param simon Simon
	 * @return JSON object
	 */
	protected ObjectJS createObjectJS(Simon simon) {
		SimonType lType = SimonType.getValueFromInstance(simon);
		ObjectJS objectJS = ObjectJS.create(simon, valueFormatter);
		objectJS.setAttribute("type", SimpleJS.createEnum(lType, valueFormatter));
		switch (lType) {
			case STOPWATCH:
				Stopwatch stopwatch = (Stopwatch) simon;
				objectJS.setAttribute("total", SimpleJS.createTime(stopwatch.getTotal(), valueFormatter));
				objectJS.setAttribute("min", SimpleJS.createTime(stopwatch.getMin(), valueFormatter));
				objectJS.setAttribute("mean", SimpleJS.createTime(stopwatch.getMean(), valueFormatter));
				objectJS.setAttribute("max", SimpleJS.createTime(stopwatch.getMax(), valueFormatter));
				objectJS.setAttribute("standardDeviation", SimpleJS.createTime(stopwatch.getStandardDeviation(), valueFormatter));
				objectJS.setAttribute("last", SimpleJS.createTime(stopwatch.getLast(), valueFormatter));
				objectJS.setAttribute("maxActiveTimestamp", SimpleJS.createDate(stopwatch.getMaxActiveTimestamp(), valueFormatter));
				objectJS.setAttribute("minTimestamp", SimpleJS.createDate(stopwatch.getMinTimestamp(), valueFormatter));
				objectJS.setAttribute("maxTimestamp", SimpleJS.createDate(stopwatch.getMaxTimestamp(), valueFormatter));
				break;
			case COUNTER:
				Counter counter = (Counter) simon;
				objectJS.setAttribute("minTimestamp", SimpleJS.createDate(counter.getMinTimestamp(), valueFormatter));
				objectJS.setAttribute("maxTimestamp", SimpleJS.createDate(counter.getMaxTimestamp(), valueFormatter));
				break;
		}
		objectJS.setAttribute("firstUsage", SimpleJS.createDate(simon.getFirstUsage(), valueFormatter));
		objectJS.setAttribute("lastUsage", SimpleJS.createDate(simon.getLastUsage(), valueFormatter));
		return objectJS;

	}

	@Override
	public void execute() throws ServletException, IOException, ActionException {
		getContext().setContentType("application/json");
	}
}
