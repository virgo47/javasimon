package org.javasimon.console;

/**
 * Interface describing the binding between an HTTP Request and an {@link Action}
 * @author gquintana
 */
public interface ActionBinding<T extends Action> {
	/**
	 * Indicates whether this action binding is applicable for this action
	 * context (=HTTP Request)
	 * @param actionContext Action context
	 * @return true if this binding is in charge of this request
	 */
	public boolean supports(ActionContext actionContext);
	/**
	 * Create an action of this action context, this method will be
	 * called only if this binding {@link #supports} this context.
	 * @param actionContext Action context
	 * @return New instance of an action
	 */
	public T create(ActionContext actionContext);
}
