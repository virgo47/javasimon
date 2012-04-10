package org.javasimon.javaee;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import org.javasimon.Split;

/**
 * RequestReporter interface .
 *
 * @author <a href="mailto:richard.richter@posam.sk">Richard "Virgo" Richter</a>
 */
public interface RequestReporter {
	/**
	 * Reports request that exceeds the threshold.
	 *
	 * @param request offending HTTP request
	 * @param requestSplit split measuring the offending request
	 * @param splits list of all splits started for this request
	 */
	void reportRequest(HttpServletRequest request, Split requestSplit, List<Split> splits);

	/**
	 * Called by initialization after the instance creation. Useful when {@link org.javasimon.Manager} is needed
	 * ({@link org.javasimon.javaee.SimonServletFilter#getManager()}) and possibly for other scenarios.
	 */
	void setSimonServletFilter(SimonServletFilter simonServletFilter);
}
