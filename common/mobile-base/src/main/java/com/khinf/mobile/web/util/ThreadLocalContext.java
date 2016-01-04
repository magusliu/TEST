package com.khinf.mobile.web.util;

import java.util.HashSet;
import java.util.Set;

import org.daochong.lang.BeanUtil;

public class ThreadLocalContext {

	private static ThreadLocal<Set<Object>> LOCALES = new ThreadLocal<Set<Object>>();

	@SuppressWarnings("unchecked")
	public static <T> T get(Class<T> clazz) {
		if (clazz == null)
			return null;
		for (Object o : getObjects()) {
			if (o != null && o.getClass().equals(clazz)) {
				return (T) o;
			}
		}
		for (Object o : getObjects()) {
			if (o != null && BeanUtil.isMatch(o, clazz)) {
				return (T) o;
			}
		}
		return null;
	}

	private static Set<Object> getObjects() {
		Set<Object> os = LOCALES.get();
		if (os == null) {
			os = new HashSet<Object>();
			LOCALES.set(os);
		}
		return os;
	}

}
