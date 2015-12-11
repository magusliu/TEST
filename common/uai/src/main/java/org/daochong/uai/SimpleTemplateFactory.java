package org.daochong.uai;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("all")
public abstract class SimpleTemplateFactory<T> implements TemplateFactory<T> {

	private boolean singleton;
	private boolean threadLocal;

	public Class<?> getInfoClass() {
		return null;
	}

	private Class<T> clazz = null;

	public SimpleTemplateFactory() {
		Type genType = this.getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		this.clazz = (Class<T>) params[0];
	}

	public Class<T> getBeanClass() {
		return clazz;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public boolean isThreadLocal() {
		return threadLocal;
	}

	public void setThreadLocal(boolean threadLocal) {
		this.threadLocal = threadLocal;
	}

	public void destory(T obj) {

	}

}
