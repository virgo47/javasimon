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

	void addChild(Simon simon);

	String getName();

	SimonState getState();

	void enable();

	void disable();

	void inheritState();

	void setSubtreeToInherit();

	boolean isEnabled();

	void reset();
}
