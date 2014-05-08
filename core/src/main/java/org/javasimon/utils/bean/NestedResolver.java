package org.javasimon.utils.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class for resolving nested bean.
 *
 * @author <a href="mailto:ivan.mushketyk@gmail.com">Ivan Mushketyk</a>
 */
class NestedResolver {

	private Object nestedTarget;
	private String targetProperty;

	/**
	 * Resolve nested bean.
	 *
	 * @param target target class where nested bean will be resolved
	 * @param property nested property specification in format bean1.bean2.property
	 */
	NestedResolver(Object target, String property) {
		nestedTarget = target;
		targetProperty = property;

		resolveNested();
	}

	private void resolveNested() {
		int separatorPos;
		while ((separatorPos = targetProperty.indexOf('.')) > 0) {
			String property = targetProperty.substring(0, separatorPos);
			targetProperty = targetProperty.substring(separatorPos + 1);
			nestedTarget = resolveNestedTarget(nestedTarget, property);
			if (nestedTarget == null) {
				throw new BeanUtilsException(String.format("Nested bean %s is null", property));
			}
		}
	}

	private Object resolveNestedTarget(Object nestedTarget, String propertyName) {
		try {
			Method getter = ClassUtils.getGetter(nestedTarget.getClass(), propertyName);
			if (getter != null) {
				return getter.invoke(nestedTarget);
			}

			Field field = ClassUtils.getField(nestedTarget.getClass(), propertyName);
			if (field == null) {
				throw new BeanUtilsException(String.format("Failed to find property %s in %s", propertyName, nestedTarget));
			}

			field.setAccessible(true);
			return field.get(nestedTarget);
		} catch (IllegalAccessException e) {
			throw new BeanUtilsException(String.format("Failed to access property %s in object %s", propertyName, nestedTarget), e);
		} catch (InvocationTargetException e) {
			throw new BeanUtilsException(String.format("Failed to invoke getter %s in object %s", propertyName, nestedTarget), e);
		}
	}

	/**
	 * Get resolved nested target bean.
	 *
	 * @return resolved target object
	 */
	Object getNestedTarget() {
		return nestedTarget;
	}

	/**
	 * Return name of the property that should be set in the nested target object
	 *
	 * @return name of the property that should be set.
	 */
	String getProperty() {
		return targetProperty;
	}
}
