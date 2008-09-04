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
 * {@link org.javasimon.StatProcessor} of some type can be attached to the Simon to get additional statistic
 * information for measured values.
 * <p>
 * The whole SimonFactory can be enabled or disabled - this is importand difference from disabled Simons.
 * Whenever SimonFactory is disabled ({@link org.javasimon.SimonFactory#disable()}) any method returning
 * Simon returns {@link org.javasimon.NullSimon}. Null Simon is different from the disabled Simon because
 * it does nothing and always returns zero/empty values if value is expected. Null Simon is always disabled
 * and cannot be enabled. If the SimonFactory is enabled again ({@link org.javasimon.SimonFactory#enable()})
 * it returns real Simons again. This leads to following recommendations:
 * <ul>
 * <li>In any normal situation always use {@link org.javasimon.SimonFactory#getStopwatch(String)} or
 * {@link org.javasimon.SimonFactory#getCounter(String)} to get Simon of your choice. Result always
 * reflects status of the SimonFactory.
 * <li>If it is necessary to keep Simon reference be sure to obtain it while SimonFactory is enabled.
 * <li>Rembember that real Simon is not disabled if the SimonFactory is disabled!
 * </ul>
 * Bottom line: <b>If you want to use Simon, get it from SimonFactory when needed and don not cache it.</b>
 * (Unless you know what you're doing.)
 * <p>
 * TODO: Configuration
 */
package org.javasimon;