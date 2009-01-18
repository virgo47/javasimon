package org.javasimon;

/**
 * Sample contains all relevant values of the Simon that are obtained by the
 * {@link org.javasimon.Simon#sample()} and {@link org.javasimon.Simon#sampleAndReset()} methods.
 * Returned object contains consistent set of Simon values as these operations are synchronized.
 * However Sample is a Java Bean and it can be modified afterwards so no consistency is guaranteed
 * when the object is used in an inapropriate context. Object is Java Bean (mutable) so it can be
 * used directly as the data transfer object without need to create another DTO with the same data.
 * Sample generally doesn't have any behavior.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Jan 7, 2009
 */
public interface Sample {
}
