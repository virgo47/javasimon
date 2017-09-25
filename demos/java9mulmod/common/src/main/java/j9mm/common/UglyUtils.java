package j9mm.common;

import j9mm.common.reflex.ReflexUtils;

public class UglyUtils {

	public static String reverse(String s) {
		return new StringBuilder(s).reverse().toString();
	}

	public static void setField(Object target, String fieldName, Object value) {
		ReflexUtils.setField(target, fieldName, value);
	}
}
