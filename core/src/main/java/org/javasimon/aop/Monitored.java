package org.javasimon.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark classes and/or methods that should be monitored. A method is monitored when it is annotated,
 * or when it is in a class that is annotated (or any of it's subclasses). {@link org.javasimon.Stopwatch} is used
 * as a monitor.
 * <p/>
 * The Stopwatch name consists of a "name" part and a "suffix" part - these parts are provided using parameters of
 * the annotation. Rules are as follows:
 * <ul>
 * <li>If method annotation has the name parameter it will be the name of the Stopwatch (suffix is ignored).</li>
 * <li>Default name part is the fully qualified class name.</li>
 * <li>Default suffix part is the name of the current method.</li>
 * <li>Name can be overruled by class annotation parameter, this can be overruled by method annotation parameter.</li>
 * <li>Suffix parameter overrules default method name. Suffix parameter is ignored on the class annotation.</li>
 * </ul>
 * If no parameter is used, name of the Stopwatch will be: {@code fully.qualified.ClassName.methodName}
 * <p/>
 * Current name resolution applies since version 3.1.
 *
 * @author Erik van Oosten
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Monitored {
	/**
	 * Returns the name for the {@link org.javasimon.Stopwatch} without its possible suffix - default is the class name.
	 * Using the parameter on the method annotation overrides name from the class.
	 *
	 * @return name of the used monitor
	 */
	String name() default "";

	/**
	 * Part added after the name part (which defaults to the class name or is specified on the class annotation). This
	 * parameter is ignored for the class annotation or if the name parameter was specified on the method
	 * annotation. If not ignored, it is added to the Stopwatch name after the Simon name separator (.).
	 *
	 * @return suffix to be added to the name of the monitor
	 */
	String suffix() default "";
}
