package org.javasimon;

/**
 * Simon stopwatch is for time span measuring. Methods {@link #start} and {@link #stop}
 * demarcate measured interval, alternatively method {@link #addTime} can be used to add
 * split time to the stopwatch. Both ways effectively updates usage times, increase usage
 * counter by one and updates total time of the stopwatch. Multiple time-splits can be
 * measured in parallel but it is important to use proper way how to do that.
 * <p/>
 * <h3>Default behavior</h3>
 * It is not possible to cover all thread models with a single usage of {@code Stopwatch}. Default
 * behavior is safe to use in two cases:
 * <ul>
 * <li>No parallelism at all - everything runs in a single thread.
 * <li>Parallel processing where every measured event starts and stops in the same thread (but only
 * one per thread!).
 * </ul>
 * In both cases following construction can be used:
 * <pre>
 * Stopwatch stopwatch = SimonManager.getStopwatch("com.my.stopwatch").start();
 * //... here goes the measured code
 * stopwatch.stop();
 * System.out.println("Result: " + stopwatch); // not really necessary</pre>
 * This can be used for simple micro-benchmarking, critical section monitoring, in web
 * filter to measure request times, etc. All these cases ensures that start and stop occurs
 * in the same thread. ThreadLocal is used under the hood for start/stop methods without parameters.
 * <p/>
 * Notice that it is possible to use {@code start} method directly after {@code getStopwatch(...)} because it
 * returns this. {@link org.javasimon.SimonManager} should always be used to get the stopwatch before using it,
 * because otherwise the code will not reflect enable/disable of the whole API. Method {@code stop}
 * should always be used on the obtained stopwatch instance.
 * <p/>
 * <h3>Stopwatch with arbitrary keys</h3>
 * <p/>
 * Default thread-local behavior is not sufficient in some cases:
 * <ul>
 * <li>There is a need to measure multiple splits in a single thread at the moment.
 * <li>Start and stop might be called in different threads.
 * </ul>
 * In both cases a particular object is often available that one split measurement can be bound to.
 * Following example shows how to sum up life-spans of all SQL statements. In some cases there are
 * more statements open at the same moment. In that case the particular statement can be used as
 * the split-key. Example code (sorry for no exception handling and SQL injecting):
 * <pre>
 * Stopwatch stopwatch = SimonManager.getStopwatch("all-stmts");
 * Statement stmt = c.createStatement();
 * stopwatch.start(stmt);
 * rs = stmt.executeQuery("select * from foo");
 * while (rs.next()) {
 *     Statement stmt2 = c.createStatement();
 *     stopwatch.start(stmt2);
 *     stmt2.execute("delete from bar where foo_id=" + rs.getInt("id"));
 *     stopwatch.stop(stmt2);
 *     stmt2.close();
 * }
 * stopwatch.stop(stmt);
 * stmt.close();</pre>
 * This is not quite a real-life example, but it should demonstrate the point. Better example is
 * measuring HTTP session life-spans. Session is typically started and ended in different requests
 * - those are processed in random threads. Hence session itself should be used as the split-key.
 * Keyed start/stop is always safe if one split is measured for the object used as the key at
 * a time (which is mostly exactly what is needed). This method can be used anytime even for
 * single-threaded environment if lifespan of a particular object is measured as the key nicely
 * indicate directly in the code what is measured.
 * <p/>
 * Using {@code this} as a key is not always safe as it might seem. While sometimes it is exactly
 * what is needed it should not be used if {@code this} is not related to the time split. If
 * method execution time is measured default start/stop should be used. When the method is later
 * moved into different helper class and it is called from multiple threads it is still valid,
 * while using {code this} would cause troubles. On the other hand, keyed methods are preferred
 * if there is a clear relation between the key object and the split - best case is measuring
 * life-span of the object.
 * <p/>
 * <h3>Disable/enable considerations</h3>
 * <p/>
 * While Counter's usage is atomic, Stopwatch measures splits and every measurement involves two
 * calls (start/stop) over a period of time. It's important to know how various management actions
 * affect measurement:
 * <ul>
 * <li>If start OR stop is called on disabled Simon, nothing is measured. That means that if
 * stopwatch is disabled or enabled between these calls, stop always returns 0 and totals are
 * not updated.
 * <li>If stopwatch is obtained from enabled Manager (API is enabled) and Manager is later
 * disabled before stop, this split is measured because real stopwatch instance was obtained.
 * If it's other way around then the split is not measured because obtained instance is "null"
 * Simon.
 * <li>If Simon instance is obtained again from the Manager in order to call stop method the result
 * is not guaranteed. It is deterministic though, but this is not considered official use case and
 * might change in the future. <i>(For example: One call {@code start} on the real stopwatch, then
 * {@code stop} on the NullSimon because the instance was obtained from disabled API, then again
 * {@code start} on disabled API and finally - after the API is enabled again - the last {@code stop}
 * might pair up with the first start - which would "measure" unrealistic long split. Simply - don't
 * do this. ;-))</i>
 * </ul>
 * <p/>
 * While API disable causes that the code works with "null" Simons, state of the real Simon is
 * perfectly preserved. Disabling particular Simon on the other hand resets some of its state.
 * When the stopwatch is disabled, its active count is set to 0 and before it is enabled again both
 * its thread-local split map and keyed-object split map is cleared. Of course, all totals/counts
 * are preserved.
 * <p/>
 * <h3>Other usages</h3>
 * <p/>
 * Reset of the stopwatch resets all stats except usages that are rather management related and
 * should not be reset. Reset is used often for various sampling purposes which requires that only
 * cumulative stats are reset, but all running splits are preserved. Running splits will be added
 * to the stopwatch after reset when respective stop methods are called.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface Stopwatch extends Simon {
	/**
	 * Adds split time in nanoseconds to total time of the stopwatch.
	 *
	 * @param ns split time
	 * @return this stopwatch
	 */
	Stopwatch addTime(long ns);

	/**
	 * Starts this stopwatch.
	 *
	 * @return this stopwatch
	 */
	Stopwatch start();

	/**
	 * Stops this stopwatch and adds split time (stop-start) to total time of the stopwatch.
	 *
	 * @return split time in nanoseconds
	 */
	long stop();

	/**
	 * Starts this stopwatch with a key for a specific split.
	 *
	 * @param key split key
	 * @return this stopwatch
	 */
	Stopwatch start(Object key);

	/**
	 * Stops key-specific split and adds its time (stop-start) to the total time of the stopwatch.
	 *
	 * @param key split key
	 * @return split time in nanoseconds
	 */
	long stop(Object key);

	/**
	 * Returns total sum of all split times in nanoseconds.
	 *
	 * @return total time of the stopwatch in nanoseconds
	 */
	long getTotal();

	/**
	 * Returns value of the last added split - wheter it was added directly or with stop method.
	 *
	 * @return value of the last added split
	 */
	long getLast();

	/**
	 * Returns usage count of the stopwatch. Counter is increased by {@code addTime} and
	 * {@code stop} - that means that it's updated every time the next time split is added.
	 *
	 * @return count of time splits
	 */
	long getCounter();

	/**
	 * Returns maximal time split value in nanoseconds.
	 *
	 * @return maximal time split in nanoseconds
	 */
	long getMax();

	/**
	 * Returns minimal time split value in nanoseconds.
	 *
	 * @return minimal time split in nanoseconds
	 */
	long getMin();

	/**
	 * Returns ms timestamp when the max value was measured.
	 *
	 * @return ms timestamp of the max value measurement
	 */
	long getMaxTimestamp();

	/**
	 * Returns ms timestamp when the min value was measured.
	 *
	 * @return ms timestamp of the min value measurement
	 */
	long getMinTimestamp();

	/**
	 * Resets the Simon - clears total time, min, max, usage stats, etc. Split times that
	 * started before reset will be counted when appropriate stop is called, so no split
	 * time is ignored by the stopwatch.
	 *
	 * @return returns this
	 */
	Stopwatch reset();

	/**
	 * Returns current number of measured splits (concurrently running).
	 *
	 * @return current number of active splits
	 */
	long getActive();

	/**
	 * Returns peek value of active concurrent splits.
	 *
	 * @return maximum reached value of active splits
	 */
	long getMaxActive();

	/**
	 * Retruns ms timestamp when the last peek of the active split count occured.
	 *
	 * @return ms timestamp of the last peek of the active split count
	 */
	long getMaxActiveTimestamp();
}
