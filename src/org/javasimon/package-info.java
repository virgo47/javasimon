/**
 * Java Simon is monitoring API for long-term application performance tracking. This core package
 * contains all basic interfaces ({@link org.javasimon.Simon}, {@link org.javasimon.Counter},
 * {@link org.javasimon.Stopwatch} and {@link org.javasimon.StatProcessor})
 * with their implementations along with {@link org.javasimon.SimonFactory} that takes care of all Simons and
 * their creation.
 * <p>
 * There are two basic types of Simons now:
 * <ul>
 * <li>Stopwatch that measures time and sums the time splits -
 * this is obtained by {@link org.javasimon.SimonFactory#getStopwatch(String)};
 * <li>Counter that counts - occurences, or tracks the integer value - this is obtained
 * by {@link org.javasimon.SimonFactory#getCounter(String)}.
 * </ul>
 * <p>
 * All Simons share some basic functions - these are implemented in {@link org.javasimon.AbstractSimon}.
 * Simons are organized in the tree - methods {@link org.javasimon.Simon#getChildren()} and
 * {@link org.javasimon.Simon#getParent()} can be used to traverse it along with
 * {@link org.javasimon.SimonFactory#getRootSimon()}. This tree is also important to determine if
 * the Simon is enabled or disabled. Disabled Simon has minimal possible overhead - but it does not count
 * or measure anything. Simon's state is specified by {@link org.javasimon.Simon#setState(SimonState, boolean)}.
 * {@link org.javasimon.SimonState} enumeration has three values: {@link org.javasimon.SimonState#ENABLED}
 * sets Simon as enabled, {@link org.javasimon.SimonState#DISABLED} sets Simon as disabled and
 * {@link org.javasimon.SimonState#INHERIT} lets Simon to inherit its state from the parent (recursively).
 * <p>
 * Simons by default provide only basic information without any additional statistical meaning.
 * {@link org.javasimon.StatProcessor} of some type can be attached to the Simon to get additional statistic information
 * for measured values.
 * <p>
 * TODO: Configuration
 */
package org.javasimon;