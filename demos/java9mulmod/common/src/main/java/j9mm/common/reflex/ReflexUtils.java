package j9mm.common.reflex;

import java.lang.reflect.Field;

public class ReflexUtils {

	public static void setField(Object target, String fieldName, Object value) {
		Class<?> aClass = target.getClass();
		try {
			Field field = aClass.getField(fieldName);
			field.set(target, value);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new ReflexFailure("Setting field '" + fieldName + "' on object of class "
				+ aClass.getName() + " failed", e);
		}
	}
}
