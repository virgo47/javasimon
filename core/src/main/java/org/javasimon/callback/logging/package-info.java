/**
 * {@link org.javasimon.callback.logging.LoggingCallback} is able to log stopwatch splits and statistics once
 * in a while (N per millisecond, every N split, when split is longer than N ms, etc.)
 * and to various outputs (SLF4J, JUL).
 * These logging strategies are called log templates inherit from {@link org.javasimon.callback.logging.LogTemplate} and
 * are produced by {@link org.javasimon.callback.logging.LogTemplates} (factory class):
 * <ul>
 *   <li>{@link org.javasimon.callback.logging.SLF4JLogTemplate}: <em>where</em> &rarr; in SLF4J {@link org.slf4j.Logger}</li>
 *   <li>{@link org.javasimon.callback.logging.JULLogTemplate}: <em>where</em> &rarr; in JUL {@link java.util.logging.Logger}</li>
 *   <li>{@link org.javasimon.callback.logging.DisabledLogTemplate}: <em>where</em> &rarr; in /dev/null</li>
 *   <li>{@link org.javasimon.callback.logging.DelegateLogTemplate}: abstract log template delegating to a concrete log template
 *     <ul>
 *       <li>{@link org.javasimon.callback.logging.PeriodicLogTemplate}: <em>when</em> &rarr; every N milliseconds</li>
 *       <li>{@link org.javasimon.callback.logging.CounterLogTemplate}: <em>when</em> &rarr; every N splits</li>
 *       <li>{@link org.javasimon.callback.logging.SplitThresholdLogTemplate}: <em>when</em> &rarr; splits lasts longer than N milliseconds</li>
 *     </ul>
 *   </li>
 * </ul>
 * Then {@link org.javasimon.callback.logging.LogMessageSource} is called back by
 * the LogTemplate to get the message (a String) to log: <em>what</em>.
 */
package org.javasimon.callback.logging;