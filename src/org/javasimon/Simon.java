package org.javasimon;

import java.util.List;

/**
 * Simon.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 4, 2008
 */
public interface Simon {
	Simon getParent();

	List<Simon> getChildren();

	String getName();

	SimonState getState();

	void enable(boolean resetSubtree);

	void disable(boolean resetSubtree);

	void inheritState(boolean resetSubtree);

	void resetSubtreeState();

	boolean isEnabled();

	void reset();

	Simon getDisabledDecorator();
}
