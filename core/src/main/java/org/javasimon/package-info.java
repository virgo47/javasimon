/**
 * Core package, contains {@link org.javasimon.Simon} implementations, with
 * the {@link org.javasimon.SimonManager} as the center point of the API.
 * <p>
 * Core package contains all basic interfaces ({@link org.javasimon.Simon}, {@link org.javasimon.Counter},
 * {@link org.javasimon.Stopwatch} and {@link org.javasimon.Manager})
 * with their implementations along with {@link org.javasimon.SimonManager} that provides convenient access
 * to the default Simon Manger.
 * <p>
 * There are two basic types of Simons provided:
 * <ul>
 * <li>Stopwatch that measures time and sums the time splits -
 * this is obtained by {@link org.javasimon.SimonManager#getStopwatch(String)};
 * <li>Counter that counts - occurences, or tracks the integer value - this is obtained
 * by {@link org.javasimon.SimonManager#getCounter(String)}.
 * </ul>
 * <p>
 * All Simons share some basic functions - these are implemented in {@link org.javasimon.AbstractSimon}.
 * Simons are organized in the tree - methods {@link org.javasimon.Simon#getChildren()} and
 * {@link org.javasimon.Simon#getParent()} can be used to traverse it along with
 * {@link org.javasimon.SimonManager#getRootSimon()}. This tree is also important to determine if
 * the Simon is enabled or disabled. Disabled Simon has minimal possible overhead - but it does not count
 * or measure anything. Simon's state is specified by {@link org.javasimon.Simon#setState(SimonState, boolean)}.
 * {@link org.javasimon.SimonState} enumeration has three values: {@link org.javasimon.SimonState#ENABLED}
 * sets Simon as enabled, {@link org.javasimon.SimonState#DISABLED} sets Simon as disabled and
 * {@link org.javasimon.SimonState#INHERIT} lets Simon to inherit its state from the parent (recursively).
 * <p>
 * The whole SimonManager can be enabled or disabled - this is importand difference from disabled Simons.
 * Whenever SimonManager is disabled ({@link org.javasimon.SimonManager#disable()}) any method returning
 * Simon returns {@link org.javasimon.NullSimon}. Null Simon is different from the disabled Simon because
 * it does nothing and always returns zero/empty values if value is expected. Null Simon is always disabled
 * and cannot be enabled. If the SimonManager is enabled again ({@link org.javasimon.SimonManager#enable()})
 * it returns real Simons again. This leads to following recommendations:
 * <ul>
 * <li>It is recommended always to use {@link org.javasimon.SimonManager#getStopwatch(String)} or
 * {@link org.javasimon.SimonManager#getCounter(String)} to get the Simon because this always
 * reflects the status of the SimonManager.
 * <li>If it is necessary to keep Simon reference be sure to obtain it while SimonManager is enabled.
 * <li>Rembember that real Simon is not disabled if the SimonManager is disabled!
 * </ul>
 * Bottom line: <b>If you want to use Simon, get it from SimonManager when needed and don not cache it.</b>
 * (Unless you know what you're doing.)
 * <p>
 * TODO: Configuration
 */
package org.javasimon;