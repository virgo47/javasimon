package org.javasimon;

/**
 * AbstractSimonWithDisabledBehavior extends AbstractSimon with current effective state (enable/disable)
 * explicitly stored in the object. It can be used to implement single class Simons without interface and
 * explicit class for disabled version.
 *
 * @author <a href="mailto:virgo47@gmail.com">Richard "Virgo" Richter</a>
 * @created Aug 8, 2008
 * @see DisabledBehavior
 */
public abstract class AbstractSimonWithDisabledBehavior extends AbstractSimon implements Simon, DisabledBehavior {
	protected boolean enabled = true;

	public AbstractSimonWithDisabledBehavior(String name) {
		super(name);
	}

	public final void nowDisabled() {
		enabled = false;
	}

	public final void nowEnabled() {
		enabled = true;
	}

	public final Simon getDisabledDecorator() {
		return this;
	}
}