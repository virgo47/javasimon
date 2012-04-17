/**
 * Machinery to convert potentially anything to a String (said differently a configurable toString() API).
 * The {@link CompositeStringifier} is a dictionnary which registers a {@link Stringifier} implementation
 * for each Java type (String, Long,etc). A given Java type can:<ul>
 * <li>Have different meanings (a long can be
 * a duration, a timestamp, a counter, etc): the "sub-type" concept was introduced.</li>
 * <li>Be styled differently (HTML, CSV, XML, JSON, etc): a dictionnary factory was introduced {@link StringifierFactory} and its subclasses
 * {@link org.javasimon.console.action.CsvStringifierFactory}, {@link org.javasimon.console.json.JsonStringifierFactory}, etc.</li>
 * </ul>
 */
package org.javasimon.console.text;