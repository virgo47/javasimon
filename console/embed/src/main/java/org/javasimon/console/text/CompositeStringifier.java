/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javasimon.console.text;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gquintana
 */
public class CompositeStringifier implements Stringifier<Object> {
	private static final class StringifierKey {
		private final Class<?> type;
		private final String subType;
		public StringifierKey(Class<?> type, String subType) {
			this.type = type;
			this.subType = subType;
		}
		public StringifierKey(Class<?> type) {
			this(type, null);
		}
		private boolean equalsTo(Object o1, Object o2) {
			if (o1==o2) {
				return true;
			} else {
				if ((o1==null)||(o2==null)) {
					return false;
				} else {
					return o1.equals(o2);
				}
			}
		}
		private int hashCode(Object o) {
			return o==null?0:o.hashCode();
		}
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final StringifierKey other = (StringifierKey) obj;
			if (!equalsTo(this.type,other.type)) {
				return false;
			}
			if (!equalsTo(this.subType,other.subType)) {
				return false;
			}
			return true;
		}

		@Override
		public int hashCode() {
			return 3+5*hashCode(this.type)+7*hashCode(this.subType);
		}
		@Override
		public String toString() {
			return "StringifierKey["+type.getName()+","+subType+"]";
		}
	}
	private final Map<StringifierKey,Stringifier> stringifiers=new HashMap<StringifierKey,Stringifier>();
	private Stringifier nullStringifier;
	private Stringifier defaultStringifier;
	public final <T> void add(Class<? extends T> type, Stringifier<T> stringifier) {
		stringifiers.put(new StringifierKey(type), stringifier);
	}
	public final <T> void add(Class<? extends T> type, String name, Stringifier<T> stringifier) {
		stringifiers.put(new StringifierKey(type, name), stringifier);
	}
	public final <T> Stringifier<T> get(Class<? extends T> type) {
		return stringifiers.get(new StringifierKey(type));
	}
	public final <T> Stringifier<T> get(Class<? extends T> type, String subType) {
		Stringifier<T> stringifier=null;
		if (subType!=null) {
			stringifier=stringifiers.get(new StringifierKey(type, subType));
		}
		if (stringifier==null) {
			stringifier=get(type);
		}
		return stringifier;
	}
	private <T> Stringifier getForInstance(T object, String subType) {
		Stringifier<T> stringifier;
		if (object==null) {
			stringifier=nullStringifier;
		} else {
			stringifier=(Stringifier<T>) get(object.getClass(), subType);
			if (stringifier==null) {
				stringifier=defaultStringifier;
			}
		}
		return stringifier;
	}
	public String toString(Object object) {
		return getForInstance(object, null).toString(object);
	}
	public String toString(Object object, String subType) {
		return getForInstance(object, subType).toString(object);
	}

	public Stringifier getDefaultStringifier() {
		return defaultStringifier;
	}

	public void setDefaultStringifier(Stringifier defaultStringifier) {
		this.defaultStringifier = defaultStringifier;
	}

	public Stringifier getNullStringifier() {
		return nullStringifier;
	}

	public void setNullStringifier(Stringifier nullStringifier) {
		this.nullStringifier = nullStringifier;
	}
	
}
