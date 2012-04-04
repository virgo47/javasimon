/**
 * {@link org.javasimon.callback.Callback} is an event listener registered with the {@link org.javasimon.Manager},
 * listening for manager or Simon events. Any number of callbacks can be registered on a manager using
 * {@link org.javasimon.Manager#callback()} and {@link org.javasimon.callback.CompositeCallback#addCallback(Callback)}.
 * <p/>
 * Non-composite callbacks are used to perform expected actions for particular events.
 * {@link org.javasimon.callback.CallbackSkeleton} can be extended if only selected events are desired to be
 * implemented instead of implementating the whole {@link org.javasimon.callback.Callback} interface.
 * <p/>
 * Composite callbacks are used to organize other callbacks in trees and then to delegate all events to them.
 * It is not recommended to mix composite callbacks with functionality, generally it should not be necessary to implement
 * composite callbacks in addition to the following two provided implementations:
 * <ul>
 *     <li>{@link org.javasimon.callback.CompositeCallbackImpl} delegates all events to all sub-callbacks
 *     (used in {@link org.javasimon.EnabledManager} for instance);</li>
 *     <li>{@link org.javasimon.callback.CompositeFilterCallback} allows to filter events that should be propagated to the sub-callbacks,
 *     filter rules can be added using
 *     {@link org.javasimon.callback.FilterCallback#addRule(org.javasimon.callback.FilterRule.Type, String, String, org.javasimon.callback.Callback.Event...)}.</li>
 * </ul>
 */
package org.javasimon.callback;