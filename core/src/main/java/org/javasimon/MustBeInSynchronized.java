package org.javasimon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for a method that should be called within a synchronized block. Annotation
 * does not ensure anything, it is a mere documentation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface MustBeInSynchronized {
}
