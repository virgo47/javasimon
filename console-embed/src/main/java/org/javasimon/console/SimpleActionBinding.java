package org.javasimon.console;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Simple action bindings which is triggered by the path and produces
 * actions of given type
 *
 * @author gquintana
 */
@SuppressWarnings("UnusedDeclaration")
public class SimpleActionBinding<T extends Action> implements ActionBinding<T> {
	/**
	 * Path
	 */
	private final String path;
	/**
	 * Constructor used to instantiate actions
	 */
	private final Class<T> actionClass;
	/**
	 * Constructor used to instantiate actions
	 */
	private final Constructor<T> actionConstructor;

	/**
	 * Constructor.
	 *
	 * @param path Supported path.
	 * @param actionClass Create Action class
	 */
	public SimpleActionBinding(String path, Class<T> actionClass) {
		this.path = path;
		this.actionClass = actionClass;
		Constructor<T> constructor;
		try {
			try {
				constructor = actionClass.getConstructor(ActionContext.class);
			} catch (NoSuchMethodException noSuchMethodException) {
				constructor = actionClass.getConstructor();
			}
		} catch (SecurityException securityException) {
			throw new IllegalStateException("Can get constructor for class " + actionClass.getName(), securityException);
		} catch (NoSuchMethodException noSuchMethodException) {
			throw new IllegalArgumentException("Can find constructor for class " + actionClass.getName());
		}
		this.actionConstructor = constructor;
	}

	/**
	 * Returns true when {@link ActionContext#getPath()} equals {@link #path}.
	 */
	public boolean supports(ActionContext actionContext) {
		return actionContext.getPath().equals(this.path);
	}

	/**
	 * Create a new {@link Action} using {@link #actionConstructor}.
	 */
	public T create(ActionContext actionContext) {
		try {
			T action;
			Class[] actionConstructorParams = actionConstructor.getParameterTypes();
			switch (actionConstructorParams.length) {
				case 0:
					action = actionConstructor.newInstance();
					break;
				case 1:
					if (actionConstructorParams[0].equals(ActionContext.class)) {
						action = actionConstructor.newInstance(actionContext);
					} else {
						throw new IllegalStateException("Unknown argument type in action constructor " + actionConstructorParams[0].getName());
					}
					break;
				default:
					throw new IllegalStateException("Invalid argument number in action constructor: " + actionConstructorParams.length);
			}
			return action;
		} catch (InstantiationException instantiationException) {
			throw new IllegalStateException("Failed to create action", instantiationException);
		} catch (IllegalAccessException illegalAccessException) {
			throw new IllegalStateException("Failed to create action", illegalAccessException);
		} catch (InvocationTargetException invocationTargetException) {
			throw new IllegalStateException("Failed to create action", invocationTargetException);
		}
	}

	public Constructor<T> getActionConstructor() {
		return actionConstructor;
	}

	public Class<T> getActionClass() {
		return actionClass;
	}

	public String getPath() {
		return path;
	}
}
